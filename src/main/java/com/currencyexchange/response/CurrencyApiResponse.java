package com.currencyexchange.response;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the exchange API response.
 */
public class CurrencyApiResponse {
    public String result;

    @SerializedName("base_code")
    public String baseCode;

    @SerializedName("conversion_rates")
    public ConversionRates conversionRates;

    /**
     * A nested class representing individual conversion rates to other currencies.
     */
    public static class ConversionRates {
        @SerializedName("USD")
        public double usd;
        @SerializedName("EUR")
        public double eur;
        @SerializedName("GBP")
        public double gbp;
        @SerializedName("JPY")
        public double jpy;
        @SerializedName("CAD")
        public double cad;
        @SerializedName("AUD")
        public double aud;
        @SerializedName("SGD")
        public double sgd;
        @SerializedName("CNY")
        public double cny;
        @SerializedName("INR")
        public double inr;
    }
}
