package com.revature.service;

import java.util.List;

import com.revature.domain.Account;
import com.revature.domain.Transfer;

public interface TransactionService {
    Account deposit(Account account, double amount);

    Account withdraw(Account account, double amount);

    Account twoAccountAction(Account account, int toAccountId, double amount);

    List<Transfer> getAllTransactions(int accountId);
}
