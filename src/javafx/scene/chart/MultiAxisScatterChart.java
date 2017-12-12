package javafx.scene.chart;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MultiAxisScatterChart extends MutliAxisChart {

	public MultiAxisScatterChart(CategoryAxis xAxis, NumberAxis y1Axis, NumberAxis y2Axis) {
		super(xAxis, y1Axis, y2Axis);
	}

	public MultiAxisScatterChart(int width, int height, CategoryAxis xAxis, NumberAxis y1Axis, NumberAxis y2Axis) {
		this(xAxis, y1Axis, y2Axis);
		setPrefSize(width, height);
		drawValues();
	}

	public MultiAxisScatterChart(int width, int height, NumberAxis xAxis, NumberAxis y1Axis, NumberAxis y2Axis) {
		super(xAxis, y1Axis, y2Axis);
		setPrefSize(width, height);
		drawValues();
	}

	@Override
	public void drawValues() {
		super.drawValues();
		ObservableList<XYChart.Series> data = getData();

		NumberAxis y1Axis = (NumberAxis) getYAxis(MutliAxisChart.LEFT_AXIS);
		NumberAxis y2Axis = (NumberAxis) getYAxis(MutliAxisChart.RIGHT_AXIS);

		int seriesIndex = 0;
		for (XYChart.Series serie : data) {
			ObservableList<XYChart.Data> dataSeries = serie.getData();

			for (XYChart.Data value : dataSeries) {
				String xValue = value.getXValue().toString();
				Number yValue = (Number) value.getYValue();

				double xPosition;
				
				if (getXAxis() instanceof CategoryAxis) {
					xPosition = ((CategoryAxis) getXAxis()).getDisplayPosition(xValue)
							+ ((CategoryAxis) getXAxis()).getLayoutX();
				} else {
					xPosition = ((NumberAxis) getXAxis()).getDisplayPosition(Double.parseDouble(xValue))
							+ ((NumberAxis) getXAxis()).getLayoutX();
				}

				double yPosition;

				if (((int) value.getExtraValue()) == MutliAxisChart.LEFT_AXIS) {
					yPosition = y1Axis.getDisplayPosition(yValue) + y1Axis.getLayoutY();
				} else {
					yPosition = y2Axis.getDisplayPosition(yValue) + y2Axis.getLayoutY();
				}

				Circle valueShape = new Circle(5);
				valueShape.setFill(Color.web(DEFAULT_COLORS[seriesIndex % DEFAULT_COLORS.length]));

				valueShape.setCenterX(xPosition);
				valueShape.setCenterY(yPosition);
				valueShape.toFront();

				chartValues.add(valueShape);
			}
			seriesIndex++;
		}

		plotPane.getChildren().addAll(chartValues);
	}

}
