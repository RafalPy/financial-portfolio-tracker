package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.PortfolioHolding;
import com.example.demo.entity.AssetType;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.TransactionType;
import com.example.demo.repository.TransactionRepository;

import org.json.JSONArray;
import org.json.JSONObject;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;





@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    Dotenv dotenv = Dotenv.load();
    String apiKey = dotenv.get("TWELVE_API_KEY");
    private final RestTemplate restTemplate = new RestTemplate();
    // Inject RestTemplate/WebClient if needed

    public Transaction createTransaction(Transaction transaction) {
        // Clean input symbol
        transaction.setAssetSymbol(transaction.getAssetSymbol().toUpperCase().trim());
        // Fetch price from external API using assetSymbol and transaction.getDate()
        BigDecimal price = fetchPriceFromApi(transaction.getAssetSymbol(), transaction.getDate());
        transaction.setPricePerUnit(price);
        if (transaction.getAssetName() == null || transaction.getAssetName().isEmpty()) {
            transaction.setAssetName(transaction.getAssetSymbol()); // Default to symbol if name not provided
        }
        // ...other business logic...
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


    public List<PortfolioHolding> getPortfolioHoldings() {
        List<Transaction> transactions = transactionRepository.findAll();

        Map<String, PortfolioHolding> holdingsMap = new HashMap<>();

        for (Transaction transaction : transactions) {
            String assetSymbol = transaction.getAssetSymbol();
            BigDecimal quantity = transaction.getQuantity();
            BigDecimal pricePerUnit = transaction.getPricePerUnit();

            if (transaction.getTransactionType() == TransactionType.SELL) {
                quantity = quantity.negate();
            }

            PortfolioHolding holding = holdingsMap.getOrDefault(assetSymbol, new PortfolioHolding(
                    assetSymbol,
                    transaction.getAssetName(),
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
            ));

            BigDecimal newTotalQuantity = holding.getTotalQuantity().add(quantity);
            BigDecimal newAveragePrice = holding.getAveragePrice();

            if (quantity.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal totalCost = holding.getAveragePrice().multiply(holding.getTotalQuantity())
                        .add(pricePerUnit.multiply(quantity));
                newAveragePrice = totalCost.divide(newTotalQuantity, BigDecimal.ROUND_HALF_UP);
            }

            holding.setTotalQuantity(newTotalQuantity);
            holding.setAveragePrice(newAveragePrice);

            holdingsMap.put(assetSymbol, holding);
        }

        return holdingsMap.values().stream()
                .filter(holding -> holding.getTotalQuantity().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
    }

}