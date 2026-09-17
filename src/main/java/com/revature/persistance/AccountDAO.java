package com.revature.persistance;

import java.sql.Connection;

import com.revature.domain.Account;

public interface AccountDAO {
    // Must be int to return account_id
    int createAccount(Account newAccount);

    Account login(int accountId, int pin);

    // This is only called after a deposit/withdraw and thus can accept a connection
    Account updateBalance(int accountId, double amount, Connection connection);

    void updatePin(int accountId, int newPin);
}
