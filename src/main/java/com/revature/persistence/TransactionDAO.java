package com.revature.persistence;

import java.util.List;

import com.revature.domain.Account;
import com.revature.domain.Transfer;

public interface TransactionDAO {
    Account deposit(int fromAccountId, double amount);

    Account withdraw(int fromAccountId, double amount);

    Account transfer(int fromAccountId, int toAccountId, double amount);

    List<Transfer> getAllTransfers(int accountId);
}