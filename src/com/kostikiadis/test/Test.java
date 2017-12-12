package com.kostikiadis.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Test extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primary) throws Exception {

		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/com/kostikiadis/test/TestFrame.fxml"));

		Parent root = loader.load();

		primary.setScene(new Scene(root));
		primary.show();
	}
}