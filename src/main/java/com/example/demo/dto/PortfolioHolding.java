package com.example.demo.dto;

import java.math.BigDecimal;

public class PortfolioHolding {
    private String assetSymbol;
    private String assetName;
    private BigDecimal totalQuantity;
    private BigDecimal averagePrice;

    public PortfolioHolding(String assetSymbol, String assetName, BigDecimal totalQuantity, BigDecimal averagePrice) {
        this.assetSymbol = assetSymbol;
        this.assetName = assetName;
        this.totalQuantity = totalQuantity;
        this.averagePrice = averagePrice;
        // Optionally, you can add more fields etc.
    }

    // Getters and Setters
    public String getAssetSymbol() {
        return assetSymbol;
    }

    public void setAssetSymbol(String assetSymbol) {
        this.assetSymbol = assetSymbol;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }
}