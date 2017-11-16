package com.kostikiadis.EnchandedFXCharts;

import java.util.Arrays;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.MultiAxisScatterChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class Test extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primary) throws Exception {

		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setCategories(FXCollections.<String>observableArrayList(
				Arrays.asList(new String[] { "Power", "Force", "Mass", "Gravity", "Programming" })));

		xAxis.setLabel("Load (kg)");

		NumberAxis y1Axis = new NumberAxis(-100, 80, 10);
		y1Axis.setLabel("Force (N)");

		NumberAxis y2Axis = new NumberAxis(0, 10, 1);
		y2Axis.setLabel("Speed");

		MultiAxisScatterChart chart = new MultiAxisScatterChart(850, 500, xAxis, y1Axis, y2Axis);
		chart.setTitle("Force, Power/Load");

		XYChart.Series series1 = new XYChart.Series();
		series1.setName("2003");

		series1.getData().add(new XYChart.Data("Power", -50, MultiAxisScatterChart.LEFT_AXIS));
		series1.getData().add(new XYChart.Data("Mass", 60, MultiAxisScatterChart.LEFT_AXIS));
		series1.getData().add(new XYChart.Data("Programming", 0, MultiAxisScatterChart.LEFT_AXIS));

		chart.getData().add(series1);

		primary.setScene(new Scene(chart));
		primary.show();

	}
}
// Hierarchy