package com.revature.serivce;

import com.revature.domain.Account;
import com.revature.persistance.AccountDAO;

public class AccountServiceImpl implements AccountService {
    private final AccountDAO accountDAO;

    public AccountServiceImpl(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }


    @Override 
    public int createAccount(Account newAccount){
        if(newAccount != null){
            // Call the DAO layer to add the account
            try {
                return accountDAO.createAccount(newAccount);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to create account", e);
            }
        }
        else {
            throw new IllegalArgumentException("Account cannot be null");
        }
    }

    @Override 
    public Account login(int accountId, int pin){
        try {
            return accountDAO.login(accountId, pin);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to login", e);
        }
    }
}
