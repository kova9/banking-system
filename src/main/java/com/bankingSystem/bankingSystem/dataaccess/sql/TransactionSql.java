package com.bankingSystem.bankingSystem.dataaccess.sql;

import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import com.bankingSystem.bankingSystem.dto.SearchDto;
import com.bankingSystem.bankingSystem.obj.CommonFields;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransactionSql implements Specification<Transaction> {

    private SearchDto searchDto;

    public TransactionSql(SearchDto searchDto) {
        this.searchDto = searchDto;
    }
    @Override
    public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> allPredicates = new ArrayList<>();

        if (this.searchDto.getSenderId() != null && !this.searchDto.getSenderId().isEmpty() && !this.searchDto.isSenderAndReceiverSame()) {
            Predicate senderAccount = root.get(CommonFields.SENDER_ACCOUNT_ID).in(this.searchDto.getSenderId());
            allPredicates.add(senderAccount);
        }

        if (this.searchDto.getReceiverId() != null && !this.searchDto.getReceiverId().isEmpty() && !this.searchDto.isSenderAndReceiverSame()) {
            Predicate receiverAccount = root.get(CommonFields.RECEIVER_ACCOUNT_ID).in(this.searchDto.getReceiverId());
            allPredicates.add(receiverAccount);
        }

        if (this.searchDto.getMinAmount() != null) {
            Predicate minAmountPred = criteriaBuilder.greaterThanOrEqualTo(root.get(CommonFields.AMOUNT), this.searchDto.getMinAmount());
            allPredicates.add(minAmountPred);
        }

        if (this.searchDto.getMaxAmount() != null) {
            Predicate maxAmountPred = criteriaBuilder.lessThanOrEqualTo(root.get(CommonFields.AMOUNT), this.searchDto.getMaxAmount());
            allPredicates.add(maxAmountPred);
        }

        if (this.searchDto.getEndDate() != null) {
            Predicate endDatePred = criteriaBuilder.lessThanOrEqualTo(root.get(CommonFields.TIMESTAMP), this.searchDto.getEndDate());
            allPredicates.add(endDatePred);
        }

        if (this.searchDto.getStartDate() != null) {
            Predicate startDatePred = criteriaBuilder.greaterThanOrEqualTo(root.get(CommonFields.TIMESTAMP), this.searchDto.getStartDate());
            allPredicates.add(startDatePred);
        }

        if (this.searchDto.getCurrencyId() != null) {
            Predicate currencyPred = criteriaBuilder.equal(root.get(CommonFields.CURRENCY_ID), this.searchDto.getCurrencyId());
            allPredicates.add(currencyPred);
        }

        if (this.searchDto.getMessage() != null){
            Predicate messagePred = criteriaBuilder.equal(root.get(CommonFields.MESSAGE), this.searchDto.getMessage());
            allPredicates.add(messagePred);
        }

        if (this.searchDto.isStorno()){
            Predicate stornoTruePred = criteriaBuilder.equal(root.get(CommonFields.STORNO), this.searchDto.isStorno());
            allPredicates.add(stornoTruePred);
        } else {
            Predicate stornoFalsePred = criteriaBuilder.equal(root.get(CommonFields.STORNO), this.searchDto.isStorno());
            allPredicates.add(stornoFalsePred);
        }

        if (this.searchDto.isSenderAndReceiverSame()) {
            Predicate senderAccount = root.get(CommonFields.SENDER_ACCOUNT_ID).in(this.searchDto.getSenderId());
            Predicate receiverAccount = root.get(CommonFields.RECEIVER_ACCOUNT_ID).in(this.searchDto.getReceiverId());
            Predicate orPredicate = criteriaBuilder.or(senderAccount, receiverAccount);

            allPredicates.add(orPredicate);
            return criteriaBuilder.and(allPredicates.toArray(new Predicate[0]));
        } else {
            if (!allPredicates.isEmpty()) {
                return criteriaBuilder.and(allPredicates.toArray(new Predicate[0]));
            } else {
                return criteriaBuilder.conjunction();
            }
        }
    }
}
