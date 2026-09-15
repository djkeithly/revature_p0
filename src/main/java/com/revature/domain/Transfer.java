package com.revature.domain;

public class Transfer {
    private int fromAccountId;
    private int toAccountId;
    private double amount;

    public Transfer(int fromAccountId, int toAccountId, double amount){
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public int getFromAccountId(){
        return fromAccountId;
    }

    public int getToAccountId(){
        return toAccountId;
    }

    public double getAmount(){
        return amount;
    }
}
