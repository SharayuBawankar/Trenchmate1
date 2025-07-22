package com.example.view.owner;


// All imports correctly placed at the top
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import org.json.JSONObject; // For creating JSON data

import com.example.Controller.Notification; // For showing notifications
import com.example.Dao.firebaseAuth; // For Firebase authentication details

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javafx.application.Platform; // For updating UI from background threads


public class Emp_Attendance {

    // 🔴 IMPORTANT: REPLACE THIS with your actual Firestore REST API URL from Firebase Step 1b
    // Example: "https://firestore.googleapis.com/v1/projects/your-project-id/databases/(default)/documents"
    private static final String FIRESTORE_BASE_URL = "https://firestore.googleapis.com/v1/projects/trenchmate1/databases/(default)/documents";

    private final Stage stage;
    private final firebaseAuth authService;
    private final Notification notification; // Declared here
    private TextField siteNameField;
    private ComboBox<String> shiftComboBox;
    private TextField hoursWorkedField;

    // Constructor: receives the stage and the authentication service
    public Emp_Attendance(Stage stage, firebaseAuth authService) {
        this.stage = stage;
        this.authService = authService;
        this.notification = new Notification(); // Initialized here
    }

    public void show() {
        VBox leftPane = new VBox();
        leftPane.setPrefWidth(1336 * 0.30);
        leftPane.setStyle("-fx-background-color: #f0f0f0;");
        leftPane.setPadding(new Insets(20));
        leftPane.setSpacing(20);

        Label previousEntriesLabel = new Label("Previous Attendance Entries");
        previousEntriesLabel.setFont(Font.font("Arial", 20));

        ListView<String> attendanceList = new ListView<>();
        attendanceList.getItems().addAll(
                "2025-07-19 : Present",
                "2025-07-18 : Present",
                "2025-07-17 : Leave",
                "2025-07-16 : Present"
        );

        attendanceList.setPrefHeight(600);

        leftPane.getChildren().addAll(previousEntriesLabel, attendanceList);
        VBox.setVgrow(attendanceList, Priority.ALWAYS);

        VBox rightPane = new VBox();
        rightPane.setPrefWidth(1336 * 0.70);
        rightPane.setStyle("-fx-background-color: #FFFFFF;");
        rightPane.setPadding(new Insets(20));
        rightPane.setSpacing(30);

        Label totalAttendanceLabel = new Label("Total Attendance: 42 Days");
        totalAttendanceLabel.setFont(Font.font("Arial", 28));
        totalAttendanceLabel.setStyle("-fx-background-color: #E0FFE0; -fx-padding: 20; -fx-border-color: #B2FFB2; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;");
        totalAttendanceLabel.setMaxWidth(Double.MAX_VALUE);
        totalAttendanceLabel.setAlignment(Pos.CENTER);

        Label leavesTakenLabel = new Label("Leaves Taken: 5 Days");
        leavesTakenLabel.setFont(Font.font("Arial", 28));
        leavesTakenLabel.setStyle("-fx-background-color: #FFE0E0; -fx-padding: 20; -fx-border-color: #FFB2B2; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;");
        leavesTakenLabel.setMaxWidth(Double.MAX_VALUE);
        leavesTakenLabel.setAlignment(Pos.CENTER);

        // --- New Input Fields for Details (UI Setup) ---
        Label detailsHeader = new Label("Enter Attendance Details:");
        detailsHeader.setFont(Font.font("Arial", 22));
        detailsHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");

        // Site Name Field
        Label siteNameLabel = new Label("Site Name:");
        siteNameLabel.setFont(Font.font("Arial", 16));
        siteNameField = new TextField();
        siteNameField.setPromptText("e.g., River Bridge Construction");
        siteNameField.setMaxWidth(400);

        // Shift ComboBox
        Label shiftLabel = new Label("Shift:");
        shiftLabel.setFont(Font.font("Arial", 16));
        shiftComboBox = new ComboBox<>();
        shiftComboBox.getItems().addAll("Morning (8AM-4PM)", "Evening (4PM-12AM)", "Night (12AM-8AM)");
        shiftComboBox.setPromptText("Select Shift");
        shiftComboBox.setMaxWidth(400);

        // Hours Worked Field
        Label hoursWorkedLabel = new Label("Hours Worked:");
        hoursWorkedLabel.setFont(Font.font("Arial", 16));
        hoursWorkedField = new TextField();
        hoursWorkedField.setPromptText("e.g., 8 (numbers only)");
        hoursWorkedField.setMaxWidth(150);

        // VBox to group the new input fields
        VBox detailInputsBox = new VBox(10,
                detailsHeader,
                siteNameLabel, siteNameField,
                shiftLabel, shiftComboBox,
                hoursWorkedLabel, hoursWorkedField
        );
        detailInputsBox.setPadding(new Insets(0, 0, 20, 0));

        Button markAttendanceButton = new Button("Mark Attendance");
        // Button styles set BEFORE setOnAction for clarity
        markAttendanceButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 20; -fx-background-radius: 10;");
        markAttendanceButton.setMinHeight(60);
        markAttendanceButton.setMinWidth(300);

        // --- Action when "Mark Attendance" button is clicked ---
        markAttendanceButton.setOnAction(e -> {
            // 1. Get User ID and Token (from the login you did earlier)
            String userId = authService.getCurrentUserId();
            String idToken = authService.getCurrentUserToken();

            // Check if we actually have a logged-in user
            if (userId == null || idToken == null) {
                notification.showNotification("Error", "Please sign in first to mark attendance.");
                return; // Stop here, cannot mark attendance
            }

            // --- Get values from the new input fields ---
            String siteName = siteNameField.getText().trim();
            String selectedShift = shiftComboBox.getValue(); // Gets the selected item from ComboBox
            String hoursWorkedText = hoursWorkedField.getText().trim();

            // Simple validation: Check if fields are empty
            if (siteName.isEmpty() || selectedShift == null || selectedShift.isEmpty() || hoursWorkedText.isEmpty()) {
                notification.showNotification("Input Error", "Please fill in all attendance details (Site, Shift, Hours).");
                return; // Stop here, do not proceed
            }

            int hoursWorked;
            try {
                hoursWorked = Integer.parseInt(hoursWorkedText); // Try to convert hours to a number
                if (hoursWorked <= 0) {
                    notification.showNotification("Input Error", "Hours worked must be a positive number.");
                    return; // Stop if not positive
                }
            } catch (NumberFormatException ex) {
                notification.showNotification("Input Error", "Please enter a valid number for Hours Worked.");
                return; // Stop if not a valid number
            }
            // --- End getting values from new fields ---

            // 2. Show that something is happening (and prevent multiple clicks)
            markAttendanceButton.setDisable(true); // Disable button now that input is validated
            notification.showNotification("Marking Attendance", "Sending data to Firebase...");

            // 3. Do the actual sending to Firebase in a separate thread
            // This is crucial to prevent your JavaFX UI from freezing
            new Thread(() -> {
                boolean success = false; // To track if the sending worked
                try {
                    // Get current time and date in ISO format for Firestore
                    String currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    // Generate a unique ID for this attendance record in Firestore
                    String attendanceId = UUID.randomUUID().toString();

                    // --- Build the URL to send data to Firestore ---
                    // It will go to: FIRESTORE_BASE_URL/users/USER_ID/attendance?documentId=UNIQUE_ID
                    String collectionPath = "users/" + userId + "/attendance";
                    String urlString = FIRESTORE_BASE_URL + "/" + collectionPath + "?documentId=" + attendanceId;

                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST"); // Use POST to create a new record in Firestore
                    conn.setRequestProperty("Content-Type", "application/json"); // We are sending JSON
                    conn.setRequestProperty("Authorization", "Bearer " + idToken); // Tell Firebase who you are!
                    conn.setDoOutput(true); // Allow sending data

                    // --- Create the data (JSON) for Firestore ---
                    // Firestore needs data in a specific "fields" object with data types
                    JSONObject fields = new JSONObject();
                    fields.put("status", new JSONObject().put("stringValue", "Present")); // "Present" as text
                    fields.put("timestamp", new JSONObject().put("timestampValue", currentTime + "Z")); // Timestamp
                    fields.put("markedBy", new JSONObject().put("stringValue", userId)); // User ID

                    // --- ADDED: New fields for attendance details from UI ---
                    fields.put("siteName", new JSONObject().put("stringValue", siteName)); // Site name as text
                    fields.put("shift", new JSONObject().put("stringValue", selectedShift)); // Selected shift as text
                    fields.put("hoursWorked", new JSONObject().put("integerValue", String.valueOf(hoursWorked))); // Hours as an integer
                    // --- END ADDED NEW FIELDS ---

                    JSONObject document = new JSONObject();
                    document.put("fields", fields); // All your data goes inside "fields"

                    String payload = document.toString();
                    System.out.println("DEBUG: Sending to Firestore: " + payload); // For checking in console

                    // Write the JSON data to Firebase
                    try (OutputStream os = conn.getOutputStream()) {
                        os.write(payload.getBytes("UTF-8"));
                    }

                    // 4. Get the response from Firebase (check if it worked)
                    int responseCode = conn.getResponseCode();
                    System.out.println("DEBUG: Firestore Response Code: " + responseCode); // For checking

                    if (responseCode == 200) { // If it's 200 OK, it worked!
                        System.out.println("Attendance marked successfully in Firestore!");
                        success = true;
                    } else {
                        // If there was an error, read what Firebase says
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"))) {
                            StringBuilder errorResponse = new StringBuilder();
                            String line;
                            while ((line = br.readLine()) != null) {
                                errorResponse.append(line);
                            }
                            System.err.println("Failed to mark attendance. Error: " + errorResponse.toString());
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("An unexpected error occurred during attendance marking: " + ex.getMessage());
                    ex.printStackTrace(); // Print full error details (useful for debugging)
                }

                // 5. Update the UI back on the main JavaFX thread
                // This is important because you can't change UI elements from other threads
                final boolean finalSuccess = success; // Make 'success' final for use in lambda
                Platform.runLater(() -> {
                    if (finalSuccess) {
                        notification.showNotification("Success!", "Attendance recorded in Firebase.");
                        // OPTIONAL: Update your attendance list on screen
                        attendanceList.getItems().add(0, java.time.LocalDate.now().toString() + " : Present (New)");
                    } else {
                        notification.showNotification("Failed!", "Could not record attendance. Check console for details.");
                    }
                    markAttendanceButton.setDisable(false); // Re-enable the button
                });
            }).start(); // Start the new thread
        });


        Button requestLeaveButton = new Button("Request Leave");
        requestLeaveButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 20; -fx-background-radius: 10;");
        requestLeaveButton.setMinHeight(60);
        requestLeaveButton.setMinWidth(300);

        HBox buttonsBox = new HBox(40, markAttendanceButton, requestLeaveButton);
        buttonsBox.setAlignment(Pos.CENTER);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 10;");
        backButton.setMinHeight(50);
        backButton.setMinWidth(200);
        backButton.setOnAction(e -> new Emp_Dashboard(stage, authService).show());

        VBox rightBottomBox = new VBox(backButton);
        rightBottomBox.setAlignment(Pos.BOTTOM_RIGHT);

        // Correct order of adding elements to rightPane
        rightPane.getChildren().addAll(totalAttendanceLabel, leavesTakenLabel, detailInputsBox, buttonsBox, rightBottomBox);
        VBox.setVgrow(rightBottomBox, Priority.ALWAYS);

        HBox mainLayout = new HBox(leftPane, rightPane);

        Scene scene = new Scene(mainLayout, 1336, 768);

        stage.setTitle("Attendance Page");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
