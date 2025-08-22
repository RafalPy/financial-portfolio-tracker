package com.example.demo.exception;

public class TransactionSearchNotFoundException extends RuntimeException{

    public TransactionSearchNotFoundException(String message){
        super(message);
    }
}
