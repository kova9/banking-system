package com.bankingSystem.bankingSystem.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class SearchDto {
    private String receiverId;
    private String senderId;
    private String message;
    private BigDecimal minAmout;
    private BigDecimal maxAmout;
    private String currencyId;
    private Timestamp startDate;
    private Timestamp endDate;

    public static SearchDto fromJson(JsonNode json) {
        if (json != null) {
            return new Gson().fromJson(json.toString(), SearchDto.class);
        } else {
            return new SearchDto();
        }
    }
}
