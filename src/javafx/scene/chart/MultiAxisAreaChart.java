package javafx.scene.chart;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.effect.Light.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class MultiAxisAreaChart extends MultiAxisChart {

	public MultiAxisAreaChart(Axis<?> xAxis, NumberAxis y1Axis, NumberAxis y2Axis) {
		super(xAxis, y1Axis, y2Axis);
	}

	public MultiAxisAreaChart(int width, int height, Axis<?> xAxis, NumberAxis y1Axis, NumberAxis y2Axis) {
		this(xAxis, y1Axis, y2Axis);
		setPrefSize(width, height);
	}

	@Override
	public void drawValues() {
		super.drawValues();
		
		ObservableList<XYChart.Series> data = getData();

		NumberAxis y1Axis = (NumberAxis) getYAxis(MultiAxisChart.LEFT_AXIS);
		NumberAxis y2Axis = (NumberAxis) getYAxis(MultiAxisChart.RIGHT_AXIS);

		int seriesIndex = 0;
		double xPosition = 0, yPosition;
		
		for (XYChart.Series serie : data) {
			ObservableList<XYChart.Data> dataSeries = serie.getData();

			Path linePath = new Path();
			linePath.setStroke(Color.web(DEFAULT_COLORS[seriesIndex % DEFAULT_COLORS.length]));
			linePath.setStrokeWidth(2);
			
			chartValues.add(linePath);
			
			Polygon area = new Polygon(); 
			area.setFill(Color.web(DEFAULT_COLORS[seriesIndex % DEFAULT_COLORS.length]+"33"));

			ArrayList<Point> allPoints = new ArrayList<>();
			chartValues.add(area);

			for (XYChart.Data value : dataSeries) {
				String xValue = value.getXValue().toString();
				Number yValue = (Number) value.getYValue();

				Color color = Color.web(DEFAULT_COLORS[seriesIndex % DEFAULT_COLORS.length]);;

				if (getXAxis() instanceof CategoryAxis) {
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
						
				if(allPoints.isEmpty()) {
					double y = getXAxis().getLayoutY() + getXAxis().getTranslateY();
					
					linePath.getElements().add(new MoveTo(xPosition, yPosition));
					allPoints.add(new Point(xPosition, y, 0, color));
				}else {
					linePath.getElements().add(new LineTo(xPosition, yPosition));
				}

				allPoints.add(new Point(xPosition, yPosition,0, color));

				Circle whole = new Circle(xPosition, yPosition, 5);
				whole.setFill(color);
				Circle inside = new Circle(xPosition, yPosition, 2);
				inside.setFill(Color.WHITE);

				chartValues.addAll(whole, inside);
			}
			
			Double polygonPoints [] = new Double[allPoints.size()*2+2];
			int index = 0;
			for(int i = 0 ; i < allPoints.size() ; i++) {
				polygonPoints[index++] = allPoints.get(i).getX();
				polygonPoints[index++] = allPoints.get(i).getY();		
			}
			
			// Add the the right down corner of the area
			double x = xPosition;
			double y = getXAxis().getLayoutY() + getXAxis().getTranslateY();
			
			polygonPoints[index++] = x;
			polygonPoints[index++] = y;	
			
			area.getPoints().addAll(polygonPoints);

			seriesIndex++;
		}

		plotPane.getChildren().addAll(chartValues);
	}
}
