package com.example.javazeebee.start.dto;

import lombok.Data;

import java.util.Map;

@Data
public class StartRequest {
    private String processId;
    private Map<String, Object> variables;
}
