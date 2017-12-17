package com.kostikiadis.test;

import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.MultiAxisAreaChart;
import javafx.scene.chart.MultiAxisBarChart;
import javafx.scene.chart.MultiAxisChart;
import javafx.scene.chart.MultiAxisLineChart;
import javafx.scene.chart.MultiAxisScatterChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

public class TestFrameController {

	public static final int SCATTER_CHART = 0;
	public static final int BAR_CHART = 1;
	public static final int LINE_CHART = 2;
	public static final int AREA_CHART = 3;
	public static final int CATEGORY_AXIS = 0;
	public static final int NUMBER_AXIS = 1;

	@FXML
	private BorderPane chartPane;

	@FXML
	private ComboBox<String> chartTypeComboBox;

	@FXML
	private ComboBox<String> xAxisTypeComboBox;

	@FXML
	private CheckBox secondAxisCheckBox;

	@FXML
	private CheckBox backgroundGridCheckBox;

	@FXML
	private ComboBox<String> y1RegressionComboBox;

	@FXML
	private ComboBox<String> y2RegressionComboBox;

	private int chartType;
	private int xAxisType;
	private boolean hasSecondAxis = true;
	private boolean hasBackgroundGrid = true;

	@FXML
	public void initialize() {
		chartTypeComboBox.getItems().addAll(new String[] { "Scatter Chart", "Bar Chart", "Line Chart", "Area Chart" });
		chartTypeComboBox.setOnAction(e -> {
			chartType = chartTypeComboBox.getSelectionModel().getSelectedIndex();
			updateChart();
		});

		xAxisTypeComboBox.getItems().addAll(new String[] { "CategoryAxis", "NumberAxis" });
		xAxisTypeComboBox.setOnAction(e -> {
			xAxisType = xAxisTypeComboBox.getSelectionModel().getSelectedIndex();
			updateChart();
		});

		secondAxisCheckBox.setOnAction(e -> {
			hasSecondAxis = secondAxisCheckBox.isSelected();
			updateChart();
		});

		backgroundGridCheckBox.setOnAction(e -> {
			hasBackgroundGrid = backgroundGridCheckBox.isSelected();
			updateChart();
		});

		xAxisTypeComboBox.getSelectionModel().select(0);
		chartTypeComboBox.getSelectionModel().select(0);
		updateChart();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateChart() {

		MultiAxisChart chart = null;
		Axis<?> xAxis = null;
		NumberAxis y1Axis = null;
		NumberAxis y2Axis = null;

		y1Axis = new NumberAxis(0, 80, 10);
		y1Axis.setLabel("Force (N)");

		if (hasSecondAxis) {
			y2Axis = new NumberAxis(0, 10, 1);
			y2Axis.setLabel("Speed");
		}

		XYChart.Series series1 = new XYChart.Series();
		series1.setName("April");

		XYChart.Series series2 = new XYChart.Series();
		series2.setName("May");

		if (xAxisType == CATEGORY_AXIS) {
			xAxis = new CategoryAxis();

			((CategoryAxis) xAxis).setCategories(FXCollections.<String>observableArrayList(
					Arrays.asList(new String[] { "Power", "Force", "Agility", "Balance", "Speed" })));

			series1.getData().add(new XYChart.Data("Power", 4, MultiAxisChart.LEFT_AXIS));
			series1.getData().add(new XYChart.Data("Force", 10, MultiAxisChart.LEFT_AXIS));
			series1.getData().add(new XYChart.Data("Agility", 15, MultiAxisChart.LEFT_AXIS));
			series1.getData().add(new XYChart.Data("Balance", 8, MultiAxisChart.LEFT_AXIS));
			series1.getData().add(new XYChart.Data("Speed", 5, MultiAxisChart.LEFT_AXIS));

			series2.getData().add(new XYChart.Data("Power", 20, MultiAxisChart.LEFT_AXIS));
			series2.getData().add(new XYChart.Data("Force", 15, MultiAxisChart.LEFT_AXIS));
			series2.getData().add(new XYChart.Data("Agility", 13, MultiAxisChart.LEFT_AXIS));
			series2.getData().add(new XYChart.Data("Balance", 12, MultiAxisChart.LEFT_AXIS));
			series2.getData().add(new XYChart.Data("Speed", 14, MultiAxisChart.LEFT_AXIS));

		} else {
			xAxis = new NumberAxis(0, 15, 1);

			series1.getData().add(new XYChart.Data(1, 4, MultiAxisChart.LEFT_AXIS));
			series1.getData().add(new XYChart.Data(3, 10, MultiAxisChart.LEFT_AXIS));
			series1.getData().add(new XYChart.Data(6, 15, MultiAxisChart.LEFT_AXIS));
			series1.getData().add(new XYChart.Data(9, 8, MultiAxisChart.LEFT_AXIS));
			series1.getData().add(new XYChart.Data(12, 5, MultiAxisChart.LEFT_AXIS));
			series1.getData().add(new XYChart.Data(15, 18, MultiAxisChart.LEFT_AXIS));
			series1.getData().add(new XYChart.Data(18, 15, MultiAxisChart.LEFT_AXIS));
			series1.getData().add(new XYChart.Data(21, 13, MultiAxisChart.LEFT_AXIS));
			series1.getData().add(new XYChart.Data(24, 19, MultiAxisChart.LEFT_AXIS));
			series1.getData().add(new XYChart.Data(27, 21, MultiAxisChart.LEFT_AXIS));
			series1.getData().add(new XYChart.Data(30, 21, MultiAxisChart.LEFT_AXIS));

			series2.getData().add(new XYChart.Data(0, 20, MultiAxisChart.LEFT_AXIS));
			series2.getData().add(new XYChart.Data(3, 15, MultiAxisChart.LEFT_AXIS));
			series2.getData().add(new XYChart.Data(6, 13, MultiAxisChart.LEFT_AXIS));
			series2.getData().add(new XYChart.Data(9, 12, MultiAxisChart.LEFT_AXIS));
			series2.getData().add(new XYChart.Data(12, 14, MultiAxisChart.LEFT_AXIS));
			series2.getData().add(new XYChart.Data(15, 18, MultiAxisChart.LEFT_AXIS));
			series2.getData().add(new XYChart.Data(18, 25, MultiAxisChart.LEFT_AXIS));
			series2.getData().add(new XYChart.Data(21, 25, MultiAxisChart.LEFT_AXIS));
			series2.getData().add(new XYChart.Data(24, 23, MultiAxisChart.LEFT_AXIS));
			series2.getData().add(new XYChart.Data(27, 26, MultiAxisChart.LEFT_AXIS));
			series2.getData().add(new XYChart.Data(31, 26, MultiAxisChart.LEFT_AXIS));
		}
		xAxis.setLabel("Load (kg)");

		if (chartType == SCATTER_CHART) {
			chart = new MultiAxisScatterChart(400, 400, xAxis, y1Axis, y2Axis);
		} else if (chartType == BAR_CHART) {
			chart = new MultiAxisBarChart(400, 400, xAxis, y1Axis, y2Axis);
		} else if (chartType == LINE_CHART) {
			chart = new MultiAxisLineChart(400, 400, xAxis, y1Axis, y2Axis);
		} else {
			chart = new MultiAxisAreaChart(400, 400, xAxis, y1Axis, y2Axis);
		}

		chart.setTitle("Force, Power/Load");
		chart.setBackgroundGrid(hasBackgroundGrid);
		chart.getData().addAll(series1, series2);

		chartPane.getChildren().clear();
		chartPane.setCenter(chart);
	}
}
