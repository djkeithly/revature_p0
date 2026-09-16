package com.revature.domain;

public class Transfer {
    private int fromAccountId;
    private int toAccountId;
    private double amount;
    private String type;
    private String timestamp;

    // Two account transfer logic
    public Transfer(int fromAccountId, int toAccountId, double amount){
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.type = "Transfer";
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    // One account transfer logic
    public Transfer(int fromAccountId, double amount){
        this.fromAccountId = fromAccountId;
        this.amount = amount;
        if(amount >= 0){
            this.type = "Deposit";
        } else {
            this.type = "Withdrawal";
        }
        this.timestamp = java.time.LocalDateTime.now().toString();
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
}
