package javafx.scene.chart;

import java.text.DecimalFormat;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.Axis.TickMark;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public abstract class MultiAxisChart extends BorderPane {

	public static final int LEFT_AXIS = 0;
	public static final int RIGHT_AXIS = 1;

	/*
	 * 
	 * Axis Instances
	 */
	private Axis<?> xAxis;
	private NumberAxis y1Axis;
	private NumberAxis y2Axis;

	/*
	 * Title properties
	 */
	protected StringProperty titleProperty = new SimpleStringProperty("Title Example");

	/*
	 * Chart graphics parts
	 */
	protected AnchorPane plotPane;
	private TilePane legendPane;
	private ObservableList<Line> verticalLines;
	private ObservableList<Rectangle> horizontalLines;
	private Line xAxisLine;
	private Line y1AxisLine;
	private Line y2AxisLine;
	private Label chartTitleLabel;
	private double xStart;
	private int yStart;

	protected ObservableList<Node> chartValues = FXCollections.observableArrayList();

	private ObservableList<XYChart.Series> data = FXCollections.observableArrayList();

	protected final String DEFAULT_COLORS[] = { "#f3622d", "#fba71b", "#57b757", "#41a9c9", "#4258c9", "#9a42c8",
			"#c84164", "#888888" };
	private boolean hasBackgroundGrid;

	public MultiAxisChart(Axis<?> xAxis, NumberAxis y1Axis, NumberAxis y2Axis) {

		if (xAxis instanceof CategoryAxis) {

			ObservableList<String> categories = FXCollections
					.observableArrayList(((CategoryAxis) xAxis).getCategories());

			CategoryAxis axis = new CategoryAxis();
			axis.tickLengthProperty().set(10);
			axis.setTickLabelFont(xAxis.getTickLabelFont());
			axis.setTickLabelGap(xAxis.getTickLabelGap());

			((Label) axis.lookup(".label")).setFont(((Label) xAxis.lookup(".label")).getFont());

			axis.tickLabelRotationProperty().addListener(e -> {
				axis.setTickLabelRotation(0);
			});
			axis.setLabel(xAxis.getLabel());
			axis.invalidateRange(categories);

			this.xAxis = axis;

		} else {
			this.xAxis = xAxis;
		}

		this.y1Axis = y1Axis;
		this.y2Axis = y2Axis;

		setStyle("-fx-background-color : #FFFFFF;");

		initLegendPane();
		initPlotPane();
		setTitle();

	}

	protected void drawValues() {
		plotPane.getChildren().removeAll(chartValues);
		chartValues.clear();
		updateAxis();
	}

	private void setTitle() {
		chartTitleLabel = new Label();
		chartTitleLabel.textProperty().bind(titleProperty);
		chartTitleLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20));

		chartTitleLabel.visibleProperty().bind(titleProperty.isEmpty().not());

		chartTitleLabel.translateXProperty()
				.bind(plotPane.widthProperty().divide(2).subtract(getLabelWidth(chartTitleLabel) / 2));
		chartTitleLabel.setTranslateY(5);

		plotPane.getChildren().addAll(chartTitleLabel);

	}

	private double getLabelWidth(Label label) {
		HBox h = new HBox();
		Label l = new Label(label.getText());
		l.setFont(label.getFont());
		h.getChildren().add(l);
		Scene s = new Scene(h);
		l.impl_processCSS(true);
		return l.prefWidth(-1);
	}

	private double getLabelHeight(Label label) {
		HBox h = new HBox();
		Label l = new Label(label.getText());
		l.setFont(label.getFont());
		h.getChildren().add(l);
		Scene s = new Scene(h);
		l.impl_processCSS(true);
		return l.prefHeight(-1);
	}

	private void initPlotPane() {

		plotPane = new AnchorPane() {
			@Override
			public void requestLayout() {
				super.requestLayout();
			}

			@Override
			protected double computeMinWidth(double height) {
				return 300;
			}

			@Override
			protected double computeMinHeight(double width) {
				// TODO Auto-generated method stub
				return 200;
			}
		};

		/*
		 * T and Y1 Axis with their lines
		 */

		xAxis.tickLabelFontProperty().set(Font.font("Times New Roman", 18));
		y1Axis.tickLabelFontProperty().set(Font.font("Times New Roman", 18));
		if (y2Axis != null)
			y2Axis.tickLabelFontProperty().set(Font.font("Times New Roman", 18));

		((Label) xAxis.lookup(".label")).setFont(Font.font("Times New Roman", 22));
		((Label) y1Axis.lookup(".label")).setFont(Font.font("Times New Roman", 22));

		if (y2Axis != null)
			((Label) y2Axis.lookup(".label")).setFont(Font.font("Times New Roman", 22));

		// we need the height cause the label is vertically oriented
		double y1AxisTitleHeight = getLabelHeight((Label) y1Axis.lookup(".label"));

		double bottomTitleHeight = getLabelHeight((Label) xAxis.lookup(".label"));

		Label tmpLabel = new Label(getAxisTickMaxLengthValue(y1Axis));
		tmpLabel.setFont(y1Axis.getTickLabelFont());
		double y1AxisTickWidth = getLabelWidth(tmpLabel);

		xStart = 55 + y1AxisTitleHeight + y1AxisTickWidth;
		yStart = 40;

		xAxis.relocate(xStart, plotPane.getHeight());
		xAxis.setAnimated(false);

		xAxis.prefWidthProperty().bind(plotPane.widthProperty().subtract(xStart * 2));
		xAxis.translateYProperty().bind(plotPane.heightProperty().subtract(yStart + bottomTitleHeight));

		xAxisLine = new Line(xStart, 0, 0, 0);
		xAxisLine.setStroke(Color.web("#8B8B8B"));
		xAxisLine.endXProperty().bind(plotPane.widthProperty().subtract(xStart));
		xAxisLine.translateYProperty().bind(xAxis.translateYProperty().add(2));

		y1Axis.setSide(Side.LEFT);
		y1Axis.setAnimated(false);
		y1Axis.relocate(40, 40);
		y1Axis.prefHeightProperty().bind(plotPane.heightProperty().subtract(80 + bottomTitleHeight));

		y1AxisLine = new Line(xStart, yStart, xStart, 0);
		y1AxisLine.setStroke(Color.web("#757575"));
		y1AxisLine.endYProperty().bind(plotPane.heightProperty().subtract(yStart + bottomTitleHeight));

		if (y2Axis != null) {
			y2Axis.setSide(Side.RIGHT);
			y2Axis.setAnimated(false);
			y2Axis.relocate(40, 40);
			y2Axis.prefHeightProperty().bind(plotPane.heightProperty().subtract(80 + bottomTitleHeight));
			y2Axis.translateXProperty().bind(xAxisLine.endXProperty().subtract(yStart));

			y2AxisLine = new Line(62, yStart, 62, 0);
			y2AxisLine.setStroke(Color.web("#757575"));
			y2AxisLine.translateXProperty().bind(xAxisLine.endXProperty().subtract(62));
			y2AxisLine.endYProperty().bind(plotPane.heightProperty().subtract(yStart + bottomTitleHeight));

			plotPane.getChildren().addAll(y2Axis, y2AxisLine);
		}

		plotPane.getChildren().addAll(xAxis, xAxisLine, y1Axis, y1AxisLine);

		verticalLines = FXCollections.observableArrayList();
		horizontalLines = FXCollections.observableArrayList();

		initBackgroundGrid();

		setCenter(plotPane);

		// Platform.runLater(new Runnable() {
		// @Override
		// public void run() {
		// updateVerticalLines();
		// updateHorizontalLines();
		// }
		// });

	}

	private void initBackgroundGrid() {
		/*
		 * Vertical and horizontal Lines. Listeners in order to draw them each time one
		 * of the Axis resize
		 */

		// TODO : set to listen axis value change as well

		y1Axis.layoutBoundsProperty().addListener(e -> {
			plotPane.getChildren().removeAll(horizontalLines);
			horizontalLines.clear();

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					updateHorizontalLines();
					drawValues();
				}
			});

		});

		xAxis.layoutBoundsProperty().addListener(e -> {
			plotPane.getChildren().removeAll(verticalLines);
			verticalLines.clear();

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					updateVerticalLines();
					drawValues();
				}
			});
		});

	}

	private String getAxisTickMaxLengthValue(NumberAxis axis) {

		String upperBound;
		String lowerBound;

		double upperValue = axis.getUpperBound();
		double lowerValue = axis.getLowerBound();

		DecimalFormat realFormatter = new DecimalFormat("#,###.00");
		DecimalFormat integerFormatter = new DecimalFormat("#,###");

		if (upperValue == ((int) upperValue)) {
			upperBound = integerFormatter.format((int) upperValue);
		} else {
			upperBound = realFormatter.format(upperValue);
		}

		if (lowerValue == ((int) lowerValue)) {
			lowerBound = integerFormatter.format((int) lowerValue);
		} else {
			lowerBound = realFormatter.format(lowerValue);
		}

		return upperBound.length() > lowerBound.length() ? upperBound : lowerBound;
	}

	private synchronized void updateVerticalLines() {

		if (!hasBackgroundGrid)
			return;

		double bottomTitleHeight = getLabelHeight((Label) xAxis.lookup(".label"));

		ObservableList<?> xMarkList = xAxis.getTickMarks();

		for (int i = 0; i < xMarkList.size(); i++) {

			double xPos = ((Axis.TickMark<?>) xMarkList.get(i)).getPosition() + xAxis.getLayoutX();

			Line verticalLine = new Line(xPos, 40, xPos, 0);
			verticalLine.setStrokeWidth(1);
			verticalLine.setStroke(Color.web("#E4E4E4"));
			verticalLine.endYProperty().bind(plotPane.heightProperty().subtract(40 + bottomTitleHeight));

			verticalLines.add(verticalLine);
		}

		for (Line l : verticalLines) {
			if (!verticalLines.contains(l)) {
				plotPane.getChildren().add(l);
			} else {
				plotPane.getChildren().remove(l);
				plotPane.getChildren().add(l);
			}
		}

	}

	private void updateHorizontalLines() {
		if (!hasBackgroundGrid)
			return;

		ObservableList<TickMark<Number>> yMarkList = y1Axis.getTickMarks();

		if (yMarkList.size() < 2) {
			return;
		}

		double height = yMarkList.get(0).getPosition() - yMarkList.get(1).getPosition();

		for (int i = 1; i < yMarkList.size(); i++) {

			double yPos = yMarkList.get(i).getPosition() + y1Axis.getLayoutX();

			Rectangle rec = new Rectangle();
			rec.relocate(xStart, yPos);

			rec.setStroke(Color.web("#E4E4E4"));

			if (i % 2 == 0) {
				rec.setFill(Color.web("#F5F5F5"));
			} else {
				rec.setFill(Color.web("#EEEEEE"));
			}

			rec.widthProperty().bind(xAxis.widthProperty());
			rec.setHeight(height);

			horizontalLines.add(rec);
		}

		for (Rectangle rec : horizontalLines) {
			if (!horizontalLines.contains(rec)) {
				plotPane.getChildren().add(rec);
			} else {
				plotPane.getChildren().remove(rec);
				plotPane.getChildren().add(rec);
			}
		}

		y1Axis.toFront();
		xAxis.toFront();
		xAxisLine.toFront();
		y1AxisLine.toFront();

		if (y2Axis != null) {
			y2Axis.toFront();
			y2AxisLine.toFront();
		}

		for (Line l : verticalLines) {
			l.toFront();
		}

	}

	private void initLegendPane() {

		legendPane = new TilePane();
		legendPane.setAlignment(Pos.CENTER);
		legendPane.setPadding(new Insets(5, 2, 5, 2));
		legendPane.setPrefColumns(5);

		legendPane.setVgap(5);
		legendPane.setHgap(10);
		legendPane.setStyle(
				"-fx-background-color : #EEEEEE; -fx-background-radius : 5; -fx-border-radius :5 ;  -fx-border-color: #CCCCCC");

		FlowPane legendPosPane = new FlowPane();
		legendPosPane.setPadding(new Insets(5));
		legendPosPane.setAlignment(Pos.CENTER);

		legendPosPane.getChildren().add(legendPane);

		// TilePane.setMargin(legendPane, new Insets(20));

		data.addListener(new ListChangeListener<Series>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Series> change) {
				while (change.next()) {
					updateLegend();
				}
			}
		});

		setBottom(legendPosPane);
	}

	protected void addLegend(String legendLabel) {

		int index = 0;
		for (XYChart.Series serie : data) {
			if (legendLabel.equalsIgnoreCase(serie.getName())) {
				break;
			}
			index++;
		}

		Label label = new Label(legendLabel);
		label.setPadding(new Insets(0, 10, 0, 10));

		Region n = new Region();
		n.setPrefSize(14, 14);
		n.setStyle("-fx-background-color : " + DEFAULT_COLORS[index % DEFAULT_COLORS.length] + ";"
				+ "-fx-background-insets: 0.0, 2.0;" + "-fx-background-radius: 10.0px;" + "-fx-padding: 5.0px;");

		label.setGraphic(n);
		legendPane.getChildren().add(label);

	}

	public void setTitle(String title) {
		titleProperty.set(title == null ? "" : title);
		chartTitleLabel.translateXProperty()
				.bind(plotPane.widthProperty().divide(2).subtract(getLabelWidth(this.chartTitleLabel) / 2.0));
	}

	public String getTitle() {
		return titleProperty.get();
	}

	public Axis<?> getXAxis() {
		return xAxis;
	}

	public Axis<?> getYAxis(int axisNumber) {
		if (axisNumber == LEFT_AXIS) {
			return y1Axis;
		} else {
			return y2Axis;
		}
	}

	/**
	 * This is called whenever a series is added or removed and the legend needs to
	 * be updated
	 */
	protected void updateLegend() {
		legendPane.getChildren().clear();
		double width = -1;
		if (getData() != null) {
			for (int seriesIndex = 0; seriesIndex < getData().size(); seriesIndex++) {
				Series series = getData().get(seriesIndex);
				addLegend(series.getName());
				width += 80; // FIXME : need to find the correct width size and apply it
			}
		}
		legendPane.requestLayout();
		legendPane.setVisible(!getData().isEmpty());
		legendPane.setPrefWidth(width);

	}

	public ObservableList<XYChart.Series> getData() {
		return data;
	}

	public void setData(ObservableList<XYChart.Series> data) {
		this.data = data;
		updateAxis();
	}

	public void setBackgroundGrid(boolean hasBackgroundGrid) {
		this.hasBackgroundGrid = hasBackgroundGrid;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateAxis() {
		ObservableList<XYChart.Series> data = getData();

		NumberAxis y1Axis = (NumberAxis) getYAxis(MultiAxisChart.LEFT_AXIS);
		NumberAxis y2Axis = (NumberAxis) getYAxis(MultiAxisChart.RIGHT_AXIS);

		double xMaxValue = Double.MIN_VALUE;
		double xMinValue = Double.MAX_VALUE;

		if (xAxis instanceof NumberAxis) {
			xMaxValue = ((NumberAxis) xAxis).getUpperBound();
			xMinValue = ((NumberAxis) xAxis).getLowerBound();
		}

		double y1MaxValue = y1Axis.getUpperBound();
		double y1MinValue = y1Axis.getLowerBound();

		double y2MaxValue = Double.MIN_VALUE;
		double y2MinValue = Double.MAX_VALUE;

		if (y2Axis != null) {
			y2MaxValue = y2Axis.getUpperBound();
			y2MinValue = y2Axis.getLowerBound();
		}

		for (XYChart.Series serie : data) {
			ObservableList<XYChart.Data> dataSeries = serie.getData();

			for (XYChart.Data value : dataSeries) {
				String xValue = value.getXValue().toString();
				Number yValue = (Number) value.getYValue();

				if (xAxis instanceof NumberAxis) {
					if (xMaxValue < Double.parseDouble(xValue)) {
						xMaxValue = Double.parseDouble(xValue);
					} else if (xMinValue > Double.parseDouble(xValue)) {
						xMinValue = Double.parseDouble(xValue);
					}
				}

				if (((int) value.getExtraValue()) == LEFT_AXIS) {
					if (y1MaxValue < Double.parseDouble(xValue)) {
						y1MaxValue = Double.parseDouble(xValue);
					} else if (y1MinValue > Double.parseDouble(xValue)) {
						y1MinValue = Double.parseDouble(xValue);
					}
				} else {
					if (y2MaxValue < Double.parseDouble(xValue)) {
						y2MaxValue = Double.parseDouble(xValue);
					} else if (y2MinValue > Double.parseDouble(xValue)) {
						y2MinValue = Double.parseDouble(xValue);
					}
				}
			}
		}

		if (xAxis instanceof NumberAxis) {
			NumberAxis xAxis = ((NumberAxis) this.xAxis);
			xAxis.setUpperBound(xMaxValue);
			xAxis.setLowerBound(xMinValue);
		}

		y1Axis.setUpperBound(y1MaxValue);
		y1Axis.setLowerBound(y1MinValue);

		if (y2Axis != null) {
			y2Axis.setUpperBound(y2MaxValue);
			y2Axis.setLowerBound(y2MinValue);
		}

		layout();
		requestFocus();
	}

}