package com.currencyexchange.ui;

import com.currencyexchange.client.CountryApiClient;
import com.currencyexchange.client.CurrencyApiClient;
import com.currencyexchange.response.CountryApiResponse;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import java.text.DecimalFormat;

/**
 * Currency converter based on IP location without using Map classes.
 */
public class AppUI extends Application {
    private Stage stage;
    private Scene scene;
    private VBox root;
    private VBox content;
    private TextField amountField;
    private ComboBox<String> currencyComboBox;
    private Button convertButton;
    private Label resultLabel;
    private ProgressIndicator progressIndicator;
    private CountryApiClient countryApiClient;
    private CurrencyApiClient currencyApiClient;
    private DecimalFormat priceFormat = new DecimalFormat("#,##0.00");

    /**
     * Constructor of the app.
     *
     */
    public AppUI() {
        root = new VBox(10);
        countryApiClient = new CountryApiClient();
        currencyApiClient = new CurrencyApiClient();
    }

    /**
     * Starts the JavaFX application.
     *
     * @param stage the primary stage for this application
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        setupUI();
        scene = new Scene(root);
        stage.setTitle("IP-Based Currency Converter");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.show();
        stage.sizeToScene();
    }

    /**
     * Constructs and styles the main UI layout.
     */
    private void setupUI() {
        // Title label
        Label titleLabel = new Label("IP-Based Currency Converter");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        titleLabel.setMaxWidth(500);

        // Form components
        GridPane formPane = new GridPane();
        formPane.setHgap(10);
        formPane.setVgap(10);
        formPane.setPadding(new Insets(10));
        formPane.setAlignment(Pos.CENTER);

        amountField = new TextField();
        amountField.setPromptText("Amount in your local currency");
        amountField.setPrefWidth(70);

        currencyComboBox = new ComboBox<>();
        currencyComboBox.getItems().addAll("USD", "EUR", "GBP", "JPY",
                "CAD", "INR", "AUD", "SGD", "CNY");
        currencyComboBox.setValue("USD");

        formPane.add(new Label("Amount:"), 0, 0);
        formPane.add(amountField, 1, 0);
        formPane.add(new Label("Convert to:"), 0, 1);
        formPane.add(currencyComboBox, 1, 1);

        // Convert button
        convertButton = new Button("Convert Currency");
        convertButton.setOnAction(e -> handleConvert());
        convertButton.setStyle("-fx-background-color: #3498db;-fx-text-fill: white");

        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setPrefSize(30, 30);

        resultLabel = new Label();
        resultLabel.setWrapText(true);
        resultLabel.setMaxWidth(400);
        resultLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #2d3436;");
        resultLabel.setAlignment(Pos.CENTER);

        root.getChildren().addAll(titleLabel, formPane, convertButton,
                progressIndicator, resultLabel);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(5));
    }

    /**
     * Handles the currency conversion process when the user clicks the button.
     * It retrieves the user's country using the Country API, determines the
     * local currency, and converts the entered amount to the selected currency.
     */
    private void handleConvert() {
        String amountText = amountField.getText().trim();
        String targetCurrency = currencyComboBox.getValue();

        if (amountText.isEmpty()) {
            showAlert("Error", "Please enter the amount.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid amount format.");
            return;
        }

        convertButton.setDisable(true);
        progressIndicator.setVisible(true);
        resultLabel.setText("Detecting location and converting...");

        new Thread(() -> {
            try {
                CountryApiResponse countryResponse = countryApiClient.getCountry();
                String countryCode = countryResponse.country;
                String sourceCurrency = getCurrencyFromCountry(countryCode);
                String ipAddress = countryResponse.ip;
                double convertedAmount = currencyApiClient.convertCurrency(
                        amount, sourceCurrency, targetCurrency);

                Platform.runLater(() -> {
                    String resultText = String.format(
                            "Detected IP: %s\n" +
                                    "Detected Country: %s\n" +
                                    "Local Currency: %s\n" +
                                    "Amount: %s %s\n" +
                                    "Converted: %s %s",
                            ipAddress,
                            countryCode,
                            sourceCurrency,
                            priceFormat.format(amount),
                            sourceCurrency,
                            priceFormat.format(convertedAmount),
                            targetCurrency);

                    resultLabel.setText(resultText);
                    convertButton.setDisable(false);
                    progressIndicator.setVisible(false);
                    stage.sizeToScene();
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    resultLabel.setText("Error: " + e.getMessage());
                    convertButton.setDisable(false);
                    progressIndicator.setVisible(false);
                });
            }
        }).start();
    }

    /**
     * Maps a country code to its corresponding local currency.
     *
     * @param countryCode a two-letter ISO country code (e.g., "US", "JP")
     * @return the ISO currency code (e.g., "USD", "JPY")
     */
    private String getCurrencyFromCountry(String countryCode) {
        if (countryCode.equals("US")) {
            return "USD"; // USA
        } else if (countryCode.equals("DE") || countryCode.equals("FR")) {
            return "EUR"; // Europe
        } else if (countryCode.equals("IN")) {
            return "INR"; // India
        } else if (countryCode.equals("JP")) {
            return "JPY"; // Japan
        } else if (countryCode.equals("GB")) {
            return "GBP"; // Great Britian
        } else if (countryCode.equals("CA")) {
            return "CAD"; // Canada
        } else if (countryCode.equals("AU")) {
            return "AUD"; // Australia
        } else if (countryCode.equals("SG")) {
            return "SGD"; // Singapore
        } else if (countryCode.equals("CN")) {
            return "CNY"; // China (Chinese Yuan)
        } else {
            return "USD"; // default fallback
        }
    }

    /**
     * Displays an alert dialog with a given title and message.
     *
     * @param title   the title of the alert window
     * @param message the message shown in the alert dialog
     */
    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    } // showAlert
} // ApiApp
