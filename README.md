# MultiAxisCharts

MultiAxisCharts library is an extension of the JavaFX available charts . All the graphical charts available in the javafx.scene.chart package has very few functionalities and are bound to the two axis system (X-Y) making the need to display multiple data with diferrent Y axis almost impossible. That's where MultiAxisCharts library comes in providing :
  - Two YAxis for displaying multiple data ( X-Y1-Y2 axis)
  - Linear and Polynomial Regression of the data represented
  - In addition to CSS rules, more methods to style almost every element on the chart using code

Available Chart:
  - MultiAxisScatterChart
  - MultiAxisLineChart
  - MultiAxisAreaChart 
  - MultiAxisBarChart
  
### Usage

The charts uses the well knowned Axis (NumberAxis, CategoryAxis) and to add data to the chart you have to add one or more MultiAxisChart.Series<?,?> containing new MultiAxisChart.Data<?,?> data. Example :

```java
MultiAxisScatterChart chart = new MultiAxisScatterChart(xAxis, yAxis, y2Axis);
chart.setTitle("Just an example");
chart.setPrefSize(500, 500);

MultiAxisChart.Series<Number, Number> series1 = new MultiAxisChart.Series<Number, Number>();
series1.setName("April");

MultiAxisChart.Series<Number, Number> series2 = new MultiAxisChart.Series<Number, Number>();
series2.setName("May");

series1.getData().add(new MultiAxisChart.Data<Number, Number>(2, 4, MultiAxisChart.Y1_AXIS));
series1.getData().add(new MultiAxisChart.Data<Number, Number>(7, 10, MultiAxisChart.Y1_AXIS));
series1.getData().add(new MultiAxisChart.Data<Number, Number>(12, 15, MultiAxisChart.Y1_AXIS));
series1.getData().add(new MultiAxisChart.Data<Number, Number>(16, 8, MultiAxisChart.Y1_AXIS));
series1.getData().add(new MultiAxisChart.Data<Number, Number>(21, 5, MultiAxisChart.Y1_AXIS));

series2.getData().add(new MultiAxisChart.Data<Number, Number>(2, 200, MultiAxisChart.Y2_AXIS));
series2.getData().add(new MultiAxisChart.Data<Number, Number>(7, 300, MultiAxisChart.Y2_AXIS));
series2.getData().add(new MultiAxisChart.Data<Number, Number>(12, 400, MultiAxisChart.Y2_AXIS));
series2.getData().add(new MultiAxisChart.Data<Number, Number>(16, 500, MultiAxisChart.Y2_AXIS));
series2.getData().add(new MultiAxisChart.Data<Number, Number>(21, 600, MultiAxisChart.Y2_AXIS));

chart.getData().addAll(series1, series2);
```

![MultiAxisScatterChart creation ](./preview/MultiAxisScatterChart.png)








### Installation







### Todos

 - Refactor the regression process ( linear & polynomial )
 - Add more functionalities

## Licence
-------
[GNU GENERAL PUBLIC LICENSE](LICENSE)



