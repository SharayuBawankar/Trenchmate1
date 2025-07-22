package com.example.view.owner;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Owner_AssignTasks {

    private final Stage stage;

    public Owner_AssignTasks(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f9f9f9;");

        Label title = new Label("🛠️ Assign Tasks to Employees");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Search Box + ListView for Employees
        TextField searchField = new TextField();
        searchField.setPromptText("Search employees...");
        searchField.setMinHeight(40);

        ObservableList<String> employees = FXCollections.observableArrayList();
        for (int i = 1; i <= 30; i++) {
            employees.add("Employee " + i);
        }

        ListView<String> employeeListView = new ListView<>(employees);
        employeeListView.setPrefHeight(400);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<String> filteredList = FXCollections.observableArrayList();
            for (String emp : employees) {
                if (emp.toLowerCase().contains(newValue.toLowerCase())) {
                    filteredList.add(emp);
                }
            }
            employeeListView.setItems(filteredList);
        });

        VBox employeeBox = new VBox(10, new Label("👷 Employees"), searchField, new ScrollPane(employeeListView));
        employeeBox.setPadding(new Insets(10));
        employeeBox.setPrefWidth(300);

        // Task and Site Selection
        ComboBox<String> taskComboBox = new ComboBox<>();
        taskComboBox.getItems().addAll(
                "Stringing", "Trenching", "Lowering", "Welding", "HDD Assigning",
                "Blasting", "Cleaning", "Backfilling", "Radiography Testing"
        );
        taskComboBox.setPromptText("Select Task");
        taskComboBox.setPrefWidth(400);
        taskComboBox.setMinHeight(50);

        ComboBox<String> siteComboBox = new ComboBox<>();
        siteComboBox.getItems().addAll("Site Alpha", "Site Bravo", "Site Charlie");
        siteComboBox.setPromptText("Select Site");
        siteComboBox.setPrefWidth(400);
        siteComboBox.setMinHeight(50);

        DatePicker deadlinePicker = new DatePicker();
        deadlinePicker.setPromptText("Select Deadline");
        deadlinePicker.setPrefWidth(400);
        deadlinePicker.setMinHeight(50);

        Button assignButton = new Button("Assign Task");
        assignButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16;");
        assignButton.setPrefWidth(400);
        assignButton.setMinHeight(50);

        assignButton.setOnAction(e -> {
            String employee = employeeListView.getSelectionModel().getSelectedItem();
            String task = taskComboBox.getValue();
            String site = siteComboBox.getValue();
            String deadline = (deadlinePicker.getValue() != null) ? deadlinePicker.getValue().toString() : null;

            if (employee == null || task == null || site == null || deadline == null) {
                new Alert(Alert.AlertType.ERROR, "Select Employee, Task, Site and Deadline").showAndWait();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION,
                        "Assign '" + task + "' to " + employee + " at " + site + "\nDeadline: " + deadline,
                        ButtonType.YES, ButtonType.NO).showAndWait();
            }
        });

        Button addSiteButton = new Button("➕ Add New Site");
        addSiteButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addSiteButton.setPrefWidth(200);

        addSiteButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add New Site");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter new site name:");
            dialog.showAndWait().ifPresent(siteName -> {
                if (!siteName.trim().isEmpty()) {
                    siteComboBox.getItems().add(siteName);
                }
            });
        });

        VBox taskBox = new VBox(15,
                new Label("📋 Task Details"),
                taskComboBox,
                siteComboBox,
                deadlinePicker,
                assignButton,
                addSiteButton
        );
        taskBox.setAlignment(Pos.TOP_LEFT);

        // Google Maps Placeholder - Larger
        Label mapPlaceholder = new Label("Google Maps View (Placeholder)");
        mapPlaceholder.setPrefSize(700, 500);  // Increased size
        mapPlaceholder.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #cccccc; -fx-border-width: 2px;");
        mapPlaceholder.setAlignment(Pos.CENTER);

        VBox mapBox = new VBox(10, new Label("🗺️ Site Location"), mapPlaceholder);
        mapBox.setAlignment(Pos.TOP_CENTER);
        mapBox.setPadding(new Insets(0, 0, 0, 50));  // Move rightward

        HBox contentBox = new HBox(50, employeeBox, taskBox, mapBox);
        contentBox.setAlignment(Pos.TOP_LEFT);

        Button backButton = new Button("⬅ Back to Dashboard");
        backButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 16;");
        backButton.setOnAction(e -> new Owner_Dashboard(stage).show());

        mainLayout.getChildren().addAll(title, contentBox, backButton);

        Scene scene = new Scene(mainLayout, 1600, 850);  // Wider Scene
        stage.setTitle("Assigned Tasks");
        stage.setScene(scene);
        stage.show();
    }
}
