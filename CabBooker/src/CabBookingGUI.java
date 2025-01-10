import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.animation.*;
import javafx.util.Duration;
import java.text.DecimalFormat;
import java.util.Random;

public class CabBookingGUI extends Application {

    static class Cab {
        String cabType;
        double pricePerKm;

        Cab(String cabType, double pricePerKm) {
            this.cabType = cabType;
            this.pricePerKm = pricePerKm;
        }

        double calculateFare(double distance) {
            return pricePerKm * distance;
        }
    }

    static Cab[] availableCabs = {
            new Cab("Standard", 10.0),
            new Cab("Luxury", 20.0),
            new Cab("SUV", 15.0),
            new Cab("Economy", 8.0),
            new Cab("Minivan", 12.0),
            new Cab("Convertible", 25.0)
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create labels, combo box, text field, and buttons
        Label titleLabel = new Label("Nana's Cab Booking");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");

        Label cabLabel = new Label("Select Cab Type:");
        ComboBox<String> cabComboBox = new ComboBox<>();
        cabComboBox.getItems().addAll("Standard", "Luxury", "SUV", "Economy", "Minivan", "Convertible");
        cabComboBox.setStyle("-fx-font-size: 14px; -fx-background-color: #f0f0f0; -fx-border-color: #4CAF50;");

        Label distanceLabel = new Label("Enter Distance (in km):");
        TextField distanceTextField = new TextField();
        distanceTextField.setPrefHeight(40);  // Keep the height of the input box
        distanceTextField.setPrefWidth(120);  // Reduce width of the input box

        Button bookButton = new Button("Book Cab");
        Button quitButton = new Button("Quit");

        Label resultLabel = new Label();
        Label timeEstimateLabel = new Label();

        // Style for labels and buttons
        cabLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        distanceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        resultLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: green; -fx-font-weight: bold;");
        timeEstimateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: blue; -fx-font-weight: bold;");
        bookButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        quitButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px;");

        // Layout
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);  // Center elements in the layout
        layout.getChildren().addAll(titleLabel, cabLabel, cabComboBox, distanceLabel, distanceTextField, bookButton, timeEstimateLabel, resultLabel, quitButton);

        // Button actions
        bookButton.setOnAction(e -> {
            String selectedCabType = cabComboBox.getValue();
            if (selectedCabType != null && !distanceTextField.getText().isEmpty()) {
                try {
                    double distance = Double.parseDouble(distanceTextField.getText());
                    Cab selectedCab = getSelectedCab(selectedCabType);
                    if (selectedCab != null) {
                        double fare = selectedCab.calculateFare(distance);
                        DecimalFormat df = new DecimalFormat("$#,###.00");

                        // Show booking confirmation in a popup
                        Stage popup = new Stage();
                        popup.initModality(Modality.APPLICATION_MODAL);
                        popup.setTitle("Booking Confirmation");

                        VBox popupLayout = new VBox(10);
                        popupLayout.setPadding(new Insets(20));
                        popupLayout.setAlignment(Pos.CENTER);
                        Label bookingDetails = new Label("Booking confirmed!\nCab: " + selectedCab.cabType + "\nFare: " + df.format(fare));
                        popupLayout.getChildren().addAll(bookingDetails);

                        Button closePopupButton = new Button("Close");
                        closePopupButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                        closePopupButton.setOnAction(closeEvent -> popup.close());

                        popupLayout.getChildren().add(closePopupButton);
                        Scene popupScene = new Scene(popupLayout, 250, 150);
                        popup.setScene(popupScene);
                        popup.show();

                        // Random time estimate for cab arrival
                        int estimatedTime = new Random().nextInt(15) + 5;  // Random time between 5 and 20 minutes
                        startCountdown(timeEstimateLabel, estimatedTime);
                    }
                } catch (NumberFormatException ex) {
                    resultLabel.setText("Please enter a valid distance.");
                }
            } else {
                resultLabel.setText("Please select a cab and enter a distance.");
            }
        });

        quitButton.setOnAction(e -> {
            primaryStage.close();  // Close the application
        });

        // Set up the stage and scene with a larger window
        Scene scene = new Scene(layout, 500, 400);  // Bigger window size
        primaryStage.setTitle("Nana's Cab Booking");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Cab getSelectedCab(String cabType) {
        for (Cab cab : availableCabs) {
            if (cab.cabType.equals(cabType)) {
                return cab;
            }
        }
        return null;
    }

    private void startCountdown(Label timeEstimateLabel, int initialTime) {
        final int[] timeRemaining = {initialTime};

        // Update the label with the remaining time
        Timeline countdown = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (timeRemaining[0] > 0) {
                timeEstimateLabel.setText("Cab arriving in " + timeRemaining[0] + " minutes.");
                timeRemaining[0]--;
            } else {
                timeEstimateLabel.setText("Cab has arrived!");
            }
        }));
        countdown.setCycleCount(Timeline.INDEFINITE);
        countdown.play();
    }
}
