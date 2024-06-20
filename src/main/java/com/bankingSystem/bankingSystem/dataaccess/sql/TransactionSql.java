package com.bankingSystem.bankingSystem.dataaccess.sql;

import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TransactionSql implements Specification<Transaction> {

    private String accountId;
    private Timestamp startDate;
    private Timestamp endDate;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;

    public TransactionSql(String accountId, Timestamp startDate, Timestamp endDate, BigDecimal minAmount, BigDecimal maxAmount) {
        this.accountId = accountId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }
    @Override
    public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate senderAccount = criteriaBuilder.equal(root.get("senderAccountId"), this.accountId);
        Predicate receiverAccount = criteriaBuilder.equal(root.get("receiverAccountId"), this.accountId);

        Predicate orPredicate = criteriaBuilder.or(senderAccount, receiverAccount);


        List<Predicate> allPredicates = new ArrayList<>();


        if(this.minAmount != null){
            Predicate minAmountPred = criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), this.minAmount);
            allPredicates.add(minAmountPred);
        }

        if (this.maxAmount != null) {
            Predicate maxAmountPred = criteriaBuilder.lessThanOrEqualTo(root.get("amount"), this.maxAmount);
            allPredicates.add(maxAmountPred);
        }
        if (this.endDate != null) {
            Predicate endDatePred = criteriaBuilder.lessThanOrEqualTo(root.get("timestamp"), this.endDate);
            allPredicates.add(endDatePred);
        }
        if (this.startDate != null){
            Predicate startDatePred = criteriaBuilder.greaterThanOrEqualTo(root.get("timestamp"), this.endDate);
            allPredicates.add(startDatePred);
        }

        if (!allPredicates.isEmpty()) {
            allPredicates.add(orPredicate);
            Predicate finalPredicate = criteriaBuilder.and(allPredicates.toArray(new Predicate[0]));
            return finalPredicate;
        } else {
            return orPredicate;
        }

    }
}
