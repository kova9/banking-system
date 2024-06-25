package com.bankingSystem.bankingSystem.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import lombok.Data;

@Data
public class CustomerDto {
    private String name;
    private String address;
    private String email;
    private String phoneNumber;

    public static CustomerDto fromJson(JsonNode json) {
        if (json != null) {
            return new Gson().fromJson(json.toString(), CustomerDto.class);
        } else {
            return new CustomerDto();
        }
    }
}
