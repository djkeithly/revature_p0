package com.revature.domain;

public class Account {
    private int id;
    private int pin;
    private double balance;

    public Account(int id, int pin){
        this.id = id;
        this.pin = pin;
        this.balance = 0;
    }

    public int getId(){
        return id;
    }

    public int getPin(){
        return pin;
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