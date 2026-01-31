package com.currencyexchange.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.currencyexchange.response.CurrencyApiResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Client for interacting with the ExchangeRate-API.
 */
public class CurrencyApiClient {
    private static final String API_ENDPOINT = "https://v6.exchangerate-api.com/v6/";
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private static final String API_KEY = "88d3e3ff63c567efee8baf01";

    /**
     * Converts an amount from one currency to another.
     *
     * @param amount        the amount to convert
     * @param fromCurrency  the source currency code (e.g., "JPY")
     * @param toCurrency    the target currency code (e.g., "USD")
     * @return the converted amount
     * @throws IOException if a networking error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public double convertCurrency(double amount, String fromCurrency, String toCurrency)
            throws IOException, InterruptedException {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        String url = API_ENDPOINT + API_KEY + "/latest/" + fromCurrency;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(
                request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            CurrencyApiResponse exchangeResponse = GSON.fromJson(
                    response.body(), CurrencyApiResponse.class);

            if (exchangeResponse.result.equals("success")) {
                double rate = getRateForCurrency(exchangeResponse, toCurrency);
                return amount * rate;
            }
        }
        throw new IOException("Failed to convert currency");
    }

    /**
     * Extracts the exchange rate for the specified currency from the API response.
     *
     * @param response     the {@link CurrencyApiResponse} from the API
     * @param currencyCode the target currency code
     * @return the exchange rate
     * @throws IllegalArgumentException if the currency code is not supported
     */
    private double getRateForCurrency(CurrencyApiResponse response, String currencyCode) {
        switch (currencyCode) {
            case "USD":
                return response.conversionRates.usd;
            case "EUR":
                return response.conversionRates.eur;
            case "GBP":
                return response.conversionRates.gbp;
            case "JPY":
                return response.conversionRates.jpy;
            case "CAD":
                return response.conversionRates.cad;
            case "AUD":
                return response.conversionRates.aud;
            case "SGD":
                return response.conversionRates.sgd;
            case "CNY":
                return response.conversionRates.cny;
            case "INR":
                return response.conversionRates.inr;
            default:
                throw new IllegalArgumentException("Unsupported currency: " + currencyCode);
        }
    }
}
