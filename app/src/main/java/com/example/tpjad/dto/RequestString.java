package com.example.tpjad.dto;

public class RequestString {
    private String payload;

    public RequestString() {
    }

    public RequestString(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
