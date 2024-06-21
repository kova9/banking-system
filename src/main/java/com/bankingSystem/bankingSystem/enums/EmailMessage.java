package com.bankingSystem.bankingSystem.enums;

import java.util.Map;

public enum EmailMessage {

    MESSAGE(""" 
               Hello!
            
              The transaction with ID: {id} has been processed successfully,
              and the balance: {amount} {currency} has been {type} from your account.
              
              Old balance: {oldBalance} {currency}
              New balance: {newBalance} {currency}
              
              Regards,
              Your XYZ bank
            """);

    private final String text;

    EmailMessage(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public String format(Map<String, String> values) {
        String formattedMessage = text;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            formattedMessage = formattedMessage.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return formattedMessage;
    }
}
