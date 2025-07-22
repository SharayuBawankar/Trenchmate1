package com.example.view.owner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Emp_Emergency {

    public static void showEmergencyPopup(Stage owner) {
        Stage popup = new Stage();
        popup.initOwner(owner);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Report Emergency");

        // Emergency Type Buttons
        Button fireButton = new Button("Fire");
        Button injuryButton = new Button("Injury");
        Button collapseButton = new Button("Collapse");
        Button gasLeakButton = new Button("Gas Leak");

        Button[] emergencyButtons = {fireButton, injuryButton, collapseButton, gasLeakButton};
        for (Button btn : emergencyButtons) {
            btn.setMinWidth(150);
            btn.setMinHeight(60);
            btn.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-font-size: 18; -fx-background-radius: 10;");
        }

        GridPane emergencyButtonsGrid = new GridPane();
        emergencyButtonsGrid.setHgap(20);
        emergencyButtonsGrid.setVgap(20);
        emergencyButtonsGrid.setAlignment(Pos.CENTER);

        emergencyButtonsGrid.add(fireButton, 0, 0);
        emergencyButtonsGrid.add(injuryButton, 1, 0);
        emergencyButtonsGrid.add(collapseButton, 0, 1);
        emergencyButtonsGrid.add(gasLeakButton, 1, 1);

        // TextArea for Description
        Label descriptionLabel = new Label("Describe the emergency:");
        descriptionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Describe the emergency situation...");
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefHeight(200);

        // Post Emergency Button
        Button postButton = new Button("Post Emergency");
        postButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 20; -fx-background-radius: 10;");
        postButton.setMinHeight(60);
        postButton.setMinWidth(350);

        postButton.setOnAction(e -> {
            String description = descriptionArea.getText();
            System.out.println("Emergency Posted: " + description);
            popup.close();
        });

        VBox layout = new VBox(30,
                emergencyButtonsGrid,
                descriptionLabel,
                descriptionArea,
                postButton
        );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        Scene scene = new Scene(layout, 800, 600);
        popup.setScene(scene);
        popup.showAndWait();
    }
}
