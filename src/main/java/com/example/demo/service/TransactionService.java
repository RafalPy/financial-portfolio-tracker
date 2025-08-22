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
        // ...other business logic...
        return transactionRepository.save(transaction);
    }


    private BigDecimal fetchPriceFromApi(String symbol, LocalDate date) {
        String startDate = date.minusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE);
        String endDate = date.plusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE);
        String url = String.format(
            "https://api.twelvedata.com/time_series?symbol=%s&interval=1day&start_date=%s&end_date=%s&apikey=%s",
            symbol, startDate, endDate, apiKey
        );

        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);

        if (json.has("values")) {
            JSONArray values = json.getJSONArray("values");

            LocalDate closestDate = null;
            BigDecimal closestPrice = null;
            long minDiff = Long.MAX_VALUE;

            for (int i = 0; i < values.length(); i++) {
                JSONObject dayData = values.getJSONObject(i);
                LocalDate day = LocalDate.parse(dayData.getString("datetime"));
                long diff = Math.abs(day.toEpochDay() - date.toEpochDay());

                if (diff < minDiff) {
                    minDiff = diff;
                    closestDate = day;
                    closestPrice = new BigDecimal(dayData.getString("close"));
                }
            }

            if (closestPrice != null) {
                return closestPrice;
            }
        }

        throw new RuntimeException("Price not found for symbol: " + symbol + " near date: " + date);
    }

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }
}