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
            try {
               return transactionDAO.deposit(fromAccountId, amount);
            } catch (Exception e) {
                System.out.println("Error making deposit: s" + e);
            }
        }
        else if(amount < 0){
            try {
                return transactionDAO.withdraw(fromAccountId, amount);
            } catch (Exception e) {
                System.out.println("Error making withdraw " + e);
            }
        }
        else{
            System.out.println("Error: No transaction action");
        }
        return null;
    }

    @Override 
    public Account twoAccountAction(int fromAccountId, int toAccountId, double amount){
        if(amount <= 0){
            throw new IllegalArgumentException("Transaction must have some money made to an account");
        } else if (fromAccountId == toAccountId){
            throw new IllegalArgumentException("Cannot transfer money to own account");
        } else {
            try {
                return transactionDAO.transfer(fromAccountId, toAccountId, amount);
            } catch (Exception e) {
                System.out.println("Error making transfer: " + e);
            }
        }

        return null;
    }

    @Override
    public List<Transfer> getAllTransactions(int accountId){
        try{
            return transactionDAO.getAllTransfers(accountId);
        } catch (Exception e){
            System.out.println("Error retrieving transactions: " + e);
        }
        return null;
    }
}
