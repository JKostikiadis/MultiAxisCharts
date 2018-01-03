# MultiAxisCharts

MultiAxisCharts library is an extension of the JavaFX available charts . All the graphical charts available in the javafx.scene.chart package has very few functionalities and are bound to the two axis system (X-Y) making the need to display multiple data on different Y axis almost impossible. That's where MultiAxisCharts library comes in, providing :
  - Two Y-Axis for displaying multiple data ( X-Y1-Y2 axis)
  - The Linear (And polynomial soon) regression of the data represented

Available Chart:
  - MultiAxisScatterChart
  - MultiAxisLineChart
  - MultiAxisAreaChart 
  - MultiAxisBarChart


### Usage

The charts uses the well known Axis (NumberAxis, CategoryAxis) and to add data to the chart you have to add one or more MultiAxisChart.Series<?,?> containing new MultiAxisChart.Data<?,?> data. Example :

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

![MultiAxisScatterChart ](./preview/ScatterChart.png)

In order to show the Linear regression for each y axis you have to call :

```java
chart.setRegression(MultiAxisChart.Y1_AXIS, MultiAxisChart.LINEAR_REGRESSION);
chart.setRegression(MultiAxisChart.Y2_AXIS, MultiAxisChart.LINEAR_REGRESSION);
```

Some hints :
- The extra value of the MultiAxisChart.Data defines the Y Axis in which the data will be displayed. Trying to add data to the Y2_AXIS while the y2Axis is null will throw a NullPointerException
- MultiAxisBarChart can only support CategoryAxis for X Axis.
- The chart values are not animated but the Axis are.
- In order to change Colors, fonts etc you can do that using CSS using the same CSS rules as javafx.scene.chart
- In order to change the regression lines color you can do that by calling :

```java
//  arguments : AxisType, SeriesIndex , Color.Web as String
chart.setRegressionColor(MultiAxisChart.Y2_AXIS, 0, "#FBA71B");
```

### Installation
 
You can clone and build the entire project to create a jar library by yourself or you can download the Pre-Build Jar file and load it to your build path for your projects.

#### Pre-Build Jar 
[MultiAxisScatterChart.jar V1.0](https://github.com/JKostikiadis/MultiAxisCharts/raw/master/build/MultiAxisCharts.jar)


### Todos

 - Refactoring the regression process ( linear ) if possible
 - Add polynomial regression 
 - Separate the series into two legends in order to show which one is on each axis
 - Consider adding more complex charts (ex. combination of BarChart and LineChart )
 - Add more functionalities

License
----

[GNU GENERAL PUBLIC LICENSE](LICENSE)



