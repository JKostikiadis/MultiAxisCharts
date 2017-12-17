package javafx.scene.chart;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;

public class MultiAxisLineChart extends MultiAxisChart {

	public MultiAxisLineChart(Axis<?> xAxis, NumberAxis y1Axis, NumberAxis y2Axis) {
		super(xAxis, y1Axis, y2Axis);
	}

	public MultiAxisLineChart(int width, int height, Axis<?> xAxis, NumberAxis y1Axis, NumberAxis y2Axis) {
		this(xAxis, y1Axis, y2Axis);
		setPrefSize(width, height);
		drawValues();
	}

	@Override
	public void drawValues() {
		super.drawValues();
		ObservableList<XYChart.Series> data = getData();

		NumberAxis y1Axis = (NumberAxis) getYAxis(MultiAxisChart.LEFT_AXIS);
		NumberAxis y2Axis = (NumberAxis) getYAxis(MultiAxisChart.RIGHT_AXIS);

		int seriesIndex = 0;
		for (XYChart.Series serie : data) {
			ObservableList<XYChart.Data> dataSeries = serie.getData();

			Path linePath = new Path();
			linePath.setStroke(Color.web(DEFAULT_COLORS[seriesIndex % DEFAULT_COLORS.length]));
			linePath.setStrokeWidth(2);
			
			chartValues.add(linePath);

			for (XYChart.Data value : dataSeries) {
				String xValue = value.getXValue().toString();
				Number yValue = (Number) value.getYValue();

				super.updateAxis(xValue,yValue,(int) value.getExtraValue());
				
				double xPosition, yPosition;

				if (getXAxis() instanceof CategoryAxis) {
					if(!((CategoryAxis) getXAxis()).getCategories().contains(xValue)) {
						continue;
					}
					
					xPosition = ((CategoryAxis) getXAxis()).getDisplayPosition(xValue)
							+ ((CategoryAxis) getXAxis()).getLayoutX();
				} else {
					xPosition = ((NumberAxis) getXAxis()).getDisplayPosition(Double.parseDouble(xValue))
							+ ((NumberAxis) getXAxis()).getLayoutX();
				}

				if (((int) value.getExtraValue()) == MultiAxisChart.LEFT_AXIS) {
					yPosition = y1Axis.getDisplayPosition(yValue) + y1Axis.getLayoutY();
				} else {
					yPosition = y2Axis.getDisplayPosition(yValue) + y2Axis.getLayoutY();
				}

				if (linePath.getElements().isEmpty()) {
					linePath.getElements().add(new MoveTo(xPosition, yPosition));
				} else {
					linePath.getElements().add(new LineTo(xPosition, yPosition));
				}

				Circle whole = new Circle(xPosition, yPosition, 5);
				whole.setFill(Color.web(DEFAULT_COLORS[seriesIndex % DEFAULT_COLORS.length]));
				Circle inside = new Circle(xPosition, yPosition, 2);
				inside.setFill(Color.WHITE);

				Shape donutShape = Shape.subtract(whole, inside);
				donutShape.setFill(Color.web(DEFAULT_COLORS[seriesIndex % DEFAULT_COLORS.length]));

				chartValues.addAll(whole, inside);
			}

			seriesIndex++;
		}
		
		plotPane.getChildren().addAll(chartValues);
		super.layout();
	}
}
