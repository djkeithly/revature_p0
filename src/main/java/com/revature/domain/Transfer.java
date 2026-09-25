package com.revature.domain;

public class Transfer {
    private final int id;
    private final int fromAccountId;
    private final int toAccountId;
    private final double amount;
    private final String type;
    private final String timestamp;

    // Two account transfer logic
    public Transfer(int id, int fromAccountId, String type, double amount, int toAccountId, String timestamp){
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.type = type;
        this.amount = amount;
        this.toAccountId = toAccountId;
        this.timestamp = timestamp;
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
    
    public String getTimestamp(){
        return timestamp;
    }

    public String getType(){
        return type;
    }

    // toString will show when an account was, made, what type, and what the amount was.
    // It will show an account if there was an account that a transfer was made to
    @Override 
    public String toString(){
        return  "[Type of transaction: " + type +
                ", Amount: $" + String.format("%,.2f", amount) +
                (type.equals("Transfer") ? (", Made to Account: " + toAccountId) + " from Account: " + fromAccountId : "") +
                ", Timestamp: " + timestamp +
                ']';
    }
}
