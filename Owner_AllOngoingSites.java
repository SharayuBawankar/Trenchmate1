package com.example.view.owner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Owner_AllOngoingSites {

    private final Stage stage;

    public Owner_AllOngoingSites(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // Left Pane (30%)
        VBox leftPane = new VBox(20);
        leftPane.setPrefWidth(1336 * 0.30);
        leftPane.setStyle("-fx-background-color: #f0f0f0;");
        leftPane.setPadding(new Insets(20));
        leftPane.setAlignment(Pos.TOP_CENTER);

        Label selectSiteLabel = new Label("Select Site:");
        selectSiteLabel.setFont(Font.font("Arial", 22));

        Button siteButton1 = createSiteButton("Site Alpha");
        Button siteButton2 = createSiteButton("Site Bravo");
        Button siteButton3 = createSiteButton("Site Charlie");

        Button backButton = new Button("⬅ Back to Dashboard");
        backButton.setMinWidth(250);
        backButton.setMinHeight(50);
        backButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 10;");
        backButton.setOnAction(e -> new Owner_Dashboard(stage).show());

        VBox.setVgrow(backButton, Priority.ALWAYS);

        leftPane.getChildren().addAll(selectSiteLabel, siteButton1, siteButton2, siteButton3, new Separator(), backButton);

        // Right Pane (70%)
        VBox rightPane = new VBox(20);
        rightPane.setPrefWidth(1336 * 0.70);
        rightPane.setPadding(new Insets(20));
        rightPane.setStyle("-fx-background-color: #ffffff;");

        // Google Maps Placeholder
        Label mapPlaceholder = new Label("Google Maps Placeholder");
        mapPlaceholder.setPrefSize(600, 300);
        mapPlaceholder.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #CCCCCC; -fx-border-width: 2;");
        mapPlaceholder.setFont(Font.font("Arial", 20));
        mapPlaceholder.setAlignment(Pos.CENTER);

        // Checkboxes beside map
        CheckBox roadsCheckbox = new CheckBox("Show Roads");
        CheckBox utilitiesCheckbox = new CheckBox("Show Utilities");
        CheckBox boundariesCheckbox = new CheckBox("Show Boundaries");

        VBox checkBoxPane = new VBox(10, roadsCheckbox, utilitiesCheckbox, boundariesCheckbox);
        checkBoxPane.setAlignment(Pos.TOP_LEFT);

        HBox mapAndOptionsBox = new HBox(30, mapPlaceholder, checkBoxPane);
        mapAndOptionsBox.setAlignment(Pos.CENTER_LEFT);

        // Assigned Workforce Section
        Label workforceData = new Label("Assigned Workforce Data:\nSupervisor: John Doe\nWorkers: 25\nEquipment: 5");
        workforceData.setPrefSize(600, 200);
        workforceData.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #CCCCCC; -fx-border-width: 2;");
        workforceData.setFont(Font.font("Arial", 16));
        workforceData.setWrapText(true);

        Button expandViewButton = new Button("View Expanded Workforce Data");
        expandViewButton.setPrefWidth(300);
        expandViewButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 10;");

        VBox workforceBox = new VBox(15, workforceData, expandViewButton);
        workforceBox.setAlignment(Pos.CENTER_LEFT);

        // Progress Bars Section
        ProgressBar workProgress = new ProgressBar(0.65);
        ProgressBar materialsProgress = new ProgressBar(0.40);
        ProgressBar deadlineProgress = new ProgressBar(0.80);

        workProgress.setPrefWidth(600);
        materialsProgress.setPrefWidth(600);
        deadlineProgress.setPrefWidth(600);

        Label workLabel = new Label("Work Completed");
        Label materialsLabel = new Label("Materials Used");
        Label deadlineLabel = new Label("Deadline Progress");

        VBox progressBarsBox = new VBox(10,
                workLabel, workProgress,
                materialsLabel, materialsProgress,
                deadlineLabel, deadlineProgress
        );

        progressBarsBox.setAlignment(Pos.CENTER_LEFT);
        progressBarsBox.setPadding(new Insets(20, 0, 0, 0));

        // Assemble Right Pane
        rightPane.getChildren().addAll(mapAndOptionsBox, workforceBox, progressBarsBox);

        // Main Layout
        HBox mainLayout = new HBox(leftPane, rightPane);

        Scene scene = new Scene(mainLayout, 1336, 768);
        stage.setTitle("All Ongoing Sites");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private Button createSiteButton(String siteName) {
        Button button = new Button(siteName);
        button.setMinWidth(250);
        button.setMinHeight(60);
        button.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 10;");
        return button;
    }
}
