package com.revature.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.domain.Account;
import com.revature.persistence.AccountDAO;

public class AccountServiceImpl implements AccountService {
    private final AccountDAO accountDAO;
    private static Logger logger;

    public AccountServiceImpl(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
        logger = LoggerFactory.getLogger(AccountService.class);
    }

    // This needs to be provided with an Account Object defined by the second constructor in the Account.java domain
    // newAccount(accountId = -1, accountPin = <USER DEFINED>, balance = 0);
    // This should be the only function to take the above definition
    @Override 
    public int createAccount(Account newAccount){
        // Check to make sure base inputs are valid
        if(newAccount == null){
            logger.error("Account generation failed");
            throw new IllegalArgumentException("Error receiving new account information, please try again.");
        } else if(newAccount.getAccountPin() <= 0){
            logger.error("Invalid PIN attempted to be entered.");
            throw new IllegalArgumentException("PIN must exist and be a positive integer.");
        }

        // Check PIN is of valid length
        String pinChecker = String.valueOf(newAccount.getAccountPin());

        if(pinChecker.length() != 4){
            logger.error("Invalid PIN attempted to be entered.");
            throw new IllegalArgumentException("PIN must be at least 4 digits long.");
        }

        // Run command
        int accountNumber = accountDAO.createAccount(newAccount);
        logger.info("Account with id: {} created.", accountNumber);
        return accountNumber;   
    }

    @Override 
    public Account login(int accountId, int pin){
        // Check PIN is of valid length
        if(String.valueOf(pin).length() != 4){
            logger.error("Invalid PIN for account: {}.", accountId);
            throw new IllegalArgumentException("Invalid PIN");
        }

        // Run login
        Account returnedAccount = accountDAO.login(accountId, pin);

        // Check login state
        if(returnedAccount == null){
            logger.error("Incorrect sign in for account: {}.", accountId);
            throw new IllegalArgumentException("Invalid account id or PIN.");
        } else {
            logger.info("Account {}, successfully validated.", accountId);
            return returnedAccount;
        }
    }

    @Override
    public void updatePin(int accountId, int oldPin, int newPin) {
        // Check to see that PINs are of valid length
        if(String.valueOf(oldPin).length() != 4 || String.valueOf(newPin).length() != 4){
            logger.error("Invalid PIN for account: {} on an attempt to update PIN.", accountId);
            throw new IllegalArgumentException("PIN must be 4 digits or more");
        }

        // Check base PIN inputs are valid
        if(newPin == oldPin){
            logger.error("The old pin is the same as new PIN for account: {}.", accountId);
            throw new IllegalArgumentException("Old PIN and new PIN cannot be the same.");
        }
        if(newPin <= 0){
            logger.error("Account: {} attempted to enter an illegal pin.");
            throw new IllegalArgumentException("PIN must exist and be a positive integer.");
        }

        Account account = accountDAO.login(accountId, oldPin);

        if (account == null) {
            logger.error("Account {} entered the wrong pin to update pin.", accountId);
            throw new IllegalArgumentException("Old PIN is incorrect.");
        }
        accountDAO.updatePin(accountId, newPin);
        logger.info("Account: {} updated their pin.", accountId);
    }
}
