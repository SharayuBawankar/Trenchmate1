package com.example.view.owner;

import com.example.Dao.GroqAPI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Emp_AssignedWork {

    private Stage stage;

    public Emp_AssignedWork(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // Left Pane (30%)
        VBox leftPane = new VBox(20);
        leftPane.setPrefWidth(1336 * 0.30);
        leftPane.setPadding(new Insets(20));
        leftPane.setStyle("-fx-background-color: #f0f0f0;");
        leftPane.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("Assigned Sites");
        titleLabel.setFont(Font.font("Arial", 24));

        Button site1Button = new Button("Site 1");
        Button site2Button = new Button("Site 2");
        Button site3Button = new Button("Site 3");

        Button[] siteButtons = {site1Button, site2Button, site3Button};
        for (Button btn : siteButtons) {
            btn.setMinWidth(300);
            btn.setMinHeight(80);
            btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 18; -fx-background-radius: 10;");
        }

        VBox siteButtonsBox = new VBox(15, site1Button, site2Button, site3Button);
        siteButtonsBox.setAlignment(Pos.CENTER);

        Button backButton = new Button("Back to Dashboard");
        backButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 10;");
        backButton.setMinWidth(250);
        backButton.setMinHeight(60);
        backButton.setOnAction(e -> {
            new Emp_Dashboard(stage, null).show();
        });

        VBox.setVgrow(siteButtonsBox, Priority.ALWAYS);
        leftPane.getChildren().addAll(titleLabel, siteButtonsBox, backButton);

        // Right Pane (70%)
        VBox rightPane = new VBox(20);
        rightPane.setPrefWidth(1336 * 0.70);
        rightPane.setPadding(new Insets(20));
        rightPane.setStyle("-fx-background-color: #FFFFFF;");

        Label siteNameLabel = new Label("Select a Site to View Details");
        siteNameLabel.setFont(Font.font("Arial", 28));

        Label mapPlaceholder = new Label("Google Maps Placeholder");
        mapPlaceholder.setPrefHeight(350);
        mapPlaceholder.setPrefWidth(700);
        mapPlaceholder.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #CCCCCC; -fx-border-width: 2;");
        mapPlaceholder.setFont(Font.font("Arial", 20));
        mapPlaceholder.setAlignment(Pos.CENTER);

        Label gpsLabel = new Label("GPS Location: N/A");
        gpsLabel.setFont(Font.font("Arial", 18));

        Label workDescription = new Label("Work Description: N/A");
        workDescription.setWrapText(true);
        workDescription.setFont(Font.font("Arial", 18));

        Button aiHelpButton = new Button("Ask AI about this Work");
        aiHelpButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 18; -fx-background-radius: 10;");
        aiHelpButton.setMinWidth(300);
        aiHelpButton.setMinHeight(60);
        aiHelpButton.setOnAction(e -> {
            String description = workDescription.getText();
            if (description == null || description.equals("Work Description: N/A")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("AI Assistant");
                alert.setHeaderText("No Work Selected");
                alert.setContentText("Please select a site first before asking the AI for help.");
                alert.showAndWait();
                return;
            }

            GroqAPI dao = new GroqAPI();
            new Emp_AI(stage, description, dao).show();
        });


        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Notes / Additional Information...");
        notesArea.setPrefHeight(150);

        // Update site info on button click
        site1Button.setOnAction(e -> {
            siteNameLabel.setText("Site 1 - Residential Complex Build");
            gpsLabel.setText("GPS Location: 19.0760° N, 72.8777° E");
            workDescription.setText("You are assigned to supervise concrete pouring and oversee foundation setup at the residential complex site.");
        });

        site2Button.setOnAction(e -> {
            siteNameLabel.setText("Site 2 - Office Tower Construction");
            gpsLabel.setText("GPS Location: 18.5204° N, 73.8567° E");
            workDescription.setText("Your task is to monitor steel framework erection and manage inventory delivery at the office tower.");
        });

        site3Button.setOnAction(e -> {
            siteNameLabel.setText("Site 3 - Bridge Infrastructure Project");
            gpsLabel.setText("GPS Location: 20.5937° N, 78.9629° E");
            workDescription.setText("Ensure bridge pillar casting and handle raw material requests at the infrastructure project site.");
        });

        rightPane.getChildren().addAll(
                siteNameLabel,
                mapPlaceholder,
                gpsLabel,
                workDescription,
                aiHelpButton,
                notesArea
        );

        // Main Layout
        HBox mainLayout = new HBox(leftPane, rightPane);

        Scene scene = new Scene(mainLayout, 1336, 768);

        stage.setTitle("Assigned Work");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
