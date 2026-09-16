package com.revature.serivce;

import java.util.List;

import com.revature.domain.Account;
import com.revature.domain.Transfer;

public interface TransactionService {
    Account oneAccountAction(int fromAccountId, double amount);

    Account twoAccountAction(int fromAccountId, int toAccountId, double amount);

    List<Transfer> getAllTransactions(int accountId);
}
