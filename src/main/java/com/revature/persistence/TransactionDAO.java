package com.revature.persistence;

import java.math.BigDecimal;
import java.util.List;

import com.revature.domain.Account;
import com.revature.domain.Transfer;

public interface TransactionDAO {
    Account deposit(int fromAccountId, BigDecimal amount);

    Account withdraw(int fromAccountId, BigDecimal amount);

    Account transfer(int fromAccountId, int toAccountId, BigDecimal amount);

    List<Transfer> getAllTransfers(int accountId);
}