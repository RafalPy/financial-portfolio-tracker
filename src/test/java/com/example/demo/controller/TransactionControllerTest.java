package com.example.demo.controller;

import com.example.demo.entity.Transaction;
import com.example.demo.entity.TransactionType;
import com.example.demo.entity.AssetType;
import com.example.demo.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    void testGetAllTransactions() throws Exception {
        Transaction t = new Transaction();
        t.setTransactionId(1L);
        t.setTransactionType(TransactionType.BUY);
        t.setAssetSymbol("AAPL");
        t.setAssetName("Apple Inc.");
        t.setAssetType(AssetType.STOCK);
        t.setQuantity(new BigDecimal("10.5"));
        t.setPricePerUnit(new BigDecimal("150.25"));
        t.setTotalAmount(new BigDecimal("1577.63"));
        t.setCurrency("USD");
        t.setExchange("NASDAQ");
        t.setCreatedAt(LocalDate.now());
        t.setDate(LocalDate.now());

        Mockito.when(transactionService.getAll()).thenReturn(Arrays.asList(t));

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].assetSymbol").value("AAPL"));
    }

    @Test
    void testCreateTransaction() throws Exception {
        Transaction t = new Transaction();
        t.setTransactionId(1L);
        t.setTransactionType(TransactionType.BUY);
        t.setAssetSymbol("AAPL");
        t.setAssetName("Apple Inc.");
        t.setAssetType(AssetType.STOCK);
        t.setQuantity(new BigDecimal("10.5"));
        t.setPricePerUnit(new BigDecimal("150.25"));
        t.setTotalAmount(new BigDecimal("1577.63"));
        t.setCurrency("USD");
        t.setExchange("NASDAQ");
        t.setCreatedAt(LocalDate.now());
        t.setDate(LocalDate.now());

        Mockito.when(transactionService.createTransaction(any(Transaction.class))).thenReturn(t);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("transactionType", "BUY")
                .param("assetSymbol", "AAPL")
                .param("assetName", "Apple Inc.")
                .param("assetType", "STOCK")
                .param("quantity", "10.5")
                .param("pricePerUnit", "150.25")
                .param("totalAmount", "1577.63")
                .param("currency", "USD")
                .param("exchange", "NASDAQ")
                .param("createdAt", LocalDate.now().toString())
                .param("date", LocalDate.now().toString())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assetSymbol").value("AAPL"));
    }
}
