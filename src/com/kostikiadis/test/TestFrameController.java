package com.kostikiadis.test;

import java.util.Arrays;
import java.util.Optional;

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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

@SuppressWarnings({ "unchecked", "rawtypes" })
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
	private ComboBox<String> y1RegressionComboBox;

	@FXML
	private ComboBox<String> y2RegressionComboBox;


	private int chartType;
	private int xAxisType = 0;
	private boolean hasSecondAxis = true;
	private MultiAxisChart chart = null;

	@FXML
	public void initialize() {
		chartTypeComboBox.getItems().addAll(new String[] { "Scatter Chart", "Bar Chart", "Line Chart", "Area Chart" });
		chartTypeComboBox.setOnAction(e -> {
			if (chartTypeComboBox.getSelectionModel().getSelectedItem().equals("Bar Chart")) {
				xAxisTypeComboBox.getSelectionModel().select(0);
				xAxisTypeComboBox.setDisable(true);
			} else {
				xAxisTypeComboBox.setDisable(false);
			}
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


		xAxisTypeComboBox.getSelectionModel().select(0);
		chartTypeComboBox.getSelectionModel().select(0);
		updateChart();
	}

	private void updateChart() {

		Axis<?> xAxis = null;
		NumberAxis y1Axis = null;
		NumberAxis y2Axis = null;

		y1Axis = new NumberAxis();
		y1Axis.setLabel("Force (N)");

		y2Axis = new NumberAxis();
		y2Axis.setLabel("Speed");

		y2Axis.setVisible(hasSecondAxis);

		MultiAxisChart.Series series1 = new MultiAxisChart.Series();
		series1.setName("April");

		MultiAxisChart.Series series2 = new MultiAxisChart.Series();
		series2.setName("May");

		if (xAxisType == CATEGORY_AXIS) {
			xAxis = new CategoryAxis();

			((CategoryAxis) xAxis).setCategories(FXCollections.<String>observableArrayList(
					Arrays.asList(new String[] { "Power", "Force", "Agility", "Balance", "Speed" })));

			series1.getData().add(new MultiAxisChart.Data("Power", 4, MultiAxisChart.Y2_AXIS));
			series1.getData().add(new MultiAxisChart.Data("Force", 17, MultiAxisChart.Y2_AXIS));
			series1.getData().add(new MultiAxisChart.Data("Agility", 15, MultiAxisChart.Y2_AXIS));
			series1.getData().add(new MultiAxisChart.Data("Balance", 8, MultiAxisChart.Y2_AXIS));
			series1.getData().add(new MultiAxisChart.Data("Speed", 5, MultiAxisChart.Y2_AXIS));
			
			series2.getData().add(new MultiAxisChart.Data("Power", 18, MultiAxisChart.Y2_AXIS));
			series2.getData().add(new MultiAxisChart.Data("Force", 22, MultiAxisChart.Y2_AXIS));
			series2.getData().add(new MultiAxisChart.Data("Agility", 34, MultiAxisChart.Y2_AXIS));
			series2.getData().add(new MultiAxisChart.Data("Balance", 32, MultiAxisChart.Y2_AXIS));
			series2.getData().add(new MultiAxisChart.Data("Speed", 18, MultiAxisChart.Y2_AXIS));

		} else {
			xAxis = new NumberAxis();

			series1.getData().add(new MultiAxisChart.Data(100, 1889,MultiAxisScatterChart.Y1_AXIS));
			series1.getData().add(new MultiAxisChart.Data(110, 1935,MultiAxisScatterChart.Y1_AXIS));
			series1.getData().add(new MultiAxisChart.Data(120, 2337,MultiAxisScatterChart.Y1_AXIS));
			series1.getData().add(new MultiAxisChart.Data(130, 2196,MultiAxisScatterChart.Y1_AXIS));
			series1.getData().add(new MultiAxisChart.Data(140, 2398,MultiAxisScatterChart.Y1_AXIS));
			series1.getData().add(new MultiAxisChart.Data(150, 2579,MultiAxisScatterChart.Y1_AXIS));
			series1.getData().add(new MultiAxisChart.Data(160, 2601,MultiAxisScatterChart.Y1_AXIS));


		}
		xAxis.setLabel("Load (kg)");

		if (chartType == SCATTER_CHART) {
			chart = new MultiAxisScatterChart(xAxis, y1Axis, y2Axis);
		} else if (chartType == BAR_CHART) {
			chart = new MultiAxisBarChart((CategoryAxis) xAxis, y1Axis, y2Axis);
		} else if (chartType == LINE_CHART) {
			chart = new MultiAxisLineChart(xAxis, y1Axis, y2Axis);
		} else {
			chart = new MultiAxisAreaChart(xAxis, y1Axis, y2Axis);
		}

		chart.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
		chart.setTitle("Force, Power/Load");
		chart.getData().addAll(series1, series2);
		chart.setRegression(MultiAxisChart.Y2_AXIS, MultiAxisChart.LINEAR_REGRESSION);
		chart.setRegressionColor(MultiAxisScatterChart.Y2_AXIS, 0, "#001DFF");
		
		chartPane.getChildren().clear();
		chartPane.setCenter(chart);
	}
}
