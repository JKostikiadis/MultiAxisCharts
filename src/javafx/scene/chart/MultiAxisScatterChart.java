package javafx.scene.chart;

import javafx.collections.ObservableList;
import javafx.scene.shape.Circle;

public class MultiAxisScatterChart<T> extends MutliAxisChart<T> {

	public MultiAxisScatterChart(Axis<?> xAxis, NumberAxis y1Axis, NumberAxis y2Axis) {
		super(xAxis, y1Axis, y2Axis);
	}

	public MultiAxisScatterChart(int width, int height, Axis<?> xAxis, NumberAxis y1Axis, NumberAxis y2Axis) {
		this(xAxis, y1Axis, y2Axis);
		setPrefSize(width, height);
		drawValues();
	}

	@Override
	public void drawValues() {
		super.drawValues();
		ObservableList<ChartValue<T>> data = getData();

		NumberAxis y1Axis = (NumberAxis) getYAxis(MutliAxisChart.LEFT_AXIS);
		NumberAxis y2Axis = (NumberAxis) getYAxis(MutliAxisChart.RIGHT_AXIS);

		if (getXAxis() instanceof CategoryAxis) {

			CategoryAxis xAxis = (CategoryAxis) getXAxis();

			for (ChartValue<T> value : data) {
				String xValue = (String) value.getXValue();
				Number yValue = value.getYValue();

				double xPosition = xAxis.getDisplayPosition(xValue) + xAxis.getLayoutX();
				double yPosition;

				if (value.getYAxisSide() == MutliAxisChart.LEFT_AXIS) {
					yPosition = y1Axis.getDisplayPosition(yValue) + y1Axis.getLayoutY();
				} else {
					yPosition = y2Axis.getDisplayPosition(yValue) + y2Axis.getLayoutY();
				}

				Circle c = new Circle(5);
				c.setCenterX(xPosition);
				c.setCenterY(yPosition);

				chartValues.add(c);
			}
		} else {

		}
		plotPane.getChildren().addAll(chartValues);
	}

}
