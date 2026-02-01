# Currency Converter (JavaFX)

A JavaFX-based desktop application that detects the user’s country via IP and converts currencies using real-time exchange rates from external APIs.  
The app communicates with REST APIs over HTTP, parses JSON responses using Gson, and also reads local JSON files to process and display additional information.

---

## Features

- JavaFX graphical user interface (desktop app)
- Detects user country based on IP address
- Fetches real-time exchange rates from an online API
- Converts between currencies based on user selection and input amount
- Uses **Gson** to parse JSON responses from HTTP requests
- Uses local **JSON file(s)** for handling or displaying additional data

---

## Tech Stack

- **Java**
- **JavaFX** (UI)
- **HTTP** networking (REST API communication)
- **Gson** (JSON parsing)
- **JSON file processing**

---

## APIs Used

### Exchange Rate API  
**Website:** https://www.exchangerate-api.com/  
**Purpose:** Provides real-time currency exchange rates used for conversion.

### Country/IP API  
**Website:** https://api.country.is/  
**Purpose:** Returns the user's IP address and related country information, which the app uses for location-based features or defaults.

---

## Dependencies

This project uses:

- **Gson** — to extract and map data from JSON responses returned by APIs.

If using Maven, Gson should be listed in `pom.xml`.  
If not using Maven, ensure `gson.jar` is added to the project libraries.

---

## Getting Started

### Clone the repository

```bash
git clone https://github.com/giangng611/Currency-Converter.git
cd Currency-Converter
```

---

### Set up JavaFX

Make sure JavaFX is installed and properly configured in your IDE.

### Run the application

1. Open the project in your IDE  
2. Locate the class that contains:

```java
public static void main(String[] args)
```

3. Run that file to start the JavaFX application

---

## API Key Setup (Exchange Rate API)

The Exchange Rate API may require an API key depending on the endpoint used.

For security, do **not** hardcode your API key directly in the source code. Instead, store it as an environment variable:

```bash
export EXCHANGE_RATE_API_KEY="your_api_key_here"
```

Then access it in Java:

```java
String apiKey = System.getenv("EXCHANGE_RATE_API_KEY");
```

---

## How the Application Works

### Step 1 — Detect User Country
- The app sends an HTTP request to: `https://api.country.is/`
- The API returns JSON containing the user's IP and country information
- Gson parses this JSON to extract required fields

### Step 2 — Fetch Exchange Rates
- The app sends an HTTP request to the Exchange Rate API
- The response contains exchange rate data in JSON format
- Gson extracts conversion rates from the response

### Step 3 — Convert Currency
- User selects base currency, target currency, and enters an amount
- The app applies the fetched exchange rate
- The converted result is displayed in the JavaFX UI

### Step 4 — Process Local JSON Data
- The app reads one or more local JSON files
- These files are used to support internal data handling, mappings, or display information

---

## Example User Flow

1. Launch the application  
2. App automatically detects your country using IP  
3. Choose base and target currencies  
4. Enter an amount  
5. View the converted result instantly  

---

## Notes

- Public APIs may have rate limits — handle failed or slow requests gracefully
- Always validate user input (no empty, negative, or invalid numbers)
- Consider caching exchange rates to reduce repeated API calls

---

## Future Improvements

- Add caching and refresh intervals for exchange rates
- Improve UI feedback when API requests fail
- Support offline mode using stored exchange rate data
- Add unit tests for conversion logic
- Enhance UI with better formatting and currency symbols
