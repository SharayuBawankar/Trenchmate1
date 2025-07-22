package com.example.view.owner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Emp_Maps_AdvancedView {

    private Stage stage;

    public Emp_Maps_AdvancedView(Stage stage) {
        this.stage = stage;
    }

    public void show() {

        // Left Pane (30%)
        VBox leftPane = new VBox(20);
        leftPane.setPrefWidth(1336 * 0.30);
        leftPane.setPadding(new Insets(20));
        leftPane.setStyle("-fx-background-color: #f0f0f0;");

        // Grid of 3x3 Buttons
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        for (int i = 0; i < 9; i++) {
            Button tagButton = new Button("Tag " + (i + 1));
            tagButton.setMinSize(80, 80);

            tagButton.setOnDragDetected(e -> tagButton.startFullDrag());

            GridPane.setConstraints(tagButton, i % 3, i / 3);
            gridPane.getChildren().add(tagButton);
        }

        gridPane.setPrefHeight(0.30 * 768);

        // Latitude and Longitude Fields
        TextField latField = new TextField();
        latField.setPromptText("Latitude");
        latField.setMaxWidth(200);

        TextField lonField = new TextField();
        lonField.setPromptText("Longitude");
        lonField.setMaxWidth(200);

        Button getLocationButton = new Button("Get Location");
        getLocationButton.setOnAction(e -> {
            latField.setText("19.123456");
            lonField.setText("72.987654");
        });

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            String lat = latField.getText();
            String lon = lonField.getText();
            System.out.println("Updated Lat: " + lat + ", Lon: " + lon);
        });

        VBox latLonBox = new VBox(10, latField, lonField, getLocationButton, updateButton);
        latLonBox.setAlignment(Pos.CENTER);

        VBox gridAndLocationBox = new VBox(30, gridPane, latLonBox);
        gridAndLocationBox.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(gridAndLocationBox, Priority.ALWAYS);  // ✅ This line is key

        // Back Button to Data Entry
        Button backToDataEntryButton = new Button("Back to Data Entry");
        backToDataEntryButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 10;");
        backToDataEntryButton.setMinWidth(250);
        backToDataEntryButton.setMinHeight(60);
        backToDataEntryButton.setOnAction(e -> {
            new Emp_DataEntry(stage,authService).show();
        });

        // Assemble left pane
        leftPane.getChildren().addAll(gridAndLocationBox, backToDataEntryButton);
        leftPane.setAlignment(Pos.TOP_CENTER);  // Optional, keeps it tidy
        
        // Right Pane (70%)
        VBox rightPane = new VBox(20);
        rightPane.setPrefWidth(1336 * 0.70);
        rightPane.setPadding(new Insets(20));
        rightPane.setStyle("-fx-background-color: #FFFFFF;");

        Label mapsLabel = new Label("Google Maps (Placeholder)");
        mapsLabel.setPrefHeight(400);
        mapsLabel.setPrefWidth(900);
        mapsLabel.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #CCCCCC; -fx-border-width: 2;");
        mapsLabel.setFont(Font.font("Arial", 20));
        mapsLabel.setAlignment(Pos.CENTER);

        TextArea infoArea = new TextArea();
        infoArea.setPromptText("Information Display...");
        infoArea.setPrefHeight(200);

        CheckBox cb1 = new CheckBox("Show Route");
        CheckBox cb2 = new CheckBox("Show Markers");
        CheckBox cb3 = new CheckBox("Enable Traffic");
        CheckBox cb4 = new CheckBox("Show Boundaries");

        cb1.setOnAction(e -> System.out.println("Show Route: " + cb1.isSelected()));
        cb2.setOnAction(e -> System.out.println("Show Markers: " + cb2.isSelected()));
        cb3.setOnAction(e -> System.out.println("Enable Traffic: " + cb3.isSelected()));
        cb4.setOnAction(e -> System.out.println("Show Boundaries: " + cb4.isSelected()));

        VBox checkBoxPane = new VBox(10, cb1, cb2, cb3, cb4);
        checkBoxPane.setPadding(new Insets(10));
        checkBoxPane.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1;");

        rightPane.getChildren().addAll(mapsLabel, infoArea, checkBoxPane);

        // Main Layout
        HBox mainLayout = new HBox(leftPane, rightPane);

        Scene scene = new Scene(mainLayout, 1336, 768);

        stage.setTitle("Advanced View");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
