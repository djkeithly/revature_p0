package com.revature.persistence;

import java.math.BigDecimal;
import java.sql.Connection;

import com.revature.domain.Account;

public interface AccountDAO {
    // Must be int to return account_id
    int createAccount(String pin);

    Account login(int accountId, String pin);

    // This is only called after a deposit/withdraw and thus can accept a connection
    Account updateBalance(int accountId, BigDecimal amount, Connection connection);

    void updatePin(int accountId, String newPin);

    boolean findAccount(int accountId);
}
