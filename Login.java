package com.example.view.owner;

import com.example.Controller.Notification;
import com.example.Dao.firebaseAuth;
import javafx.scene.paint.Color; // Ensure Color is imported for DropShadow

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Login extends Application {

    private TextField loginField;
    private PasswordField passwordField;
    private firebaseAuth authService; 

    @Override
    public void start(Stage stage) {

        StackPane root = new StackPane();
        root.setPrefSize(1336, 768); 
       
        Pane gridPane = new Pane(); 
        Color gridColor = Color.WHITE.deriveColor(0, 1, 1, 0.1); 

        
        for (int i = 0; i <= root.getPrefWidth(); i += 50) { 
            Line line = new Line(i, 0, i, root.getPrefHeight());
            line.setStroke(gridColor);
            line.setStrokeWidth(1);
            line.getStrokeDashArray().addAll(5.0, 5.0); 
            gridPane.getChildren().add(line);
        }
        
        for (int i = 0; i <= root.getPrefHeight(); i += 50) {
            Line line = new Line(0, i, root.getPrefWidth(), i);
            line.setStroke(gridColor);
            line.setStrokeWidth(1);
            line.getStrokeDashArray().addAll(5.0, 5.0); 
            gridPane.getChildren().add(line);
        }

        Image image = new Image("/Assets/Images/WhatsApp Image 2025-07-19 at 19.37.08.jpeg");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        Label titleLabel = new Label("TrenchMate");
        titleLabel.setFont(Font.font("Arial", 28));
        titleLabel.setStyle("-fx-text-fill: #333333;");

        loginField = new TextField();
        loginField.setPromptText("Login ID");
        loginField.setMaxWidth(400);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(400);

        Notification notification = new Notification();
        authService = new firebaseAuth();

        Button signInButton = new Button("Sign In");
        signInButton.setMinWidth(180);
        signInButton.setOnAction(e -> {
            String loginID = loginField.getText();
            String password = passwordField.getText();

            boolean success = authService.loginWithEmailAndPassword(loginID, password);

            if (success) {
                notification.showNotification("Login Successful", "Welcome back!");

        // Navigate to Dashboard (refactored)
                Emp_Dashboard dashboard = new Emp_Dashboard(stage, authService);  // Pass current stage
                dashboard.show();  // Show dashboard

            } else {
                notification.showNotification("Login Failed", "Please check your credentials.");
            }
        });

        Button signUpButton = new Button("Sign Up");
        signUpButton.setMinWidth(180);
        signUpButton.setOnAction(e -> {
            String loginID = loginField.getText();
            String password = passwordField.getText();

            boolean success = authService.signUpWithEmailAndPassword(loginID, password);

            if (success) {
                notification.showNotification("Signup Successful", "Account created successfully!");
            } else {
                notification.showNotification("Signup Failed", "Unable to create account.");
            }
        });

        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password?");
        forgotPasswordLink.setTextFill(Color.ANTIQUEWHITE);

        HBox buttonsContainer = new HBox(20, signInButton, signUpButton);
        buttonsContainer.setAlignment(Pos.CENTER);

        VBox vb = new VBox(20, imageView, titleLabel, loginField, passwordField, buttonsContainer, forgotPasswordLink);
        vb.setAlignment(Pos.CENTER);
        vb.setStyle("-fx-background-color: transparent;");
        vb.setPadding(new Insets(20));

        root.getChildren().addAll(gridPane,vb);
        root.setStyle("-fx-background-color: linear-gradient(to top right, #007ACC, #FF7F00);");
       
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("TrenchMate");
        stage.setWidth(1336);
        stage.setHeight(768);
        stage.show();
    }
}