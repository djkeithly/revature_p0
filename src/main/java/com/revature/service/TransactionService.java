package com.revature.service;

import java.math.BigDecimal;
import java.util.List;

import com.revature.domain.Account;
import com.revature.domain.Transfer;

public interface TransactionService {
    Account deposit(Account account, BigDecimal amount);

    Account withdraw(Account account, BigDecimal amount);

    Account twoAccountAction(Account account, int toAccountId, BigDecimal amount);

    List<Transfer> getAllTransactions(int accountId);
}
