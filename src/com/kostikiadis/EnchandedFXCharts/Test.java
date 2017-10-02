package com.kostikiadis.EnchandedFXCharts;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test extends Application {

	@Override
	public void start(Stage stage) throws Exception {

		VBox box = new VBox();
		
		
 
		stage.setScene(new Scene(box));
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
