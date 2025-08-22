package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import com.example.demo.entity.Transaction; // 
import com.example.demo.repository.TransactionRepository; // 

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
}