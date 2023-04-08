package com.example.javazeebee.message.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PublishRequest {
    private UUID seatUuid;
    private String email;
    private String business;
    private String forYearMonth;
    private String message;
    private Integer amountOfSeatsUsed;
}
