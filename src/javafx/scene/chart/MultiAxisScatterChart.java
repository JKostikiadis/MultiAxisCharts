package javafx.scene.chart;

import javafx.scene.chart.NumberAxis;

public class MultiAxisScatterChart extends MutliAxisChart {

	public MultiAxisScatterChart(NumberAxis xAxis, NumberAxis y1Axis, NumberAxis y2Axis) {
		super(xAxis, y1Axis, y2Axis);
	}

	public MultiAxisScatterChart(int width, int height, NumberAxis xAxis, NumberAxis y1Axis, NumberAxis y2Axis) {
		super(xAxis, y1Axis, y2Axis);
		setPrefSize(width, height);
	}

}
