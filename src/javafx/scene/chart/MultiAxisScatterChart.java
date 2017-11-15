package javafx.scene.chart;

import javafx.collections.ObservableList;

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
		ObservableList<ChartValue<T>> data = getData();

		ObservableList<?> xAxisMarks = getXAxis().getTickMarks();
		ObservableList<?> y1AxisMarks = getYAxis(MutliAxisChart.LEFT_AXIS).getTickMarks();
		ObservableList<?> y2AxisMarks = getYAxis(MutliAxisChart.RIGHT_AXIS).getTickMarks();

		for (ChartValue<T> value : data) {
			T xValue = value.getXValue();
			Number yValue = value.getYValue();
			System.out.println(xValue + " " + yValue);
		}

	}

}
