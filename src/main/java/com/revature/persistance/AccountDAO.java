package com.revature.persistance;

import com.revature.domain.Account;

public interface AccountDAO {
    // Must be int to return account_id
    int createAccount(Account newAccount);

    Account login(int accountId, int pin);

    Account updateBalance(int accountId, double amount);

    void updatePin(int accountId, int newPin);

    void deleteAccount(int accountId);
}
