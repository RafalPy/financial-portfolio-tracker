package com.example.demo.controller;

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
        model.addAttribute("transactions", transactionService.getAll());
        List<PortfolioHolding> portfolioHoldings = transactionService.getPortfolioHoldings();
        model.addAttribute("portfolioHoldings", portfolioHoldings);
        // Calculate total portfolio value
        double totalValue = portfolioHoldings.stream()
        .mapToDouble(holding -> holding.getTotalQuantity().multiply(holding.getAveragePrice()).doubleValue())
        .sum();
        model.addAttribute("totalValue", totalValue);
        return "index";
    }

    @PostMapping("/")
    public String createTransactionForm(@ModelAttribute Transaction transaction) {
        transactionService.createTransaction(transaction);
        return "redirect:/"; // Redirect or return a view name
    }

}
