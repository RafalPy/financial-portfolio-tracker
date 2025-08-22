package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.AssetType;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.TransactionType;
import com.example.demo.repository.TransactionRepository;

import org.json.JSONArray;
import org.json.JSONObject;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;





@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    Dotenv dotenv = Dotenv.load();
    String apiKey = dotenv.get("TWELVE_API_KEY");
    private final RestTemplate restTemplate = new RestTemplate();
    // Inject RestTemplate/WebClient if needed

    public Transaction createTransaction(Transaction transaction) {
        // Fetch price from external API using assetSymbol and transaction.getDate()
        BigDecimal price = fetchPriceFromApi(transaction.getAssetSymbol(), transaction.getDate());
        transaction.setPricePerUnit(price);
        // ...other business logic.............................
        return transactionRepository.save(transaction);
    }

    // API DOCS https://support.twelvedata.com/en/articles/5214728-getting-historical-data
    private BigDecimal fetchPriceFromApi(String symbol, LocalDate date) {
        // Example API call to fetch only last historical price - for given date (day)
        String endDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String outputSize = "1"; // Fetch only 1 record to minimize data transfer
        String url = String.format(
            "https://api.twelvedata.com/time_series?symbol=%s&interval=1min&outputsize=%s&end_date=%s&apikey=%s",
            symbol, outputSize, endDate, apiKey
        );

        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);

        // Check if the response contains the "values" array
        if (json.has("values")) {
            JSONArray values = json.getJSONArray("values");

            // Extract the first (and only) record from the "values" array
            if (values.length() > 0) {
                JSONObject dayData = values.getJSONObject(0);
                return new BigDecimal(dayData.getString("close"));
            }
        }

        // Throw an exception if the price is not found
        throw new RuntimeException("Price not found for symbol: " + symbol + " on or near date: " + date);
        }

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }
}