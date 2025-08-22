package com.example.demo.dto;
import java.time.LocalDate;

public class TransactionSearchRequest {

    private String transactionType;
    private String assetSymbol;
    private String assetName;
    private String assetType;
    private LocalDate fromDate;
    private LocalDate toDate;

    public TransactionSearchRequest(String transactionType, String assetName, String assetSymbol, String assetType, LocalDate fromDate,LocalDate toDate){

        this.transactionType=transactionType;
        this.assetSymbol=assetSymbol;
        this.assetName=assetName;
        this.assetType=assetType;
        this.fromDate=fromDate;
        this.toDate=toDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

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

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
}
