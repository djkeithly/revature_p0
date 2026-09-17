package com.revature.serivce;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.domain.Account;
import com.revature.domain.Transfer;
import com.revature.persistance.TransactionDAO;

public class TransactionServiceImpl implements TransactionService {
    private final TransactionDAO transactionDAO;
    private static Logger logger;

    public TransactionServiceImpl(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
        logger = LoggerFactory.getLogger(TransactionService.class);
    }

    @Override 
    public Account oneAccountAction(int fromAccountId, double amount){
        Account account;

        if(amount > 0){
            account = transactionDAO.deposit(fromAccountId, amount);
            logger.info("Deposit of ${} made to account: {}", fromAccountId, amount);
        }
        else if(amount < 0){
            account = transactionDAO.withdraw(fromAccountId, amount);
            logger.info("Withdraw of ${} made to account: {}", fromAccountId, -amount);
        }
        else{
            logger.warn("Account: {} made redundant transaction of 0", fromAccountId);
            throw new IllegalArgumentException("Transaction amount cannot be zero.");
        }

        return account;
    }

    @Override 
    public Account twoAccountAction(int fromAccountId, int toAccountId, double amount){
        if(amount == 0){
            logger.warn("Account: {} attempted to send no money to account: {}", fromAccountId, toAccountId);
            throw new IllegalArgumentException("Transaction amount cannot be zero.");
        } else if(amount < 0){
            logger.warn("Account: {} attempted to transfer {} from account: {}", fromAccountId, -amount, toAccountId);
            throw new IllegalArgumentException("Transaction amount must be positive.");
        } else if (fromAccountId == toAccountId){
            logger.warn("Account: {} attempted to transfer money to themselves.", fromAccountId);
            throw new IllegalArgumentException("Cannot transfer money to own account.");
        } else {
            Account account = transactionDAO.transfer(fromAccountId, toAccountId, amount);
            logger.info("Account: {} transferred${} to account: {}", fromAccountId, amount, toAccountId);
            return account;
        }
    }

    @Override
    public List<Transfer> getAllTransactions(int accountId){
        return transactionDAO.getAllTransfers(accountId);
    }
}
