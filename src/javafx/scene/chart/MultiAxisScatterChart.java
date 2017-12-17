package javafx.scene.chart;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MultiAxisScatterChart extends MultiAxisChart {

	public MultiAxisScatterChart(Axis<?> xAxis, NumberAxis y1Axis, NumberAxis y2Axis) {
		super(xAxis, y1Axis, y2Axis);
	}

	public MultiAxisScatterChart(int width, int height, Axis<?> xAxis, NumberAxis y1Axis, NumberAxis y2Axis) {
		this(xAxis, y1Axis, y2Axis);
		setPrefSize(width, height);
	}

	@Override
	public void drawValues() {
		super.drawValues();

		ObservableList<XYChart.Series> data = getData();

		NumberAxis y1Axis = (NumberAxis) getYAxis(MultiAxisChart.LEFT_AXIS);
		NumberAxis y2Axis = (NumberAxis) getYAxis(MultiAxisChart.RIGHT_AXIS);

		int seriesIndex = 0;
		for (XYChart.Series serie : data) {
			ObservableList<XYChart.Data> dataSeries = serie.getData();

			for (XYChart.Data value : dataSeries) {
				String xValue = value.getXValue().toString();
				Number yValue = (Number) value.getYValue();

				double xPosition, yPosition;

				if (getXAxis() instanceof CategoryAxis) {
					xPosition = ((CategoryAxis) getXAxis()).getDisplayPosition(xValue)
							+ ((CategoryAxis) getXAxis()).getLayoutX();
				} else {
					xPosition = ((NumberAxis) getXAxis()).getDisplayPosition(Double.parseDouble(xValue))
							+ ((NumberAxis) getXAxis()).getLayoutX();
				}

				if (((int) value.getExtraValue()) == MultiAxisChart.LEFT_AXIS) {
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
