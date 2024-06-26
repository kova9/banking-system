package com.bankingSystem.bankingSystem.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class SearchDto {
    private List<String> receiverId;
    private List<String> senderId;
    private String message;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String currencyId;
    private Timestamp startDate;
    private Timestamp endDate;
    private boolean senderAndReceiverSame;
    private boolean storno;

    public static SearchDto fromJson(JsonNode json) {
        if (json != null) {
            return new Gson().fromJson(json.toString(), SearchDto.class);
        } else {
            return new SearchDto();
        }
    }
}
