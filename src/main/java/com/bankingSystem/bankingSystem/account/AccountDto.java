package com.bankingSystem.bankingSystem.account;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {

    private String accountId;

    private String accountNumber;

    private String accountType;

    private BigDecimal balance;

    private BigDecimal pastMonthTurnover;

    private String customerId;

    public static AccountDto fromJson(JsonNode json) {
        if (json != null) {
            return new Gson().fromJson(json.toString(), AccountDto.class);
        } else {
            return new AccountDto();
        }
    }
}
