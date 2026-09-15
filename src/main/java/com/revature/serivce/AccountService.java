package com.revature.serivce;

import com.revature.domain.Account;;

public interface AccountService {
    // Should return an id to display
    int createAccount(Account newAccount);

    Account login(int accountId, int pin);
}
