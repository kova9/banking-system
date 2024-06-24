package com.bankingSystem.bankingSystem.job;

import com.bankingSystem.bankingSystem.job.common.Expressions;
import com.bankingSystem.bankingSystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledJob {
    @Autowired
    private AccountService accountService;

    @Scheduled(cron = Expressions.ONCE_A_MONTH)
    public void calculateMonthlyTurnovers(){
        accountService.getTurnovers();
    }
}
