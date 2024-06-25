package com.bankingSystem.bankingSystem.dataaccess.entity;

import com.bankingSystem.bankingSystem.dto.TransactionDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
