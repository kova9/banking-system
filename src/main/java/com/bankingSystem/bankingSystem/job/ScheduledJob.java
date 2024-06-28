package com.bankingSystem.bankingSystem.job;

import com.bankingSystem.bankingSystem.job.common.Expressions;
import com.bankingSystem.bankingSystem.account.service.AccountService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledJob {
    private final AccountService accountService;

    public ScheduledJob(AccountService accountService){
        this.accountService = accountService;
    }

    @Scheduled(cron = Expressions.ONCE_A_MONTH)
    public void calculateMonthlyTurnovers(){
        accountService.getTurnovers();
    }
}
