package com.revature.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.domain.Account;
import com.revature.persistance.AccountDAO;

public class AccountServiceImpl implements AccountService {
    private final AccountDAO accountDAO;
    private static Logger logger;

    public AccountServiceImpl(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
        logger = LoggerFactory.getLogger(AccountService.class);
    }

    @Override 
    public int createAccount(Account newAccount){
        if(newAccount == null){
            logger.error("Account generation failed");
            throw new IllegalArgumentException("Error receiving new account information, please try again.");
        } else if(newAccount.getAccountPin() <= 0){
            logger.error("Invalid PIN attempted to be entered");
            throw new IllegalArgumentException("PIN must exist and be a positive integer");
        } else {
            int accountNumber = accountDAO.createAccount(newAccount);
            logger.info("Account with id: {} created.", accountNumber);
            return accountNumber;
        }
    }

    @Override 
    public Account login(int accountId, int pin){
        Account returnedAccount = accountDAO.login(accountId, pin);
        if(returnedAccount == null){
            logger.error("Incorrect sign in for account: {}.", accountId);
            throw new IllegalArgumentException("Invalid accountId or pin");
        } else {
            logger.info("Account {}, successfully validated.", accountId);
            return returnedAccount;
        }
    }

    @Override
    public void updatePin(int accountId, int oldPin, int newPin) {
        if(newPin == oldPin){
            logger.error("The old pin is the same as new pin for account: {}.", accountId);
            throw new IllegalArgumentException("Old pin and new pin cannot be the same");
        }
        if(newPin <= 0){
            logger.error("Account: {} attempted to enter an illegal pin.");
            throw new IllegalArgumentException("PIN must exist and be a positive integer");
        }

        Account account = accountDAO.login(accountId, oldPin);

        if (account == null) {
            logger.error("Account {} entered the wrong pin to update pin.", accountId);
            throw new IllegalArgumentException("Old PIN is incorrect");
        }
        accountDAO.updatePin(accountId, newPin);
        logger.info("Account: {} updated their pin.", accountId);
    }
}
