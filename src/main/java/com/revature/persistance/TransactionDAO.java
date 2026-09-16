package com.revature.persistance;

public interface TransactionDAO {
    void deposit(int fromAccountId, double amount);

    void withdraw(int fromAccountId, double amount);
}