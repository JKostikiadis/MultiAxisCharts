package com.kostikiadis.EnchandedFXCharts;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;

public class Test extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primary) throws Exception {

		// new CustomAxis(minValue, maxValue, tickValue)
		NumberAxis xAxis = new NumberAxis(0, 10, 1);
		xAxis.setLabel("Load (kg)");

		NumberAxis y1Axis = new NumberAxis(0, 10, 1);
		y1Axis.setLabel("Force (N)");

		NumberAxis y2Axis = new NumberAxis(0, 10, 1);
		y2Axis.setLabel("Speed");

		// new MultiAxisScatterChart(width, height , . . . );
		MultiAxisScatterChart chart = new MultiAxisScatterChart(850, 500, xAxis, y1Axis, y2Axis);
		chart.setTitle("Force, Power/Load");

		primary.setScene(new Scene(chart));
		primary.show();

	}
}
 // Hierarchy 