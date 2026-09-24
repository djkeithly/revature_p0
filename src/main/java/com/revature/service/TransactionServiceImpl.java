package com.revature.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.domain.Account;
import com.revature.domain.Transfer;
import com.revature.persistence.TransactionDAO;

public class TransactionServiceImpl implements TransactionService {
    private final TransactionDAO transactionDAO;
    private static Logger logger;

    public TransactionServiceImpl(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
        logger = LoggerFactory.getLogger(TransactionService.class);
    }

    // Where account is going to provide both balance and from_account_id
    @Override 
    public Account deposit(Account account, BigDecimal amount){
        if(amount.compareTo(BigDecimal.valueOf(0)) == 0){
            logger.error("Account: {} made redundant deposit of 0", account.getAccountId());
            throw new IllegalArgumentException("Deposit amount cannot be zero");
        } else if (amount.compareTo(BigDecimal.valueOf(0)) < 0){
            logger.error("Account: {} attempted to deposit negative money.", account.getAccountId());
            throw new IllegalArgumentException("Deposit amount cannot be negative.");
        }
        
        // Keeps the amount of money at 2 decimals.
        amount = amount.setScale(2, RoundingMode.UP);

        Account newAccount;

        try {
            newAccount = transactionDAO.deposit(account.getAccountId(), amount);
        } catch (IllegalStateException e){
            logger.error("Unexpected error when account: {} attempted to deposit: " + e, account.getAccountId());
            throw new IllegalStateException("Unknown error. Please try again.");
        }
        logger.info("Account: {} deposited ${}", account.getAccountId(), amount);
        return newAccount;
    }

    @Override 
    public Account withdraw(Account account, BigDecimal amount){
        if (account.getBalance().compareTo(amount) < 0) {
            logger.error("Account: {} attempted to overdraw.", account.getAccountId());
            throw new IllegalArgumentException("Cannot withdraw more than inside account");
        }

        if(amount.compareTo(BigDecimal.valueOf(0)) == 0){
            logger.error("Account: {} made redundant withdraw of 0", account.getAccountId());
            throw new IllegalArgumentException("Withdraw amount cannot be zero");
        } else if (amount.compareTo(BigDecimal.valueOf(0)) < 0){
            logger.error("Account: {} attempted to withdraw negative money.", account.getAccountId());
            throw new IllegalArgumentException("Withdraw amount cannot be negative.");
        }

        // Keeps the amount of money at 2 decimals.
        amount = amount.setScale(2, RoundingMode.UP);

        Account newAccount;
        
        try{
            newAccount = transactionDAO.withdraw(account.getAccountId(), amount);
        } catch (IllegalStateException e){
            logger.error("Unexpected error when account: {} attempted to withdraw: " + e, account.getAccountId());
            throw new IllegalStateException("Unknown error. Please try again.");
        }
        logger.info("Account: {} withdrew ${}", account.getAccountId(), amount);
        return newAccount;
    }

    @Override 
    public Account twoAccountAction(Account account, int toAccountId, BigDecimal amount){
        int fromAccountId = account.getAccountId();

        if(account.getBalance().compareTo(amount) < 0){
            logger.error("Account: {} attempted to transfer more than was inside account.", fromAccountId);
            throw new IllegalArgumentException("Cannot transfer more money than is in account");
        } else if(amount.compareTo(BigDecimal.valueOf(0)) == 0) {
            logger.error("Account: {} attempted to send no money to account: {}", fromAccountId, toAccountId);
            throw new IllegalArgumentException("Transaction amount cannot be zero.");
        } else if(amount.compareTo(BigDecimal.valueOf(0)) < 0) {
            logger.error("Account: {} attempted to transfer {} from account: {}", fromAccountId, amount.negate(), toAccountId);
            throw new IllegalArgumentException("Transaction amount must be positive.");
        } else if (fromAccountId == toAccountId) {
            logger.error("Account: {} attempted to transfer money to themselves.", fromAccountId);
            throw new IllegalArgumentException("Cannot transfer money to own account.");
        }
        
        // Keeps the amount of money at 2 decimals.
        amount = amount.setScale(2, RoundingMode.UP);
        Account newAccount;

        try{
            newAccount = transactionDAO.transfer(fromAccountId, toAccountId, amount);
        } catch (IllegalStateException e){
            logger.error("Unexpected error when account: {} attempted to deposit: " + e, account.getAccountId());
            throw new IllegalStateException("Unknown error. Please try again.");
        }

        if(newAccount == null){
            logger.error("Account: {} attempted to transfer to an account that does not exist");
            throw new IllegalArgumentException("Account to transfer to does not exist");
        }
        logger.info("Account: {} transferred${} to account: {}", fromAccountId, amount, toAccountId);
        return newAccount;
        
    }

    @Override
    public List<Transfer> getAllTransactions(int accountId){
        return transactionDAO.getAllTransfers(accountId);
    }
}
