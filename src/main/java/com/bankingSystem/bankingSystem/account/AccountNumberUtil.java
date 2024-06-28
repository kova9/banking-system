package com.bankingSystem.bankingSystem.account;

import java.security.SecureRandom;

public class AccountNumberUtil {

    private AccountNumberUtil(){}
    public static String generateRandomAccountNumber() {
        int length = 16;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Generates a random digit between 0 and 9
            sb.append(digit);
        }

        return sb.toString();
    }
}
