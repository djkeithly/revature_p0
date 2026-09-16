package com.revature.persistance;

import com.revature.domain.Account;

public interface TransactionDAO {
    Account deposit(int fromAccountId, double amount);

    Account withdraw(int fromAccountId, double amount);
}