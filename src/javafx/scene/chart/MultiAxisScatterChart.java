package javafx.scene.chart;

import java.util.ArrayList;
import java.util.Iterator;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.charts.Legend.LegendItem;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.util.Duration;

@SuppressWarnings("restriction")
public class MultiAxisScatterChart<X, Y> extends MultiAxisChart<X, Y> {

	// -------------- PRIVATE FIELDS ------------------------------------------
	private Legend legend = new Legend();

	// -------------- CONSTRUCTORS ----------------------------------------------

	/**
	 * Construct a new ScatterChart with the given axis and data.
	 *
	 * @param xAxis
	 *            The x axis to use
	 * @param yAxis
	 *            The y axis to use
	 */

	public MultiAxisScatterChart(Axis<X> xAxis, Axis<Y> y1Axis, Axis<Y> y2Axis) {
		this(xAxis, y1Axis, y2Axis, FXCollections.<Series<X, Y>>observableArrayList());
	}

	/**
	 * Construct a new ScatterChart with the given axis and data.
	 *
	 * @param xAxis
	 *            The x axis to use
	 * @param yAxis
	 *            The y axis to use
	 * @param data
	 *            The data to use, this is the actual list used so any changes to it
	 *            will be reflected in the chart
	 */
	public MultiAxisScatterChart(Axis<X> xAxis, Axis<Y> y1Axis, Axis<Y> y2Axis, ObservableList<Series<X, Y>> data) {
		super(xAxis, y1Axis, y2Axis);
		setLegend(legend);
		setData(data);
	}

	// -------------- METHODS ----------------------------------------------


	/** @inheritDoc */
	@Override
	protected void dataItemAdded(MultiAxisChart.Series<X, Y> series, int itemIndex, Data<X, Y> item) {
		Node symbol = item.getNode();
		// check if symbol has already been created
		if (symbol == null) {
			symbol = new StackPane();
			symbol.setAccessibleRole(AccessibleRole.TEXT);
			symbol.setAccessibleRoleDescription("Point");
			symbol.focusTraversableProperty().bind(Platform.accessibilityActiveProperty());
			item.setNode(symbol);
		}
		// set symbol styles
		symbol.getStyleClass().setAll("chart-symbol", "series" + getData().indexOf(series), "data" + itemIndex,
				series.defaultColorStyleClass);
		// add and fade in new symbol if animated
		if (shouldAnimate()) {
			symbol.setOpacity(0);
			getPlotChildren().add(symbol);
			FadeTransition ft = new FadeTransition(Duration.millis(500), symbol);
			ft.setToValue(1);
			ft.play();
		} else {
			getPlotChildren().add(symbol);
		}

	}

	/** @inheritDoc */
	@Override
	protected void dataItemRemoved(final Data<X, Y> item, final MultiAxisChart.Series<X, Y> series) {
		final Node symbol = item.getNode();

		if (symbol != null) {
			symbol.focusTraversableProperty().unbind();
		}

		if (shouldAnimate()) {
			// fade out old symbol
			FadeTransition ft = new FadeTransition(Duration.millis(500), symbol);
			ft.setToValue(0);
			ft.setOnFinished(actionEvent -> {
				getPlotChildren().remove(symbol);
				removeDataItemFromDisplay(series, item);
				symbol.setOpacity(1.0);
			});
			ft.play();
		} else {
			getPlotChildren().remove(symbol);
			removeDataItemFromDisplay(series, item);
		}
	}

	/** @inheritDoc */
	@Override
	protected void dataItemChanged(Data<X, Y> item) {
	}

	/** @inheritDoc */
	@Override
	protected void seriesAdded(MultiAxisChart.Series<X, Y> series, int seriesIndex) {
		// handle any data already in series
		for (int j = 0; j < series.getData().size(); j++) {
			dataItemAdded(series, j, series.getData().get(j));
		}
	}

	/** @inheritDoc */
	@Override
	protected void seriesRemoved(final MultiAxisChart.Series<X, Y> series) {
		// remove all symbol nodes
		if (shouldAnimate()) {
			ParallelTransition pt = new ParallelTransition();
			pt.setOnFinished(event -> {
				removeSeriesFromDisplay(series);
			});
			for (final Data<X, Y> d : series.getData()) {
				final Node symbol = d.getNode();
				// fade out old symbol
				FadeTransition ft = new FadeTransition(Duration.millis(500), symbol);
				ft.setToValue(0);
				ft.setOnFinished(actionEvent -> {
					getPlotChildren().remove(symbol);
					symbol.setOpacity(1.0);
				});
				pt.getChildren().add(ft);
			}
			pt.play();
		} else {
			for (final Data<X, Y> d : series.getData()) {
				final Node symbol = d.getNode();
				getPlotChildren().remove(symbol);
			}
			removeSeriesFromDisplay(series);
		}
	}

	/** @inheritDoc */
	@Override
	protected void layoutPlotChildren() {
		// update symbol positions
		for (int seriesIndex = 0; seriesIndex < getDataSize(); seriesIndex++) {
			MultiAxisChart.Series<X, Y> series = getData().get(seriesIndex);
			for (Iterator<Data<X, Y>> it = getDisplayedDataIterator(series); it.hasNext();) {
				Data<X, Y> item = it.next();
				double x = getXAxis().getDisplayPosition(item.getCurrentX());
				double y = -1;
				if (item.getExtraValue() == null || (int) item.getExtraValue() == MultiAxisChart.Y1_AXIS) {
					y = getY1Axis().getDisplayPosition(item.getCurrentY());
				} else {
					if (getY2Axis() != null) {
						if (getY2Axis().isVisible()) {
							y = getY2Axis().getDisplayPosition(item.getCurrentY());
						} else {
							continue;
						}
					} else {
						throw new NullPointerException(
								"Data found with Y2_Axis as Y axis while Y2 axis is not defined.");
					}
				}

				if (Double.isNaN(x) || Double.isNaN(y)) {
					continue;
				}
				Node symbol = item.getNode();
				if (symbol != null) {
					final double w = symbol.prefWidth(-1);
					final double h = symbol.prefHeight(-1);
					symbol.resizeRelocate(x - (w / 2), y - (h / 2), w, h);
				}
			}
		}

	

	}

	/**
	 * This is called whenever a series is added or removed and the legend needs to
	 * be updated
	 */
	@Override
	protected void updateLegend() {
		legend.getItems().clear();
		if (getData() != null) {
			for (int seriesIndex = 0; seriesIndex < getData().size(); seriesIndex++) {
				MultiAxisChart.Series<X, Y> series = getData().get(seriesIndex);
				LegendItem legenditem = new LegendItem(series.getName());
				if (!series.getData().isEmpty() && series.getData().get(0).getNode() != null) {
					legenditem.getSymbol().getStyleClass().addAll(series.getData().get(0).getNode().getStyleClass());
				}
				legend.getItems().add(legenditem);
			}
		}
		if (legend.getItems().size() > 0) {
			if (getLegend() == null) {
				setLegend(legend);
			}
		} else {
			setLegend(null);
		}
	}
}
