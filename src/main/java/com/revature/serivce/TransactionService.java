package com.revature.serivce;

import com.revature.domain.Account;
public interface TransactionService {
    Account oneAccountAction(int fromAccountId, double amount);
}
