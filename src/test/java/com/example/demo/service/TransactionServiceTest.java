package com.example.demo.service;

import com.example.demo.entity.AssetType;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.TransactionType;
import com.example.demo.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        Transaction transaction = new Transaction();
        when(transactionRepository.findAll()).thenReturn(Collections.singletonList(transaction));
        List<Transaction> result = transactionService.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void testCreateTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.BUY); // or any valid enum value
        transaction.setAssetSymbol("AAPL");
        transaction.setAssetName("Apple Inc.");
        transaction.setAssetType(AssetType.STOCK); // or any valid enum value
        transaction.setQuantity(new BigDecimal("10.5"));
        transaction.setPricePerUnit(new BigDecimal("150.25"));
        transaction.setTotalAmount(new BigDecimal("1577.63"));
        transaction.setCurrency("USD");
        transaction.setExchange("NASDAQ");
        transaction.setCreatedAt(LocalDate.now());
        transaction.setDate(LocalDate.of(2024, 8, 20));
        // Set other required fields...

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction saved = transactionService.createTransaction(transaction);
        assertNotNull(saved);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}