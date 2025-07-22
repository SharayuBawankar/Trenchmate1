package com.example.view.owner;

import com.example.Dao.GroqAPI;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Emp_AI {

    private Stage popup;
    private TextArea outputArea;
    private TextArea siteDescriptionArea;
    private TextField promptField;

    private final GroqAPI empAIDao;

    public Emp_AI(Stage owner, String siteDescription, GroqAPI dao) {
        this.empAIDao = dao;

        popup = new Stage();
        popup.initOwner(owner);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("AI Assistant");
        popup.setMinWidth(800);
        popup.setMinHeight(600);

        Label header = new Label("🤖 AI Assistant");
        header.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: linear-gradient(to right, #4CAF50, #2196F3); -fx-padding: 10;");

        siteDescriptionArea = new TextArea(siteDescription);
        siteDescriptionArea.setWrapText(true);
        siteDescriptionArea.setEditable(false);
        siteDescriptionArea.setPrefHeight(120);
        siteDescriptionArea.setStyle("-fx-font-size: 14;");

        Label descLabel = new Label("Site Description:");
        descLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        VBox descBox = new VBox(5, descLabel, siteDescriptionArea);
        descBox.setPadding(new Insets(10, 0, 10, 0));

        promptField = new TextField();
        promptField.setPromptText("Type your prompt here...");
        promptField.setStyle("-fx-background-radius: 10; -fx-padding: 10; -fx-font-size: 14;");
        promptField.setMinWidth(600);

        Button sendPromptBtn = new Button("Send");
        sendPromptBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-size: 14;");
        sendPromptBtn.setOnAction(e -> {
            String prompt = promptField.getText().trim();
            if (!prompt.isEmpty()) {
                sendPrompt(prompt);
                promptField.clear();
            } else {
                appendOutput("❌ Please enter a prompt.");
            }
        });

        HBox promptBox = new HBox(10, promptField, sendPromptBtn);
        promptBox.setAlignment(Pos.CENTER_LEFT);
        promptBox.setPadding(new Insets(10, 0, 20, 0));

        Button explainSiteBtn = new Button("Explain Site");
        explainSiteBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-size: 16; -fx-min-width: 150; -fx-min-height: 40;");
        explainSiteBtn.setOnAction(e -> {
            String desc = siteDescriptionArea.getText();
            if (desc != null && !desc.isEmpty()) {
                sendPrompt("Explain this site work in simple terms: " + desc);
            } else {
                appendOutput("❌ Site description is empty.");
            }
        });

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        outputArea.setStyle("-fx-background-color: #f0f0f0; -fx-font-size: 14; -fx-padding: 10;");
        outputArea.setPrefHeight(300);

        VBox mainBox = new VBox(10,
                header,
                descBox,
                promptBox,
                explainSiteBtn,
                outputArea);

        mainBox.setPadding(new Insets(20));
        mainBox.setStyle("-fx-background-color: #FFFFFF;");

        Scene scene = new Scene(mainBox);
        popup.setScene(scene);
    }

    public void show() {
        popup.showAndWait();
    }

    private void sendPrompt(String prompt) {
        appendOutput("⏳ AI is thinking...");
        new Thread(() -> {
            try {
                String response = empAIDao.getAIResponse(prompt);
                Platform.runLater(() -> appendOutput("\n🤖 AI: " + response + "\n"));
            } catch (Exception e) {
                Platform.runLater(() -> appendOutput("❌ Error: " + e.getMessage()));
            }
        }).start();
    }

    private void appendOutput(String text) {
        outputArea.appendText(text + "\n");
    }
}
