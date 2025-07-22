package com.example.view.owner;

import com.example.Controller.Notification; // <--- ADD THIS IMPORT
import com.example.Dao.firebaseAuth; // <--- ADD THIS IMPORT
import org.json.JSONObject; // <--- ADD THIS IMPORT

import java.io.BufferedReader; // <--- ADD THIS IMPORT
import java.io.InputStreamReader; // <--- ADD THIS IMPORT
import java.io.OutputStream; // <--- ADD THIS IMPORT
import java.net.HttpURLConnection; // <--- ADD THIS IMPORT
import java.net.URL; // <--- ADD THIS IMPORT
import java.time.LocalDateTime; // <--- ADD THIS IMPORT
import java.time.ZoneId; // <--- ADD THIS IMPORT
import java.time.format.DateTimeFormatter; // <--- ADD THIS IMPORT
import java.util.UUID; // <--- ADD THIS IMPORT
import javafx.application.Platform; // <--- ADD THIS IMPORT
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Emp_DataEntry {
    private static final String FIRESTORE_BASE_URL = "https://firestore.googleapis.com/v1/projects/trenchmate1/databases/(default)/documents";

    private final Stage stage;
     private final firebaseAuth authService; // <--- ADD THIS LINE
    private final Notification notification;
    private TextField employeesField; // <--- ADD THIS LINE
    private TextArea materialsArea;   // <--- ADD THIS LINE
    private TextArea wasteArea;       // <--- ADD THIS LINE
    private TextField requestField;

    public Emp_DataEntry(Stage stage, firebaseAuth authService) { // <--- CHANGE THIS LINE
        this.stage = stage;
        this.authService = authService; // <--- ADD THIS LINE
        this.notification = new Notification(); // <--- ADD THIS LINE (Initialize Notification)
    }
    public void show() {
        VBox leftPane = new VBox(20);
        leftPane.setPrefWidth(1336 * 0.30);
        leftPane.setPadding(new Insets(20));
        leftPane.setStyle("-fx-background-color: #f0f0f0;");
        leftPane.setAlignment(Pos.TOP_CENTER);

        Label siteNameLabel = new Label("Site Name: Example Construction Site");
        siteNameLabel.setFont(Font.font("Arial", 20));
        siteNameLabel.setWrapText(true);

        Label mapPlaceholder = new Label("Google Maps (Placeholder)");
        mapPlaceholder.setPrefHeight(500);
        mapPlaceholder.setMaxWidth(Double.MAX_VALUE);
        mapPlaceholder.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #CCCCCC; -fx-border-width: 2;");
        mapPlaceholder.setFont(Font.font("Arial", 18));
        mapPlaceholder.setAlignment(Pos.CENTER);

        Button openAdvancedViewButton = new Button("Open Advanced View");
        openAdvancedViewButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 18; -fx-background-radius: 10;");
        openAdvancedViewButton.setMinHeight(50);
        openAdvancedViewButton.setMinWidth(250);

        openAdvancedViewButton.setOnAction(e -> {
        Emp_Maps_AdvancedView advancedView = new Emp_Maps_AdvancedView(stage);
        advancedView.show();
    });

        // Label siteDescriptionLabel = new Label("This site is responsible for residential building projects located in downtown. Material flow and employee count are critical.");
        // siteDescriptionLabel.setFont(Font.font("Arial", 14));
        // siteDescriptionLabel.setWrapText(true);

        leftPane.getChildren().addAll(siteNameLabel, mapPlaceholder, openAdvancedViewButton);

        VBox rightPane = new VBox(30);
        rightPane.setPrefWidth(1336 * 0.70);
        rightPane.setPadding(new Insets(40));
        rightPane.setStyle("-fx-background-color: #FFFFFF;");

        Label heading = new Label("Site Data Entry");
        heading.setFont(Font.font("Arial", 30));

        double fieldWidth = 500;
        double fieldHeight = 50;

Label employeesLabel = new Label("Number of Employees on Site:");
        employeesLabel.setFont(Font.font("Arial", 18));
        this.employeesField = new TextField();
        this.materialsArea = new TextArea();
        this.wasteArea = new TextArea();
        this.requestField = new TextField(); // You correctly initialize them with 'this.'

        employeesField.setPromptText("Enter number...");
        employeesField.setPrefWidth(fieldWidth);
        employeesField.setPrefHeight(fieldHeight);

        Label materialsLabel = new Label("Materials Used:");
        materialsLabel.setFont(Font.font("Arial", 18));
        // DELETE 'TextArea' keyword here:
        materialsArea.setPromptText("List materials used..."); // <--- CHANGE: Delete 'TextArea ' at start of this line
        materialsArea.setPrefWidth(fieldWidth);
        materialsArea.setPrefHeight(fieldHeight);

        Label wasteLabel = new Label("Waste Materials Generated:");
        wasteLabel.setFont(Font.font("Arial", 18));
        // DELETE 'TextArea' keyword here:
        wasteArea.setPromptText("Describe waste materials..."); // <--- CHANGE: Delete 'TextArea ' at start of this line
        wasteArea.setPrefWidth(fieldWidth);
        wasteArea.setPrefHeight(fieldHeight);

        Label requestLabel = new Label("Request Raw Material:");
        requestLabel.setFont(Font.font("Arial", 18));
        // DELETE 'TextField' keyword here:
        requestField.setPromptText("Enter material needed..."); // <--- CHANGE: Delete 'TextField ' at start of this line
        requestField.setPrefWidth(fieldWidth);
        requestField.setPrefHeight(fieldHeight);

        VBox formBox = new VBox(15,
                employeesLabel, employeesField,
                materialsLabel, materialsArea,
                wasteLabel, wasteArea,
                requestLabel, requestField
        );

        Button submitButton = new Button("Submit Data");
        submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 18; -fx-background-radius: 10;");
        submitButton.setMinHeight(50);
        submitButton.setMinWidth(250);

        submitButton.setOnAction(e -> {
            // 1. Get User ID and Token
            String userId = authService.getCurrentUserId();
            String idToken = authService.getCurrentUserToken();

            // Check if user is logged in
            if (userId == null || idToken == null) {
                notification.showNotification("Error", "Please sign in first to submit data.");
                return; // Stop if not logged in
            }

            // --- Get values from input fields ---
            String employeesText = employeesField.getText().trim();
            String materials = materialsArea.getText().trim();
            String waste = wasteArea.getText().trim();
            String request = requestField.getText().trim();

            // Basic Validation: Check if fields are empty
            if (employeesText.isEmpty() || materials.isEmpty() || waste.isEmpty() || request.isEmpty()) {
                notification.showNotification("Input Error", "Please fill in all data entry fields.");
                return; // Stop if any field is empty
            }

            int numEmployees;
            try {
                numEmployees = Integer.parseInt(employeesText);
                if (numEmployees <= 0) {
                    notification.showNotification("Input Error", "Number of employees must be a positive number.");
                    return; // Stop if not valid
                }
            } catch (NumberFormatException ex) {
                notification.showNotification("Input Error", "Please enter a valid number for Employees.");
                return; // Stop if not a number
            }
            // --- End getting values and basic validation ---


            // 2. Show that something is happening (and prevent multiple clicks)
            submitButton.setDisable(true); // Disable button
            notification.showNotification("Submitting Data", "Sending site data to Firebase...");

            // 3. Do the actual sending to Firebase in a separate thread
            new Thread(() -> {
                boolean success = false; // To track if sending worked
                try {
                    // Get current time for the record
                    String currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    // Generate a unique ID for this site entry document
                    String entryId = UUID.randomUUID().toString();

                    // --- Build the URL for Firestore ---
                    // Path: users/USER_ID/site_reports/ENTRY_ID
                    String collectionPath = "users/" + userId + "/site_reports"; // This creates a 'site_reports' subcollection under the user
                    String urlString = FIRESTORE_BASE_URL + "/" + collectionPath + "?documentId=" + entryId;

                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST"); // Use POST to create a new document
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Authorization", "Bearer " + idToken); // Authenticate with token
                    conn.setDoOutput(true);

                    // --- Create the JSON data for Firestore (with specific types) ---
                    JSONObject fields = new JSONObject();
                    fields.put("numEmployees", new JSONObject().put("integerValue", String.valueOf(numEmployees)));
                    fields.put("materialsUsed", new JSONObject().put("stringValue", materials));
                    fields.put("wasteGenerated", new JSONObject().put("stringValue", waste));
                    fields.put("materialRequest", new JSONObject().put("stringValue", request));
                    fields.put("timestamp", new JSONObject().put("timestampValue", currentTime + "Z"));
                    fields.put("submittedBy", new JSONObject().put("stringValue", userId));

                    JSONObject document = new JSONObject();
                    document.put("fields", fields);

                    String payload = document.toString();
                    System.out.println("DEBUG: Sending Site Data to Firestore: " + payload);

                    // Write the JSON payload to the connection
                    try (OutputStream os = conn.getOutputStream()) {
                        os.write(payload.getBytes("UTF-8"));
                    }

                    // 4. Get the response from Firebase
                    int responseCode = conn.getResponseCode();
                    System.out.println("DEBUG: Firestore Site Data Response Code: " + responseCode);

                    if (responseCode == 200) {
                        System.out.println("Site data submitted successfully to Firestore!");
                        success = true;
                    } else {
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"))) {
                            StringBuilder errorResponse = new StringBuilder();
                            String line;
                            while ((line = br.readLine()) != null) {
                                errorResponse.append(line);
                            }
                            System.err.println("Failed to submit site data. Error: " + errorResponse.toString());
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("An unexpected error occurred during site data submission: " + ex.getMessage());
                    ex.printStackTrace();
                }

                // 5. Update UI on the JavaFX Application Thread
                final boolean finalSuccess = success;
                Platform.runLater(() -> {
                    if (finalSuccess) {
                        notification.showNotification("Success!", "Site data submitted to Firebase.");
                        // Optional: Clear fields after successful submission
                        employeesField.clear();
                        materialsArea.clear();
                        wasteArea.clear();
                        requestField.clear();
                    } else {
                        notification.showNotification("Failed!", "Could not submit site data. Check console for details.");
                    }
                    submitButton.setDisable(false); // Re-enable the button
                });
            }).start(); // Start the new thread
        });

        HBox submitButtonBox = new HBox(submitButton);
        submitButtonBox.setAlignment(Pos.CENTER);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 10;");
        backButton.setMinHeight(50);
        backButton.setMinWidth(200);
        backButton.setOnAction(e -> new Emp_Dashboard(stage, authService).show());

        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setAlignment(Pos.BOTTOM_RIGHT);

        rightPane.getChildren().addAll(heading, formBox, submitButtonBox, backButtonBox);
        VBox.setVgrow(backButtonBox, Priority.ALWAYS);

        HBox mainLayout = new HBox(leftPane, rightPane);

        Scene scene = new Scene(mainLayout, 1336, 768);

        stage.setTitle("Data Entry Page");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
