package com.revature.api;

import java.util.List;
import java.util.Scanner;

import com.revature.domain.Account;
import com.revature.domain.Transfer;
import com.revature.serivce.AccountService;
import com.revature.serivce.TransactionService;

public class BankRepl {
    private final Scanner in = new Scanner(System.in);
    private final AccountService accountService;
    private final TransactionService transactionService;
    private Account loggedInAccount;

    public BankRepl(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void run(){
        while(true){
            System.out.print("> ");
            
            String command = in.nextLine();

            if(command.equals("exit")){
                break;
            }

            try{
                if(loggedInAccount == null){
                    handle(command);
                } else {
                    handleLogin(command);
                }
            }
            catch(IllegalArgumentException e){
                System.out.println("Invalid command: " + e.getMessage());
            }
        }
    }

    // Basic command handling for when no user is logged in
    private void handle(String command){
        switch(command){
            case "help" -> System.out.println("Available commands: help, add, login, exit");
            case "add" -> makeAccount();
            case "login" -> loggedInAccount = login(); 
            default -> throw new IllegalArgumentException(command);
        }
    }

    // Basic command handling for when a user is logged in. Allows personal actions
    private void handleLogin(String command){
        switch(command){
            case "help" -> System.out.println("Available commands: help, balance, deposit, withdraw, transfer, update pin, delete account, history, logout, exit");
            case "deposit" -> deposit();
            case "withdraw" -> withdraw();
            case "transfer" -> transfer();
            case "history" -> showHistory(); 
            case "balance" -> System.out.println("Your balance is: " + loggedInAccount.getBalance());
            case "update pin" -> updatePin();
            case "delete account" -> deleteAccount();
            case "logout" -> loggedInAccount = null;
            default -> throw new IllegalArgumentException(command);
        }
    }
    
    // Needs to collect pin. ID will be auto-generated and balance will default to 0.00
    private void makeAccount(){
        int pin;

        System.out.print("Pin number: ");
        try{
            pin = in.nextInt();
            in.nextLine(); // consume the newline character after the integer input
        } catch(Exception e){
            System.out.println("PIN can only be made of numbers.");
            System.out.println("Quitting account creation");
            in.nextLine();
            return;
        }

        try {
            int accountId = accountService.createAccount(new Account(pin));
            System.out.println("Your account number is: " + accountId + " ensure you remember this.");
        } catch (Exception e){
            System.out.println("Account Creation Failed: " + e);
        }
    }
        
    private Account login(){
        System.out.println("Enter Account Id and PIN");
        System.out.print("Account Number: ");
        int accountId = in.nextInt();
        System.out.print("PIN: ");
        int pin = in.nextInt();
        in.nextLine(); // consume the newline character after the integer input

        try {
            Account account = accountService.login(accountId, pin);
            System.out.println("Welcome, " + account.getAccountId() + ". Your balance is: " + account.getBalance());
            return account;
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            return null;
        }
    }

    private void deposit(){
        System.out.print("How much to deposit: ");
        int amount = in.nextInt();
        in.nextLine();

        if(amount <= 0){
            System.out.println("Amount to deposit must be greater than 0");
            return;
        }

        try {
            loggedInAccount =transactionService.oneAccountAction(loggedInAccount.getAccountId(), amount);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private void withdraw(){
        System.out.print("How much to withdraw: ");
        int amount = in.nextInt();
            in.nextLine();

        if(amount <= 0){
            System.out.println("Amount to withdraw must be greater than 0");
            return;
        }
        if(amount > loggedInAccount.getBalance()){
            System.out.println("Amount to withdraw exceeds your current balance");
            return;
        }

        try {
            loggedInAccount = transactionService.oneAccountAction(loggedInAccount.getAccountId(), -amount);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void transfer(){
        System.out.print("Transfer from your account to account id: ");
        int toAccountId = in.nextInt();
        System.out.print("Amount to transfer: ");
        int amount = in.nextInt();
        in.nextLine();

        if(amount <= 0){
            System.out.println("Amount to transfer must be greater than 0");
            return;
        }
        if(amount > loggedInAccount.getBalance()){
            System.out.println("Amount to transfer exceeds your current balance");
            return;
        }

        try {
            loggedInAccount = transactionService.twoAccountAction(loggedInAccount.getAccountId(), toAccountId, amount);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void showHistory(){
        try {
            List<Transfer> transfers = transactionService.getAllTransactions(loggedInAccount.getAccountId());
            if (transfers == null || transfers.isEmpty()) {
                System.out.println("No transaction history available.");
            } else {
                for (Transfer transfer : transfers) {
                    System.out.println(transfer);
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving transaction history: " + e);
        }
    }

    public void updatePin(){
        System.out.print("Enter old PIN: ");
        int oldPin = in.nextInt();
        System.out.print("Enter new PIN: ");
        int newPin = in.nextInt();
        in.nextLine();

        try {
            accountService.updatePin(loggedInAccount.getAccountId(), oldPin, newPin);
        } catch (Exception e) {
            System.out.println("Error updating PIN: " + e);
        }
    }

    public void deleteAccount(){
        System.out.print("Are you sure you want to delete your account (y/n): ");
        int pin;

        try{
            String confirm = in.nextLine();
            if(confirm.toLowerCase().equals("n"))
                return;
            else if (!confirm.toLowerCase().equals("y")){
                System.out.println("Invalid input, cancelling request");
                return;
            }

            System.out.print("Enter PIN: ");
            pin = in.nextInt();
            in.nextLine();
        } catch (Exception e){
            System.out.println("Invalid input, cancelling request");
            return;
        }

        try {
            accountService.deleteAccount(loggedInAccount.getAccountId(), pin);
            loggedInAccount = null;
        } catch (Exception e) {
            System.out.println("Error Creating Account: " + e);
        }
    
    }
}