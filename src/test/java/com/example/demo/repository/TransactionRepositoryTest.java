package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import com.example.demo.entity.AssetType;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.TransactionType;
import com.example.demo.repository.TransactionRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class TransactionRepositoryTest {
    
    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void testFindAll() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertNotNull(transactions);
    }
    
    @Test
    public void testSave() {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.BUY);
        transaction.setAssetSymbol("AAPL");
        transaction.setAssetName("Apple Inc.");
        transaction.setAssetType(AssetType.STOCK);
        transaction.setQuantity(new BigDecimal("10"));
        transaction.setPricePerUnit(new BigDecimal("150.00"));
        transaction.setTotalAmount(new BigDecimal("1500.00"));
        transaction.setCurrency("USD");
        transaction.setExchange("NASDAQ");  
        transaction.setCreatedAt(java.time.LocalDate.now());
        transaction.setDate(java.time.LocalDate.now());
        Transaction savedTransaction = transactionRepository.save(transaction);
        assertNotNull(savedTransaction);

    }
}