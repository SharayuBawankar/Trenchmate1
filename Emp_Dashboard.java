package com.example.view.owner;


import com.example.Dao.firebaseAuth;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Emp_Dashboard {

    private final Stage stage;
    private final firebaseAuth authService;


    public Emp_Dashboard(Stage stage, firebaseAuth authService) {
        this.stage = stage;
        this.authService = authService;
    }

    public void show() {
        VBox leftPane = new VBox();
        leftPane.setPrefWidth(1336 * 0.30);
        leftPane.setStyle("-fx-background-color: #f0f0f0;");
        leftPane.setSpacing(20);

        VBox welcomeBox = new VBox();
        welcomeBox.setPrefHeight(768 * 0.20);
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-background-radius: 10; -fx-border-radius: 10;");

        Label welcomeLabel = new Label("Welcome, Username");
        welcomeLabel.setFont(Font.font("Arial", 24));
        welcomeBox.getChildren().add(welcomeLabel);

        Separator separator = new Separator(Orientation.HORIZONTAL);

        Button assignedworkButton = new Button("Assigned Work");
        Button dataEntryButton = new Button("Data Entry");
        Button attendanceButton = new Button("Attendance");
        Button siteInfoButton = new Button("Site Information");
        Button logoutButton = new Button("Logout");

        attendanceButton.setOnAction(e -> new Emp_Attendance(stage, authService).show());
        dataEntryButton.setOnAction(e -> new Emp_DataEntry(stage,authService).show());
        logoutButton.setOnAction(e -> new Login().start(stage));
        assignedworkButton.setOnAction(e -> new Emp_AssignedWork(stage).show());
        siteInfoButton.setOnAction(e -> new Emp_SiteInfo(stage).show());

        Button[] menuButtons = {assignedworkButton, dataEntryButton, attendanceButton, siteInfoButton, logoutButton};
        for (Button btn : menuButtons) {
            btn.setMinWidth(350);
            btn.setMinHeight(60);
            btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 18; -fx-background-radius: 10;");
        }

        VBox menuBox = new VBox(20, assignedworkButton, dataEntryButton, attendanceButton, siteInfoButton, logoutButton);
        menuBox.setAlignment(Pos.TOP_CENTER);

        leftPane.getChildren().addAll(welcomeBox, separator, menuBox);
        VBox.setVgrow(menuBox, Priority.ALWAYS);

        VBox rightPane = new VBox();
        rightPane.setPrefWidth(1336 * 0.70);
        rightPane.setStyle("-fx-background-color: #FFFFFF;");   
        rightPane.setPadding(new Insets(20));
        rightPane.setSpacing(20);

        Label siteNameLabel = new Label("Current Site: Example Construction Site");
        siteNameLabel.setFont(Font.font("Arial", 30));

        Label mapPlaceholder = new Label("Google Map Location (Placeholder)");
        mapPlaceholder.setPrefHeight(400);
        mapPlaceholder.setPrefWidth(700);
        mapPlaceholder.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #CCCCCC; -fx-border-width: 2;");
        mapPlaceholder.setFont(Font.font("Arial", 20));
        mapPlaceholder.setAlignment(Pos.CENTER);

        VBox progressBarsBox = new VBox(30);
        progressBarsBox.setAlignment(Pos.CENTER_LEFT);

        String[] progressLabels = {"  Site Progress", "  Resources Used", "  Deadline"};

        for (String label : progressLabels) {
            Label indicatorBox = new Label("✔️");
            indicatorBox.setPrefSize(60, 40);
            indicatorBox.setStyle("-fx-background-color: #DDDDDD; -fx-border-color: #CCCCCC; -fx-border-width: 1;");
            indicatorBox.setAlignment(Pos.CENTER);

            ProgressBar progressBar = new ProgressBar(0.5);
            progressBar.setPrefWidth(300);
            progressBar.setPrefHeight(40);

            Label progressText = new Label(label);
            progressText.setFont(Font.font("Arial", 16));

            StackPane progressStack = new StackPane(progressBar, progressText);
            progressStack.setAlignment(Pos.CENTER_LEFT);
            progressStack.setPadding(new Insets(0, 0, 0, 10));

            HBox progressRow = new HBox(10, indicatorBox, progressStack);
            progressRow.setAlignment(Pos.CENTER_LEFT);

            progressBarsBox.getChildren().add(progressRow);
        }

        Button emergencyButton = new Button("EMERGENCY");
        emergencyButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 20; -fx-background-radius: 10;");
        emergencyButton.setMinWidth(200);
        emergencyButton.setMinHeight(80);
        emergencyButton.setOnAction(e -> Emp_Emergency.showEmergencyPopup(stage));
;

        VBox emergencyBox = new VBox(emergencyButton);
        emergencyBox.setAlignment(Pos.CENTER);

        VBox progressAndEmergencyBox = new VBox(30, progressBarsBox, emergencyBox);
        progressAndEmergencyBox.setAlignment(Pos.CENTER_LEFT);

        HBox mapAndProgressLayout = new HBox(50, mapPlaceholder, progressAndEmergencyBox);
        mapAndProgressLayout.setAlignment(Pos.CENTER_LEFT);

        Label notificationLabel = new Label("Notifications");
        notificationLabel.setFont(Font.font("Arial", 20));

        TextArea notificationArea = new TextArea("• Site update pending\n• Supervisor meeting at 3PM\n• New data entry required");
        notificationArea.setPrefHeight(200);
        notificationArea.setEditable(false);

        VBox notificationBox = new VBox(10, notificationLabel, notificationArea);
        notificationBox.setAlignment(Pos.BOTTOM_LEFT);

        rightPane.getChildren().addAll(siteNameLabel, mapAndProgressLayout, notificationBox);

        HBox mainLayout = new HBox(leftPane, rightPane);

        Scene scene = new Scene(mainLayout, 1336, 768);

        stage.setTitle("Employee Dashboard");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
