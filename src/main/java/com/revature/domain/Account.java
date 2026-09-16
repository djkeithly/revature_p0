package com.revature.domain;

public class Account {
    private final int account_id;
    private final int account_pin;
    private double balance;

    // Constructor for creating an account with a specified ID (used when retrieving from the database)
    public Account(int account_pin, int account_id, double balance){
        this.account_pin = account_pin;
        this.account_id = account_id;
        this.balance = balance;
    }

    // Made for creating an account without specifying an ID, which will be auto-generated
    public Account(int account_pin){
        this.account_pin = account_pin;
        this.account_id = -1;
        this.balance = 0;
    }

    public int getAccountId(){
        return account_id;
    }

    public int getAccountPin(){
        return account_pin;
    }
    
    public double getBalance(){
        return balance;
    }

    public void deposit(double amount){
        if(amount > 0){
            this.balance += amount;
        }
    }

    public boolean withdraw(double amount){
        if(amount > 0 && amount <= this.balance){
            this.balance -= amount;
            return true;
        }
        return false;
    }
}