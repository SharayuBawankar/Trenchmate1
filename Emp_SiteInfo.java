package com.example.view.owner;
import com.example.Dao.GroqAPI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Emp_SiteInfo {

    private Stage stage;

    public Emp_SiteInfo(Stage stage) {
        this.stage = stage;
    }

    public void show() {

        VBox mainLayout = new VBox(30);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #FFFFFF;");

        // -------- Title --------
        Label siteNameLabel = new Label("Current Site: River Bridge Construction");
        siteNameLabel.setFont(Font.font("Arial", 34));
        siteNameLabel.setAlignment(Pos.CENTER);

        // -------- Top Section (Map + Stats) --------
        HBox topSection = new HBox(50);
        topSection.setAlignment(Pos.CENTER);

        // Left: Google Maps Placeholder (Square)
        Label mapPlaceholder = new Label("Google Maps\n(Placeholder)");
        mapPlaceholder.setPrefSize(350, 350);
        mapPlaceholder.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #CCCCCC; -fx-border-width: 2;");
        mapPlaceholder.setFont(Font.font("Arial", 18));
        mapPlaceholder.setAlignment(Pos.CENTER);

        // Right: Site Stats
        VBox siteStatsBox = new VBox(20);
        siteStatsBox.setAlignment(Pos.TOP_LEFT);

        Label managerLabel = new Label("Site Manager: John Doe");
        managerLabel.setFont(Font.font("Arial", 22));

        Label workforceLabel = new Label("Workforce: 87 Workers");
        workforceLabel.setFont(Font.font("Arial", 22));

        Label statusLabel = new Label("Status: Active");
        statusLabel.setFont(Font.font("Arial", 22));

        Label progressLabel = new Label("Overall Progress: 60%");
        progressLabel.setFont(Font.font("Arial", 22));

        ProgressBar progressBar = new ProgressBar(0.6);
        progressBar.setPrefWidth(400);
        progressBar.setPrefHeight(25);

        siteStatsBox.getChildren().addAll(managerLabel, workforceLabel, statusLabel, progressLabel, progressBar);

        topSection.getChildren().addAll(mapPlaceholder, siteStatsBox);

        // -------- Site Description --------
        Label descriptionLabel = new Label("Site Description:");
        descriptionLabel.setFont(Font.font("Arial", 24));

        TextArea descriptionArea = new TextArea("This bridge construction project is in Phase 2, focusing on reinforcement work and safety inspections.");
        descriptionArea.setWrapText(true);
        descriptionArea.setEditable(false);
        descriptionArea.setPrefHeight(180);
        descriptionArea.setFont(Font.font("Arial", 16));

        // -------- Middle Section (Materials & Issues) --------
        HBox middleSection = new HBox(50);
        middleSection.setAlignment(Pos.CENTER);

        // Left Column: Key Materials
        VBox materialsBox = new VBox(15);
        materialsBox.setAlignment(Pos.TOP_LEFT);
        materialsBox.setPrefWidth(600);

        Label materialsLabel = new Label("Key Materials Being Used:");
        materialsLabel.setFont(Font.font("Arial", 22));

        ListView<String> materialsList = new ListView<>();
        materialsList.getItems().addAll("Cement", "Steel Bars", "Gravel", "Concrete Mix");
        materialsList.setPrefHeight(250);

        materialsBox.getChildren().addAll(materialsLabel, materialsList);

        // Right Column: Recent Issues
        VBox issuesBox = new VBox(15);
        issuesBox.setAlignment(Pos.TOP_LEFT);
        issuesBox.setPrefWidth(600);

        Label issuesLabel = new Label("Recent Issues:");
        issuesLabel.setFont(Font.font("Arial", 22));

        ListView<String> issuesList = new ListView<>();
        issuesList.getItems().addAll("Permit delay resolved", "Material delivery pending", "Crane requires maintenance");
        issuesList.setPrefHeight(250);

        issuesBox.getChildren().addAll(issuesLabel, issuesList);

        middleSection.getChildren().addAll(materialsBox, issuesBox);

        // -------- Bottom Buttons --------
        HBox bottomButtonsBox = new HBox(40);
        bottomButtonsBox.setAlignment(Pos.CENTER);
        
        Button aiHelpButton = new Button("Ask AI About This Work");
        aiHelpButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 8;");
        aiHelpButton.setMinWidth(250);
        aiHelpButton.setMinHeight(50);
        aiHelpButton.setOnAction(e -> {
            String description = descriptionArea.getText();  // get text from the site description
            GroqAPI dao = new GroqAPI();                     // create backend DAO
            new Emp_AI(stage, description, dao).show();      // pass all 3 params
        });

        Button contactManagerButton = new Button("Contact Site Manager");
        contactManagerButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 18; -fx-background-radius: 10;");
        contactManagerButton.setMinWidth(250);
        contactManagerButton.setMinHeight(50);

        Button backButton = new Button("Back to Dashboard");
        backButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 18; -fx-background-radius: 10;");
        backButton.setMinWidth(250);
        backButton.setMinHeight(50);

        backButton.setOnAction(e -> {
            new Emp_Dashboard(stage, null).show();
        });

        bottomButtonsBox.getChildren().addAll(aiHelpButton, contactManagerButton, backButton);

        // -------- Final Assembly --------
        mainLayout.getChildren().addAll(
                siteNameLabel,
                topSection,
                descriptionLabel,
                descriptionArea,
                middleSection,
                bottomButtonsBox
        );

        Scene scene = new Scene(mainLayout, 1336, 768);

        stage.setTitle("Site Information");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
