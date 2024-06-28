package com.bankingSystem.bankingSystem.email;

import com.bankingSystem.bankingSystem.email.EmailMessage;
import com.bankingSystem.bankingSystem.email.EmailFields;
import com.bankingSystem.bankingSystem.email.EmailInfo;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailSenderService {

    private final JavaMailSender mailSender;
    public EmailSenderService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    private final static String EMAIL_SUBJECT = "New Transaction";
    private final static String EMAIL_SENDER = "bankingsystem.testmail@gmail.com";


    public void sendMail(String receiver, EmailInfo info){
        SimpleMailMessage message = new SimpleMailMessage();
        String body = buildMessage(info);

        message.setFrom(EMAIL_SENDER);
        message.setTo(receiver);
        message.setText(body);
        message.setSubject(EMAIL_SUBJECT);

        mailSender.send(message);
    }

    private String buildMessage(EmailInfo info){
        EmailMessage message = EmailMessage.MESSAGE;
        Map<String, String> values = new HashMap<>();

        values.put(EmailFields.TRANSACTION_ID, info.getTransactionId());
        values.put(EmailFields.AMOUNT, String.valueOf(info.getAmount()));
        values.put(EmailFields.TYPE, info.getType());
        values.put(EmailFields.OLD_BALANCE, String.valueOf(info.getOldBalance()));
        values.put(EmailFields.NEW_BALANCE, String.valueOf(info.getNewBalance()));
        values.put(EmailFields.CURRENCY, info.getCurrency());

        return message.format(values);
    }

}
