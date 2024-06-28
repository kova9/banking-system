package com.bankingSystem.bankingSystem.transaction;

import com.bankingSystem.bankingSystem.transaction.TransactionDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @Column(length = 36)
    private String transactionId;

    @Column
    private String senderAccountId;

    @Column
    private String receiverAccountId;

    @Column
    private BigDecimal amount;

    @Column
    private String currencyId;

    @Column
    private String message;

    @Column
    private Timestamp timestamp;

    @Column
    private boolean storno;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(String senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public String getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(String receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isStorno() {
        return storno;
    }

    public void setStorno(boolean storno) {
        this.storno = storno;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", senderAccountId='" + senderAccountId + '\'' +
                ", receiverAccountId='" + receiverAccountId + '\'' +
                ", amount=" + amount +
                ", currencyId='" + currencyId + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", storno=" + storno +
                '}';
    }

    public TransactionDto toDto(){
        TransactionDto dto = new TransactionDto();

        dto.setAmount(getAmount());
        dto.setReceiverAccountId(getReceiverAccountId());
        dto.setSenderAccountId(getTransactionId());
        dto.setCurrencyId(getCurrencyId());
        dto.setMessage(getMessage());
        dto.setStorno(isStorno());

        return dto;
    }
}
