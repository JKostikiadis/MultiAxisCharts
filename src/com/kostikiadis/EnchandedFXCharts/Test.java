package com.kostikiadis.EnchandedFXCharts;

import java.util.Arrays;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.MultiAxisScatterChart;
import javafx.scene.chart.MutliAxisChart.ChartValue;
import javafx.scene.chart.NumberAxis;
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

		MultiAxisScatterChart<String> chart = new MultiAxisScatterChart<String>(850, 500, xAxis, y1Axis, y2Axis);
		chart.setTitle("Force, Power/Load");

		ObservableList<ChartValue<String>> data = FXCollections.observableArrayList();

		data.add(new ChartValue<String>("Power", 60, MultiAxisScatterChart.LEFT_AXIS));
		data.add(new ChartValue<String>("Mass", 40, MultiAxisScatterChart.LEFT_AXIS));
		data.add(new ChartValue<String>("Programming", 18, MultiAxisScatterChart.LEFT_AXIS));

		chart.setData(data);

		primary.setScene(new Scene(chart));
		primary.show();

	}
}
// Hierarchy