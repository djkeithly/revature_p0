package com.revature.serivce;

import com.revature.persistance.TransactionDAO;

public class TransactionServiceImpl implements TransactionService {
    private final TransactionDAO transactionDAO;

    public TransactionServiceImpl(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @Override 
    public void oneAccountAction(int fromAccountId, double amount){

        if(amount > 0){
            try {
               transactionDAO.deposit(fromAccountId, amount);
            } catch (Exception e) {
                System.out.println("Error making deposit: s" + e);
            }
        }
        else if(amount < 0){
            try {
                
            } catch (Exception e) {
                System.out.println("Error making withdraw " + e);
            }
        }
        else{
            System.out.println("Error: No transaction action");
        }
    }
}
