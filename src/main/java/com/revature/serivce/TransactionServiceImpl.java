package com.revature.serivce;

import java.util.List;

import com.revature.domain.Account;
import com.revature.domain.Transfer;
import com.revature.persistance.TransactionDAO;

public class TransactionServiceImpl implements TransactionService {
    private final TransactionDAO transactionDAO;

    public TransactionServiceImpl(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @Override 
    public Account oneAccountAction(int fromAccountId, double amount){
        if(amount > 0){
            return transactionDAO.deposit(fromAccountId, amount);
        }
        else if(amount < 0){
            return transactionDAO.withdraw(fromAccountId, amount);
        }
        else{
            throw new IllegalArgumentException("Transaction amount cannot be zero.");
        }
    }

    @Override 
    public Account twoAccountAction(int fromAccountId, int toAccountId, double amount){
        if(amount == 0){
            throw new IllegalArgumentException("Transaction amount cannot be zero.");
        } else if(amount < 0){
            throw new IllegalArgumentException("Transaction amount must be positive.");
        } else if (fromAccountId == toAccountId){
            throw new IllegalArgumentException("Cannot transfer money to own account.");
        } else {
            return transactionDAO.transfer(fromAccountId, toAccountId, amount);
        }
    }

    @Override
    public List<Transfer> getAllTransactions(int accountId){
        return transactionDAO.getAllTransfers(accountId);
    }
}
