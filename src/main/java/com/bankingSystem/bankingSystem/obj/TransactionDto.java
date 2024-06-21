package com.bankingSystem.bankingSystem.obj;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDto {
    private String senderAccountId;
    private String receiverAccountId;
    private BigDecimal amount;
    private String currencyId;
    private String message;

    public static TransactionDto fromJson(JsonNode json) {
        if (json != null) {
            return new Gson().fromJson(json.toString(), TransactionDto.class);
        } else {
            return new TransactionDto();
        }
    }
}
