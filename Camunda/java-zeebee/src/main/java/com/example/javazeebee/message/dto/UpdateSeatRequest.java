package com.example.javazeebee.message.dto;

import lombok.Data;
@Data
public class UpdateSeatRequest {
    private String updatedByEmail;
    private Integer usedSeat;
}
