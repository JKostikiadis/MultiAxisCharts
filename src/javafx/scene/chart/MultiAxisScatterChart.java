package javafx.scene.chart;

import javafx.collections.ObservableList;
import javafx.scene.shape.Rectangle;

public class MultiAxisScatterChart extends MutliAxisChart {

	public MultiAxisScatterChart(CategoryAxis xAxis, NumberAxis y1Axis, NumberAxis y2Axis) {
		super(xAxis, y1Axis, y2Axis);
	}

	public MultiAxisScatterChart(int width, int height, CategoryAxis xAxis, NumberAxis y1Axis, NumberAxis y2Axis) {
		this(xAxis, y1Axis, y2Axis);
		setPrefSize(width, height);
		drawValues();
	}

	@Override
	public void drawValues() {
		super.drawValues();

		ObservableList<XYChart.Series> data = getData();

		CategoryAxis xAxis = (CategoryAxis) getXAxis();
		NumberAxis y1Axis = (NumberAxis) getYAxis(MutliAxisChart.LEFT_AXIS);
		NumberAxis y2Axis = (NumberAxis) getYAxis(MutliAxisChart.RIGHT_AXIS);

		for (XYChart.Series serie : data) {
			ObservableList<XYChart.Data> dataSeries = serie.getData();

			for (XYChart.Data value : dataSeries) {
				String xValue = (String) value.getXValue();
				Number yValue = (Number) value.getYValue();

				double xPosition = xAxis.getDisplayPosition(xValue) + xAxis.getLayoutX();
				double yPosition;

				if (((int) value.getExtraValue()) == MutliAxisChart.LEFT_AXIS) {
					yPosition = y1Axis.getDisplayPosition(yValue) + y1Axis.getLayoutY();
				} else {
					yPosition = y2Axis.getDisplayPosition(yValue) + y2Axis.getLayoutY();
				}

				Rectangle valuePane = new Rectangle();
				valuePane.setWidth(10);
				valuePane.setHeight(y1Axis.getLayoutX() + y1Axis.getHeight() - yPosition + 2);

				valuePane.setLayoutX(xPosition - valuePane.getWidth() / 2);
				valuePane.setLayoutY(yPosition);
				valuePane.setStyle(getSeriesStyle(0));
				valuePane.toFront();

				chartValues.add(valuePane);
			}
		}

		plotPane.getChildren().addAll(chartValues);
	}

	private String getSeriesStyle(int i) {
		return "-fx-background-color: CHART_COLOR_1;";
	}

}
