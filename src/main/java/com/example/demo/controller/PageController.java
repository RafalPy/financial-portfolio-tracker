package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.dto.PortfolioHolding;
import com.example.demo.entity.Transaction;
import com.example.demo.service.TransactionService;

@Controller
public class PageController {

    private final TransactionService transactionService;

    public PageController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public String indexPage(Model model) {
        List<Transaction> transactions = transactionService.getAll();

        model.addAttribute("transactions", transactions);
        List<PortfolioHolding> portfolioHoldings = transactionService.getPortfolioHoldings();
        model.addAttribute("portfolioHoldings", portfolioHoldings);
        // Calculate total portfolio value
        double totalValue = portfolioHoldings.stream()
        .mapToDouble(holding -> holding.getTotalQuantity().multiply(holding.getAveragePrice()).doubleValue())
        .sum();
        model.addAttribute("totalValue", totalValue);
        int portfolioHoldingsCount = portfolioHoldings.size();
        model.addAttribute("portfolioHoldingsCount", portfolioHoldingsCount);

        Transaction latestTransaction = transactions.isEmpty() ? null : transactions.get(transactions.size() - 1);
        model.addAttribute("latestTransaction", latestTransaction);
        
        model.addAttribute("content", "dashboard");
        return "layout";
    }

    @PostMapping("/")
    public String createTransactionForm(@ModelAttribute Transaction transaction) {
        if (transaction.getDate() == null) {
        transaction.setDate(LocalDate.now()); // Set the current date if null
        }
        transactionService.createTransaction(transaction);
        return "redirect:/"; // Redirect or return a view name
    }

}
