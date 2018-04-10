package com.kostikiadis.sun.dependencies;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Dimension2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;


public class Legend extends Region {

	private static final int GAP = 5;

	
	private int rows = 0, columns = 0;
	private ListChangeListener<LegendItem> itemsListener = new ListChangeListener<LegendItem>() {
		@Override
		public void onChanged(Change<? extends LegendItem> c) {
			getChildren().clear();
			for (LegendItem item : getItems())
				getChildren().add(item.label);
			if (isVisible())
				requestLayout();
		}
	};

	
	private BooleanProperty vertical = new BooleanPropertyBase(false) {
		@Override
		protected void invalidated() {
			requestLayout();
		}

		@Override
		public Object getBean() {
			return Legend.this;
		}

		@Override
		public String getName() {
			return "vertical";
		}
	};

	public final boolean isVertical() {
		return vertical.get();
	}

	public final void setVertical(boolean value) {
		vertical.set(value);
	}

	public final BooleanProperty verticalProperty() {
		return vertical;
	}

	private ObjectProperty<ObservableList<LegendItem>> items = new ObjectPropertyBase<ObservableList<LegendItem>>() {
		ObservableList<LegendItem> oldItems = null;

		@Override
		protected void invalidated() {
			if (oldItems != null)
				oldItems.removeListener(itemsListener);
			getChildren().clear();
			ObservableList<LegendItem> newItems = get();
			if (newItems != null) {
				newItems.addListener(itemsListener);
				for (LegendItem item : newItems)
					getChildren().add(item.label);
			}
			oldItems = get();
			requestLayout();
		}

		@Override
		public Object getBean() {
			return Legend.this;
		}

		@Override
		public String getName() {
			return "items";
		}
	};

	public final void setItems(ObservableList<LegendItem> value) {
		itemsProperty().set(value);
	}

	public final ObservableList<LegendItem> getItems() {
		return items.get();
	}

	public final ObjectProperty<ObservableList<LegendItem>> itemsProperty() {
		return items;
	}

	public Legend() {
		setItems(FXCollections.<LegendItem>observableArrayList());
		getStyleClass().setAll("chart-legend");
	}

	
	private Dimension2D getTileSize() {
		double maxWidth = 0;
		double maxHeight = 0;
		for (LegendItem item : getItems()) {
			maxWidth = Math.max(maxWidth, item.label.prefWidth(-1));
			maxHeight = Math.max(maxHeight, item.label.prefHeight(-1));
		}
		return new Dimension2D(Math.ceil(maxWidth), Math.ceil(maxHeight));
	}

	@Override
	protected double computePrefWidth(double height) {
		if (getItems().isEmpty())
			return 0; 
		final double contentHeight = height - snappedTopInset() - snappedBottomInset();
		Dimension2D tileSize = getTileSize();
		if (height == -1) {
			if (columns <= 1)
				return tileSize.getWidth() + snappedLeftInset() + snappedRightInset();
		} else {
			rows = (int) Math.max(1, Math.floor(contentHeight / (tileSize.getHeight() + GAP)));
			columns = (int) Math.ceil(getItems().size() / (double) rows);
		}
		if (columns == 1)
			rows = Math.min(rows, getItems().size());
		return (columns * (tileSize.getWidth() + GAP)) - GAP + snappedLeftInset() + snappedRightInset();
	}

	@Override
	protected double computePrefHeight(double width) {
		if (getItems().isEmpty())
			return 0; 
		final double contentWidth = width - snappedLeftInset() - snappedRightInset();
		Dimension2D tileSize = getTileSize();
		if (width == -1) {
			if (rows <= 1)
				return tileSize.getHeight() + snappedTopInset() + snappedBottomInset();
		} else {
			columns = (int) Math.max(1, Math.floor(contentWidth / (tileSize.getWidth() + GAP)));
			rows = (int) Math.ceil(getItems().size() / (double) columns);
		}
		if (rows == 1)
			columns = Math.min(columns, getItems().size());
		return (rows * (tileSize.getHeight() + GAP)) - GAP + snappedTopInset() + snappedBottomInset();
	}

	@Override
	protected void layoutChildren() {
		Dimension2D tileSize = getTileSize();
		if (isVertical()) {
			double left = snappedLeftInset();
			outer: for (int col = 0; col < columns; col++) {
				double top = snappedTopInset();
				for (int row = 0; row < rows; row++) {
					int itemIndex = (col * rows) + row;
					if (itemIndex >= getItems().size())
						break outer;
					getItems().get(itemIndex).label.resizeRelocate(left, top, tileSize.getWidth(),
							tileSize.getHeight());
					top += tileSize.getHeight() + GAP;
				}
				left += tileSize.getWidth() + GAP;
			}
		} else {
			double top = snappedTopInset();
			outer: for (int row = 0; row < rows; row++) {
				double left = snappedLeftInset();
				for (int col = 0; col < columns; col++) {
					int itemIndex = (row * columns) + col;
					if (itemIndex >= getItems().size())
						break outer;
					getItems().get(itemIndex).label.resizeRelocate(left, top, tileSize.getWidth(),
							tileSize.getHeight());
					left += tileSize.getWidth() + GAP;
				}
				top += tileSize.getHeight() + GAP;
			}
		}
	}

	public static class LegendItem {

		private Label label = new Label();

		private StringProperty text = new StringPropertyBase() {
			@Override
			protected void invalidated() {
				label.setText(get());
			}

			@Override
			public Object getBean() {
				return LegendItem.this;
			}

			@Override
			public String getName() {
				return "text";
			}
		};

		public final String getText() {
			return text.getValue();
		}

		public final void setText(String value) {
			text.setValue(value);
		}

		public final StringProperty textProperty() {
			return text;
		}

		private ObjectProperty<Node> symbol = new ObjectPropertyBase<Node>(new Region()) {
			@Override
			protected void invalidated() {
				Node symbol = get();
				if (symbol != null)
					symbol.getStyleClass().setAll("chart-legend-item-symbol");
				label.setGraphic(symbol);
			}

			@Override
			public Object getBean() {
				return LegendItem.this;
			}

			@Override
			public String getName() {
				return "symbol";
			}
		};

		public final Node getSymbol() {
			return symbol.getValue();
		}

		public final void setSymbol(Node value) {
			symbol.setValue(value);
		}

		public final ObjectProperty<Node> symbolProperty() {
			return symbol;
		}

		public LegendItem(String text) {
			setText(text);
			label.getStyleClass().add("chart-legend-item");
			label.setAlignment(Pos.CENTER_LEFT);
			label.setContentDisplay(ContentDisplay.LEFT);
			label.setGraphic(getSymbol());
			getSymbol().getStyleClass().setAll("chart-legend-item-symbol");
		}

		public LegendItem(String text, Node symbol) {
			this(text);
			setSymbol(symbol);
		}
	}
}