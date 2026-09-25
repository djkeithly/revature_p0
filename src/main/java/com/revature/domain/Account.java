package com.revature.domain;

import java.math.BigDecimal;

public class Account {
    private final int account_id;
    private final int account_pin;
    private final BigDecimal balance;

    // Constructor for creating an account with a specified ID (used when retrieving from the database)
    // This should not have access to a pin at all
    public Account(int account_id, BigDecimal balance){
        this.account_pin = -1;
        this.account_id = account_id;
        this.balance = balance;
    }

    // Made for creating an account without specifying an ID, which will be auto-generated
    public Account(int account_pin){
        this.account_pin = account_pin;
        this.account_id = -1;
        this.balance = BigDecimal.ZERO;
    }

    public int getAccountId(){
        return account_id;
    }

    public int getAccountPin(){
        return account_pin;
    }
    
    public BigDecimal getBalance(){
        return balance;
    }

    public void deposit(BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) > 0){
            this.balance.add(amount);
        }
    }

    public boolean withdraw(BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) < 0 && amount.compareTo(this.balance) >= 0){
            this.balance.subtract(amount);
            return true;
        }
        return false;
    }
}