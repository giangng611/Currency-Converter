package com.currencyexchange.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.currencyexchange.response.CountryApiResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Client for interacting with the Country API.
 */
public class CountryApiClient {
    private static final String API_ENDPOINT = "https://api.country.is/";
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
            .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
            .build();                                     // builds and returns a HttpClient object

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * Sends a GET request to the Country API to retrieve the user's IP and country code.
     *
     * @return a {@link CountryApiResponse} object containing the user's IP and country
     * @throws IOException if a networking error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public CountryApiResponse getCountry() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(
                request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return GSON.fromJson(response.body(), CountryApiResponse.class);
        }
        throw new IOException("Country API request failed with status: " + response.statusCode());
    }
}
