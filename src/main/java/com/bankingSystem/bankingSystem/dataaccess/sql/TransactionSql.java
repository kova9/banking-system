package com.bankingSystem.bankingSystem.dataaccess.sql;

import com.bankingSystem.bankingSystem.dataaccess.entity.Transaction;
import com.bankingSystem.bankingSystem.obj.CommonFields;
import com.bankingSystem.bankingSystem.dto.SearchDto;
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
        Predicate senderAccount = criteriaBuilder.equal(root.get(CommonFields.SENDER_ACCOUNT_ID), this.searchDto.getSenderId());
        Predicate receiverAccount = criteriaBuilder.equal(root.get(CommonFields.RECEIVER_ACCOUNT_ID), this.searchDto.getReceiverId());

        Predicate orPredicate = criteriaBuilder.or(senderAccount, receiverAccount);


        List<Predicate> allPredicates = new ArrayList<>();


        if(this.searchDto.getMinAmout() != null){
            Predicate minAmountPred = criteriaBuilder.greaterThanOrEqualTo(root.get(CommonFields.AMOUNT), this.searchDto.getMinAmout());
            allPredicates.add(minAmountPred);
        }

        if (this.searchDto.getMaxAmout() != null) {
            Predicate maxAmountPred = criteriaBuilder.lessThanOrEqualTo(root.get(CommonFields.AMOUNT), this.searchDto.getMaxAmout());
            allPredicates.add(maxAmountPred);
        }
        if (this.searchDto.getEndDate() != null) {
            Predicate endDatePred = criteriaBuilder.lessThanOrEqualTo(root.get(CommonFields.TIMESTAMP), this.searchDto.getEndDate());
            allPredicates.add(endDatePred);
        }
        if (this.searchDto.getStartDate() != null){
            Predicate startDatePred = criteriaBuilder.greaterThanOrEqualTo(root.get(CommonFields.TIMESTAMP), this.searchDto.getStartDate());
            allPredicates.add(startDatePred);
        }
        if (this.searchDto.getCurrencyId() != null){
            Predicate currencyPred = criteriaBuilder.equal(root.get(CommonFields.CURRENCY_ID), this.searchDto.getCurrencyId());
            allPredicates.add(currencyPred);
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
