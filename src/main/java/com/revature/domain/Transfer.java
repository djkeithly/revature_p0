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

    @Override 
    public String toString(){
        return "Transfer id: " + id +
                " [fromAccountId=" + fromAccountId +
                ", toAccountId=" + toAccountId +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ']';
    }
}
