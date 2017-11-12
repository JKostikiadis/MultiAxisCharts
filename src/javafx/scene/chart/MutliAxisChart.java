package javafx.scene.chart;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.Axis.TickMark;
import javafx.scene.chart.NumberAxis;
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

public class MutliAxisChart extends BorderPane {

	/*
	 * Axis Instances
	 */
	private NumberAxis xAxis;
	private NumberAxis y1Axis;
	private NumberAxis y2Axis;

	/*
	 * Title properties
	 */
	protected StringProperty titleProperty = new SimpleStringProperty("Title Example");
	protected StringProperty xAxisTitleProperty = new SimpleStringProperty("");
	protected StringProperty y1AxisTitleProperty = new SimpleStringProperty("");
	protected StringProperty y2AxisTitleProperty = new SimpleStringProperty("");

	/*
	 * Chart graphics parts
	 */
	private AnchorPane plotPane;
	private TilePane legendPane = new TilePane();
	private ObservableList<Line> verticalLines;
	private ObservableList<Rectangle> horizontalLines;
	private Line xAxisLine;
	private Line y1AxisLine;
	private Line y2AxisLine;
	private Label chartTitleLabel;

	public MutliAxisChart(NumberAxis xAxis, NumberAxis y1Axis, NumberAxis y2Axis) {
		this.xAxis = xAxis;
		this.y1Axis = y1Axis;
		this.y2Axis = y2Axis;

		setStyle("-fx-background-color : #FFFFFF;");
		legendPane.setAlignment(Pos.CENTER);
		legendPane.setPrefColumns(6);

		initLegendPane();
		initPlotPane();
		setTitle();

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
		return l.prefHeight(-1) + 5;
	}

	private void initPlotPane() {

		plotPane = new AnchorPane() {
			@Override
			public void requestLayout() {
				// do nothing
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
		 * X and Y1 Axis with their lines
		 */
		((Label) xAxis.lookup(".label")).setFont(Font.font("Times New Roman", 24));
		((Label) y1Axis.lookup(".label")).setFont(Font.font("Times New Roman", 24));
		((Label) y2Axis.lookup(".label")).setFont(Font.font("Times New Roman", 24));

		double leftTitleWidth = getAxisLabelWidth(y1Axis);
		double xStart = 61 + leftTitleWidth;
		double yStart = 40;

		xAxis.relocate(xStart, plotPane.getHeight());
		xAxis.setAnimated(false);

		xAxis.prefWidthProperty().bind(plotPane.widthProperty().subtract(xStart * 2));
		xAxis.translateYProperty().bind(plotPane.heightProperty().subtract(yStart));

		xAxisLine = new Line(xStart, 0, 0, 0);
		xAxisLine.setStroke(Color.web("#8B8B8B"));
		xAxisLine.endXProperty().bind(plotPane.widthProperty().subtract(xStart));
		xAxisLine.translateYProperty().bind(xAxis.translateYProperty().add(2));

		y1Axis.setSide(Side.LEFT);
		y1Axis.setAnimated(false);
		y1Axis.relocate(40, 40);
		y1Axis.prefHeightProperty().bind(plotPane.heightProperty().subtract(80));

		y1AxisLine = new Line(xStart, yStart, xStart, 0);
		y1AxisLine.setStroke(Color.web("#757575"));
		y1AxisLine.endYProperty().bind(plotPane.heightProperty().subtract(yStart));

		y2Axis.setSide(Side.RIGHT);
		y2Axis.setAnimated(false);
		y2Axis.relocate(40, 40); // Maybe only set the y translation
		y2Axis.prefHeightProperty().bind(plotPane.heightProperty().subtract(80));
		y2Axis.translateXProperty().bind(xAxisLine.endXProperty().subtract(yStart));

		y2AxisLine = new Line(62, yStart, 62, 0);
		y2AxisLine.setStroke(Color.web("#757575"));
		y2AxisLine.translateXProperty().bind(xAxisLine.endXProperty().subtract(62));
		y2AxisLine.endYProperty().bind(plotPane.heightProperty().subtract(yStart));

		plotPane.getChildren().addAll(xAxis, xAxisLine, y1Axis, y1AxisLine, y2Axis, y2AxisLine);

		/*
		 * Vertical and horizontal Lines. Listeners in order to draw them each time one
		 * of the Axis resize
		 */

		// TODO : set to listen axis value change as well

		xAxis.layoutBoundsProperty().addListener(e -> {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					updateVerticalLines();
				}
			});
		});

		y1Axis.layoutBoundsProperty().addListener(e -> {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					updateHorizontalLines();
				}
			});
		});

		verticalLines = FXCollections.observableArrayList();
		horizontalLines = FXCollections.observableArrayList();

		setCenter(plotPane);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				updateVerticalLines();
				updateHorizontalLines();
			}
		});

	}

	private double getAxisLabelWidth(NumberAxis axis) {
		System.out.println("h = " + this.getLabelHeight((Label) axis.lookup(".label")));

		return 33;
	}

	private synchronized void updateVerticalLines() {

		plotPane.getChildren().removeAll(verticalLines);
		verticalLines.clear();

		ObservableList<TickMark<Number>> xMarkList = xAxis.getTickMarks();

		for (int i = 1; i < xMarkList.size(); i++) {

			double xPos = xMarkList.get(i).getPosition() + xAxis.getLayoutX();

			Line verticalLine = new Line(xPos, 40, xPos, 0);
			verticalLine.setStroke(Color.web("#E4E4E4"));
			verticalLine.endYProperty().bind(plotPane.heightProperty().subtract(40));

			verticalLines.add(verticalLine);
		}

		plotPane.getChildren().addAll(verticalLines);

	}

	private void updateHorizontalLines() {
		plotPane.getChildren().removeAll(horizontalLines);
		horizontalLines.clear();

		ObservableList<TickMark<Number>> y1MarkList = y1Axis.getTickMarks();

		double leftTitleWidth = getAxisLabelWidth(y1Axis);
		double xStart = 61 + leftTitleWidth;
		double height = plotPane.heightProperty().get() / y1MarkList.size();

		for (int i = 1; i < y1MarkList.size(); i++) {
			double yPos = y1MarkList.get(i).getPosition() + y1Axis.getLayoutY();

			Rectangle horizontalLine = new Rectangle();
			horizontalLine.relocate(xStart, yPos);

			horizontalLine.widthProperty().bind(plotPane.widthProperty().subtract(xStart * 2));
			horizontalLine.setHeight(height);

			horizontalLine.setStroke(Color.web("#E4E4E4"));

			if (i % 2 == 0) {
				horizontalLine.setFill(Color.web("#F5F5F5"));
			} else {
				horizontalLine.setFill(Color.web("#EEEEEE"));
			}
			horizontalLine.toBack();

			horizontalLines.add(horizontalLine);
		}

		plotPane.getChildren().addAll(horizontalLines);

		for (Line l : verticalLines) {
			l.toFront();
		}
		y1Axis.toFront();
		xAxis.toFront();
		xAxisLine.toFront();
		y1AxisLine.toFront();
		y2AxisLine.toFront();
	}

	private void initLegendPane() {

		FlowPane legendPosPane = new FlowPane();
		legendPosPane.setPadding(new Insets(5));
		legendPosPane.setAlignment(Pos.CENTER);

		legendPosPane.getChildren().add(legendPane);

		legendPane.setPadding(new Insets(10));

		legendPane.setVgap(5);
		legendPane.setHgap(10);

		legendPane.setStyle("-fx-background-color : #EEEEEE; -fx-background-radius : 10;");

		TilePane.setMargin(legendPane, new Insets(20));

		String DEFAULT_COLORS[] = { "#f3622d", "#fba71b", "#57b757", "#41a9c9", "#4258c9", "#9a42c8", "#c84164",
				"#888888" };
		for (int i = 0; i < DEFAULT_COLORS.length * 2; i++) {
			Label label = new Label("legend " + i);

			Region n = new Region();
			n.setStyle("-fx-min-width: 10.0;\r\n" + "    -fx-max-width: 10.0;\r\n" + "    -fx-min-height: 10.0;\r\n"
					+ "    -fx-max-height: 10.0;\r\n" + "  \r\n" + "    -fx-background-color : "
					+ DEFAULT_COLORS[i % DEFAULT_COLORS.length] + ";\r\n" + "    -fx-background-insets: 0.0, 2.0;\r\n"
					+ "    -fx-background-radius: 5.0px;\r\n" + "    -fx-padding: 5.0px;");

			label.setGraphic(n);
			legendPane.getChildren().add(label);
		}

		if (legendPane.getChildren().isEmpty()) {
			legendPosPane.setVisible(false);
		}
		setBottom(legendPosPane);
	}

	public void setTitle(String title) {
		titleProperty.set(title == null ? "" : title);
		chartTitleLabel.translateXProperty()
				.bind(plotPane.widthProperty().divide(2).subtract(getLabelWidth(this.chartTitleLabel) / 2.0));
	}

	public String getTitle() {
		return titleProperty.get();
	}

	public void setXAxisTitle(String title) {
		xAxisTitleProperty.set(title == null ? "" : title);
	}

	public String getXAxisTitle() {
		return xAxisTitleProperty.get();
	}

	public void setY1AxisTitle(String title) {
		y1AxisTitleProperty.set(title == null ? "" : title);
	}

	public String getY1AxisTitle() {
		return y1AxisTitleProperty.get();
	}

	public void setY2AxisTitle(String title) {
		y2AxisTitleProperty.set(title == null ? "" : title);
	}

	public String getY2AxisTitle() {
		return y2AxisTitleProperty.get();
	}

}
