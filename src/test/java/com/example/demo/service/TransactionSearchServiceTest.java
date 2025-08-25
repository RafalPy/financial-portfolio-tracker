package com.example.demo.service;

import com.example.demo.dto.TransactionSearchRequest;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.TransactionSearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionSearchServiceTest {

    @Mock
    private TransactionSearchRepository transactionSearchRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionSearchService transactionSearchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchTransactions_NoFieldsProvided_ReturnsAllTransactions() {
        // Arrange
        TransactionSearchRequest request = new TransactionSearchRequest(
            null, null, null, null, null, null
        );
        List<Transaction> mockTransactions = List.of(new Transaction(), new Transaction());
        when(transactionRepository.findAll()).thenReturn(mockTransactions);

        // Act
        List<Transaction> result = transactionSearchService.searchTransactions(request);

        // Assert
        assertEquals(mockTransactions.size(), result.size());
        verify(transactionRepository, times(1)).findAll();
        verify(transactionSearchRepository, never()).findAll(any(Specification.class));
    }

    @Test
    void testSearchTransactions_WithFieldsProvided_ReturnsFilteredTransactions() {
        // Arrange
        TransactionSearchRequest request = new TransactionSearchRequest(
            "BUY",
            "Apple Inc.",
            "AAPL",
            "STOCK",
            LocalDate.of(2023, 1, 1),
            LocalDate.of(2023, 12, 31)
        );

        List<Transaction> mockTransactions = List.of(new Transaction());
        when(transactionSearchRepository.findAll(any(Specification.class))).thenReturn(mockTransactions);

        // Act
        List<Transaction> result = transactionSearchService.searchTransactions(request);

        // Assert
        assertEquals(mockTransactions.size(), result.size());
        verify(transactionSearchRepository, times(1)).findAll(any(Specification.class));
        verify(transactionRepository, never()).findAll();
    }

    @Test
    void testSearchTransactions_NoResultsFound_ReturnsEmptyList() {
        // Arrange
        TransactionSearchRequest request = new TransactionSearchRequest(
            "SELL",
            null,
            null,
            null,
            null,
            null
        );
        when(transactionSearchRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        // Act
        List<Transaction> result = transactionSearchService.searchTransactions(request);

        // Assert
        assertEquals(0, result.size());
        verify(transactionSearchRepository, times(1)).findAll(any(Specification.class));
        verify(transactionRepository, never()).findAll();
    }
}