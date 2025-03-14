package com.example.Boutique_Final.exception;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(String message){ super(message);}
}