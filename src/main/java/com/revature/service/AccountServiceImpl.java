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
    public int createAccount(String pin){
        // Check to make sure base inputs are valid
        int pinVal;
        try{
            pinVal = Integer.parseInt(pin);
        } catch(Exception e) {
            throw new IllegalArgumentException("Invalid PIN.");
        }

        if(pinVal < 1 || pinVal > 9999 || pin.length() != 4){
            logger.warn("Invalid PIN attempted to be entered.");
            throw new IllegalArgumentException("PIN must be positive and 4 digits long.");
        }

        // Run command
        int accountNumber;
        try{
            accountNumber = accountDAO.createAccount(pin);
        } catch (IllegalStateException e){
            logger.error("Unexpected error when attempting to create account");
            throw new IllegalStateException("Unknown error. Please try again.");
        }

        logger.info("Account with id: {} created.", accountNumber);
        return accountNumber;   
    }

    @Override 
    public Account login(int accountId, String pin){
        int pinNum;
        try {
            pinNum = Integer.parseInt(pin);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid PIN.");
        }

        // Check PIN is of valid length
        if(pinNum < 1 || pinNum > 9999 || pin.length() != 4){
            logger.warn("Invalid PIN for account: {}.", accountId);
            throw new IllegalArgumentException("Invalid PIN");
        }

        // Run login
        Account returnedAccount;

        try{
            returnedAccount = accountDAO.login(accountId, pin);
        } catch (IllegalStateException e){
            logger.error("Unexpected error when account: {} attempted to login: " + e, accountId);
            throw new IllegalStateException("Unknown error. Please try again.");
        }

        // Check login state
        if(returnedAccount == null){
            logger.warn("Incorrect sign in for account: {}.", accountId);
            throw new IllegalArgumentException("Invalid account id or PIN.");
        } else {
            logger.info("Account {}, successfully validated.", accountId);
            return returnedAccount;
        }
    }

    @Override
    public void updatePin(int accountId, String oldPin, String newPin) {
        int oldPinNum;
        int newPinNum;
        try {
            oldPinNum = Integer.parseInt(oldPin);
            newPinNum = Integer.parseInt(newPin);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid PIN.");
        }


        // Check to see that PINs are of valid length
        if(newPinNum < 1000 || newPinNum > 9999 || newPin.length() != 4 || oldPinNum < 1000 || oldPinNum > 9999 || oldPin.length() != 4){
            logger.warn("Invalid PIN for account: {} on an attempt to update PIN.", accountId);
            throw new IllegalArgumentException("PIN must be a positive 4 digits");
        }

        // Check base PIN inputs are valid
        if(newPinNum == oldPinNum){
            logger.warn("The old pin is the same as new PIN for account: {}.", accountId);
            throw new IllegalArgumentException("Old PIN and new PIN cannot be the same.");
        }

        Account account;

        try{
            account = accountDAO.login(accountId, oldPin);
        } catch (IllegalStateException e){
            logger.error("Unexpected error when account: {} attempted to update pin: " + e, accountId);
            throw new IllegalStateException("Unknown error. Please try again.");
        }

        if (account == null) {
            logger.warn("Account {} entered the wrong pin to update pin.", accountId);
            throw new IllegalArgumentException("Old PIN is incorrect.");
        }
        accountDAO.updatePin(accountId, newPin);
        logger.info("Account: {} updated their pin.", accountId);
    }
}
