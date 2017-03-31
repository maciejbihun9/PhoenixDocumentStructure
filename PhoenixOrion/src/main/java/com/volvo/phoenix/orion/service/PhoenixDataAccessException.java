package com.volvo.phoenix.orion.service;

public class PhoenixDataAccessException extends Exception {


    private static final long serialVersionUID = 1366451415161571661L;


    public PhoenixDataAccessException(Exception e) {
        super(e);
    }


    public PhoenixDataAccessException(String message) {
        super(message);
    }
}
