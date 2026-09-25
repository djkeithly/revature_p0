package com.revature.service;

import com.revature.domain.Account;;

public interface AccountService {
    // Should return an id to display
    int createAccount(String pin);

    Account login(int accountId, String pin);

    void updatePin(int accountId, String oldPin, String newPin);
}
