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
        if(newAccount == null)
            throw new IllegalArgumentException("Error receiving new account information, please try again.");
        else 
            return accountDAO.createAccount(newAccount);
    }

    @Override 
    public Account login(int accountId, int pin){
        Account returnedAccount = accountDAO.login(accountId, pin);
        if(returnedAccount == null)
            throw new IllegalArgumentException("Invalid accountId or pin");
        else
            return returnedAccount;
    }

    @Override
    public void updatePin(int accountId, int oldPin, int newPin) {
        if(newPin == oldPin){
            throw new IllegalArgumentException("Old pin and new pin cannot be the same");
        }

        Account account = accountDAO.login(accountId, oldPin);

        if (account == null) {
            throw new IllegalArgumentException("Old PIN is incorrect");
        }
        accountDAO.updatePin(accountId, newPin);
    }

    @Override 
    public void deleteAccount(int accountId, int pin){
        Account account = accountDAO.login(accountId, pin);
        if(account == null){
            throw new IllegalArgumentException("Incorrect PIN");
        }
        accountDAO.deleteAccount(accountId);
    }
}
