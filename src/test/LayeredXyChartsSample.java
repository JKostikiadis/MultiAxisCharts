package test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.MultiAxisAreaChart;
import javafx.scene.chart.MultiAxisChart;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;

/**
 * Demonstrates how to draw layers of MultiAxisCharts.
 * https://forums.oracle.com/forums/thread.jspa?threadID=2435995 "Using
 * StackPane to layer more different type charts"
 */
public class LayeredXyChartsSample extends Application {

	private MultiAxisChart.Series<String, Number> barSeries;
	private MultiAxisChart.Series<String, Number> lineSeries;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		initSeries();

		// Close the application when the window is closed
		stage.setOnCloseRequest(t -> {
			Platform.exit();
			System.exit(0);
		});

		CategoryAxis xAxis = new CategoryAxis();

		MultiAxisAreaChart chart = new MultiAxisAreaChart(xAxis, createYaxis(), createYaxis());

		chart.getData().addAll(barSeries, lineSeries);

		stage.setScene(new Scene(chart));
		stage.show();

		updateSeries();
	}

	@SuppressWarnings("unchecked")
	private void initSeries() {
		barSeries = new MultiAxisChart.Series(FXCollections.observableArrayList(new MultiAxisChart.Data("Jan", 2),
				new MultiAxisChart.Data("Feb", 10), new MultiAxisChart.Data("Mar", 8),
				new MultiAxisChart.Data("Apr", 4), new MultiAxisChart.Data("May", 7), new MultiAxisChart.Data("Jun", 5),
				new MultiAxisChart.Data("Jul", 4), new MultiAxisChart.Data("Aug", 8),
				new MultiAxisChart.Data("Sep", 16.5), new MultiAxisChart.Data("Oct", 13.9),
				new MultiAxisChart.Data("Nov", 17), new MultiAxisChart.Data("Dec", 10)));

		lineSeries = new MultiAxisChart.Series(FXCollections.observableArrayList(new MultiAxisChart.Data("Jan", 1),
				new MultiAxisChart.Data("Feb", 2), new MultiAxisChart.Data("Mar", 1.5),
				new MultiAxisChart.Data("Apr", 3), new MultiAxisChart.Data("May", 2.5),
				new MultiAxisChart.Data("Jun", 5), new MultiAxisChart.Data("Jul", 4), new MultiAxisChart.Data("Aug", 8),
				new MultiAxisChart.Data("Sep", 6.5), new MultiAxisChart.Data("Oct", 13),
				new MultiAxisChart.Data("Nov", 10), new MultiAxisChart.Data("Dec", 20)));

	}

	private void updateSeries() {
		new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				final int index = i;
				final double value = 20 * Math.random();
				Platform.runLater(() -> {
					barSeries.getData().remove(0);
					barSeries.getData().add(new MultiAxisChart.Data<>(String.valueOf(index), value));

					lineSeries.getData().remove(0);
					lineSeries.getData().add(new MultiAxisChart.Data<>(String.valueOf(index), value * 2));
				});
			}

			Platform.exit();
			System.exit(0);
		}).start();
	}

	private NumberAxis createYaxis() {
		final NumberAxis axis = new NumberAxis();

		axis.setAutoRanging(true);
		axis.setPrefWidth(35);
		axis.setMinorTickCount(10);

		axis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(axis) {
			@Override
			public String toString(Number object) {
				return String.format("%7.2f", object.floatValue());
			}
		});

		return axis;
	}

}
