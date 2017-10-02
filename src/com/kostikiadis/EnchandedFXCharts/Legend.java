package com.kostikiadis.EnchandedFXCharts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart.Series;

public class Legend {

	private ObservableList<Series> allSeries;

	public Legend() {
		allSeries = FXCollections.observableArrayList();
		
	}

}
