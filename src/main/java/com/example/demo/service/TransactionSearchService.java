package com.example.demo.service;

import com.example.demo.dto.TransactionSearchRequest;
import com.example.demo.entity.Transaction;
import jakarta.persistence.criteria.Predicate;
import com.example.demo.exception.TransactionSearchNotFoundException;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.TransactionSearchRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionSearchService {

    private final TransactionSearchRepository transactionSearchRepository;
    private final TransactionRepository transactionRepository;

    public TransactionSearchService(TransactionSearchRepository transactionSearchRepository,TransactionRepository transactionRepository) {
        this.transactionSearchRepository = transactionSearchRepository;
        this.transactionRepository=transactionRepository;
    }

    public List<Transaction> searchTransactions(TransactionSearchRequest transactionSearchRequest) {

        boolean hasAnyField =
                (transactionSearchRequest.getTransactionType() != null && !transactionSearchRequest.getTransactionType().isBlank()) ||
                        (transactionSearchRequest.getAssetSymbol() != null && !transactionSearchRequest.getAssetSymbol().isBlank()) ||
                        (transactionSearchRequest.getAssetName() != null && !transactionSearchRequest.getAssetName().isBlank()) ||
                        (transactionSearchRequest.getAssetType() != null && !transactionSearchRequest.getAssetType().isBlank()) ||
                        (transactionSearchRequest.getFromDate() != null) ||
                        (transactionSearchRequest.getToDate() != null);

        if (!hasAnyField) {
            return transactionRepository.findAll();
        }
        // Build Specification dynamically if at least one field is provided
        Specification<Transaction> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (transactionSearchRequest.getTransactionType() != null && !transactionSearchRequest.getTransactionType().isBlank()) {
                predicates.add(cb.equal(root.get("transactionType"), transactionSearchRequest.getTransactionType()));
            }
            if (transactionSearchRequest.getAssetSymbol() != null && !transactionSearchRequest.getAssetSymbol().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("assetSymbol")),
                        "%" + transactionSearchRequest.getAssetSymbol().toLowerCase() + "%"));
            }
            if (transactionSearchRequest.getAssetName() != null && !transactionSearchRequest.getAssetName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("assetName")),
                        "%" + transactionSearchRequest.getAssetName().toLowerCase() + "%"));
            }
            if (transactionSearchRequest.getAssetType() != null && !transactionSearchRequest.getAssetType().isBlank()) {
                predicates.add(cb.equal(root.get("assetType"), transactionSearchRequest.getAssetType()));
            }
            if (transactionSearchRequest.getFromDate() != null && transactionSearchRequest.getToDate() != null) {
                predicates.add(cb.between(root.get("createdAt"),
                        transactionSearchRequest.getFromDate(), transactionSearchRequest.getToDate()));
            } else if (transactionSearchRequest.getFromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), transactionSearchRequest.getFromDate()));
            } else if (transactionSearchRequest.getToDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), transactionSearchRequest.getToDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

       return transactionSearchRepository.findAll(spec);


    }
}
