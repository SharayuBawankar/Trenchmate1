package com.example.view.owner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Owner_Dashboard {

    private final Stage stage;

    public Owner_Dashboard(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // Sidebar (30%)
        VBox sidebar = new VBox(20);
        sidebar.setPrefWidth(1336 * 0.30);
        sidebar.setStyle("-fx-background-color: #f0f0f0;");
        sidebar.setPadding(new Insets(20));

        VBox welcomeBox = new VBox();
        welcomeBox.setPrefHeight(768 * 0.20);
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-background-radius: 10; -fx-border-radius: 10;");

        Label welcomeLabel = new Label("Welcome, Owner");
        welcomeLabel.setFont(Font.font("Arial", 24));
        welcomeBox.getChildren().add(welcomeLabel);

        Button allSitesButton = createSidebarButton("All Ongoing Sites");
        Button assignTasksButton = createSidebarButton("Assign Tasks");
        Button pendingTasksButton = createSidebarButton("Pending Tasks");
        Button settingsButton = createSidebarButton("Settings");

        allSitesButton.setOnAction(e -> new Owner_AllOngoingSites(stage).show());
        assignTasksButton.setOnAction(e -> new Owner_AssignTasks(stage).show());

        Button logoutButton = new Button("Log Out");
        logoutButton.setMinWidth(300);
        logoutButton.setMinHeight(60);
        logoutButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 18; -fx-background-radius: 10;");

        VBox menuBox = new VBox(20, allSitesButton, assignTasksButton, pendingTasksButton, settingsButton);
        menuBox.setAlignment(Pos.TOP_CENTER);

        VBox.setVgrow(menuBox, Priority.ALWAYS);

        sidebar.getChildren().addAll(welcomeBox, new Separator(), menuBox, logoutButton);
        sidebar.setAlignment(Pos.TOP_CENTER);

        // Main Content (70%)
        VBox mainContent = new VBox(20);
        mainContent.setPrefWidth(1336 * 0.70);
        mainContent.setStyle("-fx-background-color: #ffffff;");
        mainContent.setPadding(new Insets(20));

        Label siteNameLabel = new Label("Current Site: Example Site");
        siteNameLabel.setFont(Font.font("Arial", 30));

        Label mapPlaceholder = new Label("Map of Previous Sites");
        mapPlaceholder.setPrefSize(300, 300);
        mapPlaceholder.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #CCCCCC; -fx-border-width: 2;");
        mapPlaceholder.setFont(Font.font("Arial", 18));
        mapPlaceholder.setAlignment(Pos.CENTER);

        Button materialsButton = createSmallButton("Materials Used");
        materialsButton.setOnAction(e -> showMaterialsUsedPopup());

        Button progressButton = createSmallButton("Progress + Deadline");
        Button emergencyButton = createSmallButton("Emergency Notification");

        VBox leftColumn = new VBox(20, mapPlaceholder, materialsButton, progressButton, emergencyButton);
        leftColumn.setAlignment(Pos.TOP_CENTER);

        Label assignedLabel = new Label("Assigned Workforce Data\n& Description");
        assignedLabel.setPrefSize(350, 500);
        assignedLabel.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #CCCCCC; -fx-border-width: 2;");
        assignedLabel.setFont(Font.font("Arial", 16));
        assignedLabel.setAlignment(Pos.CENTER);
        assignedLabel.setWrapText(true);

        Button expandButton = new Button("Click for Expanded View");
        expandButton.setPrefWidth(200);
        expandButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16;");

        VBox centerColumn = new VBox(20, assignedLabel, expandButton);
        centerColumn.setAlignment(Pos.TOP_CENTER);

        Label alertsPlaceholder = new Label("Alerts and Notifications");
        alertsPlaceholder.setPrefSize(250, 500);
        alertsPlaceholder.setStyle("-fx-background-color: #fff8e1; -fx-border-color: #CCCCCC; -fx-border-width: 2;");
        alertsPlaceholder.setFont(Font.font("Arial", 16));
        alertsPlaceholder.setAlignment(Pos.TOP_CENTER);
        alertsPlaceholder.setWrapText(true);

        VBox rightColumn = new VBox(alertsPlaceholder);
        rightColumn.setAlignment(Pos.TOP_CENTER);

        HBox centerLayout = new HBox(40, leftColumn, centerColumn, rightColumn);
        centerLayout.setAlignment(Pos.CENTER_LEFT);

        mainContent.getChildren().addAll(siteNameLabel, centerLayout);

        HBox mainLayout = new HBox(sidebar, mainContent);

        Scene scene = new Scene(mainLayout, 1336, 768);

        stage.setTitle("Owner Dashboard");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private Button createSidebarButton(String text) {
        Button button = new Button(text);
        button.setMinWidth(300);
        button.setMinHeight(60);
        button.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 18; -fx-background-radius: 10;");
        return button;
    }

    private Button createSmallButton(String text) {
        Button button = new Button(text);
        button.setMinWidth(250);
        button.setMinHeight(50);
        button.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 10;");
        return button;
    }

    private void showMaterialsUsedPopup() {
        Stage popup = new Stage();
        popup.setTitle("Materials Used");
        popup.setWidth(800);
        popup.setHeight(600);

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #ffffff;");

        Label header = new Label("📦 Materials Used at Current Site");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(20);

        String[][] materials = {
                {"Welding Rods", "150 units"},
                {"Diesel (litres)", "4500 litres"},
                {"JCB Machines", "3 on site"},
                {"HDD Machines", "2 on site"},
                {"Copper Sleeves", "200 used"},
                {"Pipes", "120 segments on site"}
        };

        for (int i = 0; i < materials.length; i++) {
            Label nameLabel = new Label("• " + materials[i][0]);
            nameLabel.setStyle("-fx-font-size: 16px;");
            Label quantityLabel = new Label(materials[i][1]);
            quantityLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            grid.add(nameLabel, 0, i);
            grid.add(quantityLabel, 1, i);
        }

        Button closeButton = new Button("Close");
        closeButton.setPrefWidth(200);
        closeButton.setPrefHeight(50);
        closeButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 10;");
        closeButton.setOnAction(e -> popup.close());

        layout.getChildren().addAll(header, grid, closeButton);
        layout.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(layout);
        popup.setScene(scene);
        popup.showAndWait();
    }
}
