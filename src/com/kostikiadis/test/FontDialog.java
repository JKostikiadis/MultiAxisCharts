package com.kostikiadis.test;

import javafx.beans.binding.ObjectBinding;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class FontDialog extends Dialog<Font> {

	private ListView<String> fontFamilyList;
	private ListView<String> styleList;
	private ListView<Integer> sizeList;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FontDialog(Font f) {

		setTitle("Font Selector");
		setHeaderText("Select font properties.");

		getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		Label sampleLabel = new Label("Sample Text");

		VBox rootPane = new VBox(10);
		HBox fontSettingPane = new HBox(20);
		FlowPane samplePane = new FlowPane();
		samplePane.setAlignment(Pos.CENTER);
		samplePane.getChildren().add(sampleLabel);

		rootPane.getChildren().addAll(fontSettingPane, samplePane);

		getDialogPane().setContent(rootPane);

		VBox fontFamilyPane = new VBox();
		fontFamilyList = new ListView<>();
		fontFamilyList.getItems().addAll(javafx.scene.text.Font.getFamilies());
		fontFamilyPane.getChildren().addAll(new Label("Family"), fontFamilyList);

		VBox fontStylePane = new VBox();
		styleList = new ListView<>();
		styleList.setPrefWidth(200);
		styleList.getItems().addAll(new String[] { "Regular", "Bold", "Italic", "Bold & Italic" });
		fontStylePane.getChildren().addAll(new Label("Style"), styleList);

		VBox fontSizePane = new VBox();
		sizeList = new ListView<>();
		sizeList.setPrefWidth(150);
		sizeList.getItems().addAll(new Integer[] { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36,
				38, 40, 42, 44, 46, 48, 50, 52 });
		fontSizePane.getChildren().addAll(new Label("Size"), sizeList);

		fontSettingPane.getChildren().addAll(fontFamilyPane, fontStylePane, fontSizePane);

		fontFamilyList.getSelectionModel().select(f.getFamily());
		fontFamilyList.getFocusModel().focus(fontFamilyList.getSelectionModel().getSelectedIndex());
		fontFamilyList.scrollTo(fontFamilyList.getSelectionModel().getSelectedIndex());
		
		if(f.getStyle().equalsIgnoreCase("Bold")) {
			styleList.getSelectionModel().select(1);
			styleList.getFocusModel().focus(1);
			styleList.scrollTo(1);
		}else if(f.getStyle().equalsIgnoreCase("Italic")) {
			styleList.getSelectionModel().select(2);
			styleList.getFocusModel().focus(2);
			styleList.scrollTo(2);
		}else if(f.getStyle().equalsIgnoreCase("Bold Italic")){
			styleList.getSelectionModel().select(3);
			styleList.getFocusModel().focus(3);
			styleList.scrollTo(3);
		}else {
			styleList.getSelectionModel().select(0);
			styleList.getFocusModel().focus(0);
			styleList.scrollTo(0);
		}
		
		sizeList.getSelectionModel().select(new Integer((int)f.getSize()));
		sizeList.getFocusModel().focus(new Integer((int)f.getSize()));
		sizeList.scrollTo(new Integer((int)f.getSize()));
		
		sampleLabel.fontProperty().bind(new ObjectBinding() {
			{
				bind(fontFamilyList.getSelectionModel().selectedItemProperty());
				bind(styleList.getSelectionModel().selectedItemProperty());
				bind(sizeList.getSelectionModel().selectedItemProperty());
			}

			@Override
			protected Object computeValue() {
				return getFont();
			}

		});

		setResultConverter(dialogButton -> {
			return getFont();
		});

	}

	private Font getFont() {
		String fontFamily = fontFamilyList.getSelectionModel().getSelectedItem();
		FontWeight weight = FontWeight.NORMAL;
		FontPosture posture = FontPosture.REGULAR;
		int size = sizeList.getSelectionModel().getSelectedItem();

		if (styleList.getSelectionModel().getSelectedIndex() == 0) {
			weight = FontWeight.NORMAL;
		} else if (styleList.getSelectionModel().getSelectedIndex() == 1) {
			weight = FontWeight.BOLD;
		} else if (styleList.getSelectionModel().getSelectedIndex() == 2) {
			posture = FontPosture.ITALIC;
		} else {
			weight = FontWeight.BOLD;
			posture = FontPosture.ITALIC;
		}

		return Font.font(fontFamily, weight, posture, size);
	}
}
