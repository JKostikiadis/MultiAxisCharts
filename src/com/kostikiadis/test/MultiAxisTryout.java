package com.kostikiadis.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.MultiAxisChart;
import javafx.scene.chart.MultiAxisScatterChart;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;

public class MultiAxisTryout extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

		stage.setTitle("Bar Chart Multi Example");

		NumberAxis xAxis = new NumberAxis(900, 1700, 100);
		NumberAxis yAxis = new NumberAxis(1700, 2650, 50);
		NumberAxis y2Axis = new NumberAxis(1800, 2800, 50);

		MultiAxisScatterChart chart = new MultiAxisScatterChart(xAxis, yAxis, y2Axis);
		chart.setTitle("Just an example");
		chart.setPrefSize(500, 500);

		MultiAxisChart.Series<Number, Number> series1 = new MultiAxisChart.Series<Number, Number>();
		series1.setName("April");

		MultiAxisChart.Series<Number, Number> series2 = new MultiAxisChart.Series<Number, Number>();
		series2.setName("May");

		series1.getData().add(new MultiAxisChart.Data<Number, Number>(1000, 2298, MultiAxisChart.Y1_AXIS));
		series1.getData().add(new MultiAxisChart.Data<Number, Number>(1100, 2193, MultiAxisChart.Y1_AXIS));
		series1.getData().add(new MultiAxisChart.Data<Number, Number>(1200, 2469, MultiAxisChart.Y1_AXIS));
		series1.getData().add(new MultiAxisChart.Data<Number, Number>(1300, 2332, MultiAxisChart.Y1_AXIS));
		series1.getData().add(new MultiAxisChart.Data<Number, Number>(1400, 2404, MultiAxisChart.Y1_AXIS));
		series1.getData().add(new MultiAxisChart.Data<Number, Number>(1500, 2399, MultiAxisChart.Y1_AXIS));
		series1.getData().add(new MultiAxisChart.Data<Number, Number>(1600, 2240, MultiAxisChart.Y1_AXIS));

		series2.getData().add(new MultiAxisChart.Data<Number, Number>(1000, 1889, MultiAxisChart.Y2_AXIS));
		series2.getData().add(new MultiAxisChart.Data<Number, Number>(1100, 1935, MultiAxisChart.Y2_AXIS));
		series2.getData().add(new MultiAxisChart.Data<Number, Number>(1200, 2337, MultiAxisChart.Y2_AXIS));
		series2.getData().add(new MultiAxisChart.Data<Number, Number>(1300, 2196, MultiAxisChart.Y2_AXIS));
		series2.getData().add(new MultiAxisChart.Data<Number, Number>(1400, 2398, MultiAxisChart.Y2_AXIS));
		series2.getData().add(new MultiAxisChart.Data<Number, Number>(1500, 2579, MultiAxisChart.Y2_AXIS));
		series2.getData().add(new MultiAxisChart.Data<Number, Number>(1600, 2601, MultiAxisChart.Y2_AXIS));

		chart.getData().addAll(series1, series2);

		chart.setRegression(MultiAxisChart.Y1_AXIS, MultiAxisChart.DEGREE_NUM2); // quadratic
		chart.setRegression(MultiAxisChart.Y2_AXIS, MultiAxisChart.DEGREE_NUM1); // linear
		chart.setRegressionColor(MultiAxisChart.Y2_AXIS, 0, "#FBA71B");

		Scene scene = new Scene(chart, 800, 600);

		stage.setScene(scene);
		stage.show();

	}
}