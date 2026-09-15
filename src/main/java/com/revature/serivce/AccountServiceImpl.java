package com.revature.serivce;

import com.revature.domain.Account;
import com.revature.persistance.AccountDAO;

public class AccountServiceImpl implements AccountService {
    private final AccountDAO accountDAO;

    public AccountServiceImpl(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }


    @Override 
    public void createAccount(Account newAccount){
        if(newAccount != null){
            // Call the DAO layer to add the account
            accountDAO.createAccount(newAccount);
        }
        else {
            throw new IllegalArgumentException("Account cannot be null");
        }
    }
}
