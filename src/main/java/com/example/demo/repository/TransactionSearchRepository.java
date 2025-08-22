package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionSearchRepository extends JpaRepository<Transaction,Long> , JpaSpecificationExecutor<Transaction> {

}
