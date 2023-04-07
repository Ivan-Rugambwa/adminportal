package com.example.javazeebee.message.dto;

import lombok.Data;

@Data
public class PublishRequest {
    private String email;
    private String month;
    private String year;
    private String message;
}
