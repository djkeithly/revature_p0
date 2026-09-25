package com.revature.api;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.revature.domain.Account;
import com.revature.domain.Transfer;
import com.revature.service.AccountService;
import com.revature.service.TransactionService;

public class BankRepl {
    private final Scanner in = new Scanner(System.in);

    // Will contain amount and accountId, never PIN
    private final AccountService accountService;
    
    private final TransactionService transactionService;
    private Account loggedInAccount;

    public BankRepl(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void run(){
        System.out.println("Bank Application Online\n");

        while(true){
            System.out.print("> ");
            
            // To lower case just to keep all inputs uniform
            String command = in.nextLine().toLowerCase();

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
            case "help" -> System.out.println("Available commands: help, balance, deposit, withdraw, transfer, update pin, history, logout, exit");
            case "deposit" -> deposit();
            case "withdraw" -> withdraw();
            case "transfer" -> transfer();
            case "history" -> showHistory(); 
            case "balance" -> System.out.println("Your balance is: $" + String.format("%,.2f", loggedInAccount.getBalance()));
            case "update pin" -> updatePin();
            case "logout" -> loggedInAccount = null;
            default -> throw new IllegalArgumentException(command);
        }
    }
    
    // Needs to collect pin. ID will be auto-generated and balance will default to 0.00
    private void makeAccount(){
        System.out.print("Pin number (Must be 4 digits and not start with a zero): ");

        String pin = in.nextLine();

        try {
            int accountId = accountService.createAccount(pin);
            System.out.println("Your account number is: " + accountId + " ensure you remember this.");
        } catch (Exception e){
            System.out.println("Account Creation Failed: " + e.getMessage());
        }
    }
        
    private Account login(){
        int accountId = -1;
        String pin;

        try{
            System.out.println("Enter Account Id and PIN");
            System.out.print("Account Number: ");
            accountId = in.nextInt();
            in.nextLine(); // Consume the newline left-over
            System.out.print("PIN: ");
            pin = in.nextLine();
        } catch(InputMismatchException e){
            System.out.println("Incomprehensible inputs.");
            System.out.println("Quitting login");
            return null;
        } finally{
            // Consume the newline left-over if any
            if (accountId == -1) {
                in.nextLine();
            }
        }

        try {
            Account account = accountService.login(accountId, pin);
            System.out.println("Welcome, account " + account.getAccountId() + ". Your balance is: $" + String.format("%,.2f", account.getBalance()));
            return account;
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            return null;
        }
    }

    private void deposit(){
        BigDecimal amount;

        try{
            System.out.print("How much to deposit: $");
            amount = in.nextBigDecimal();
        } catch (InputMismatchException e){
            System.out.println("Input must be a number greater than zero.");
            System.out.println("Quitting deposit function");
            return;
        } finally{
            in.nextLine();
        }

        try {
            loggedInAccount =transactionService.deposit(loggedInAccount, amount);
        } catch (Exception e) {
            System.out.println("Depositing failed: " + e.getMessage());
        }
    }

    private void withdraw(){
        BigDecimal amount;

        try{
            System.out.print("How much to withdraw: $");
            amount = in.nextBigDecimal();
        } catch (InputMismatchException e){
            System.out.println("Input must be a number greater than zero");
            return;
        } finally{
            in.nextLine();
        }

        try {
            loggedInAccount = transactionService.withdraw(loggedInAccount, amount);
        } catch (Exception e) {
            System.out.println("Withdraw failed: " + e.getMessage());
        }
    }

    public void transfer(){
        int toAccountId;
        BigDecimal amount;

        try{
            System.out.print("Transfer from your account to account id: ");
            toAccountId = in.nextInt();
            System.out.print("Amount to transfer: $");
            amount = in.nextBigDecimal();
        } catch (InputMismatchException e) {
            System.out.println("Input incomprehensible");
            System.out.println("Quitting transfer");
            return;
        } finally {
            in.nextLine();
        }

        try {
            loggedInAccount = transactionService.twoAccountAction(loggedInAccount, toAccountId, amount);
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
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
            System.out.println("Error retrieving transaction history: " + e.getMessage());
        }
    }

    public void updatePin(){
        String oldPin;
        String newPin;

        try{
            System.out.print("Enter old PIN: ");
            oldPin = in.nextLine();
            System.out.print("Enter new PIN: ");
            newPin = in.nextLine();
        } catch (InputMismatchException e){
            System.out.println("Input incomprehensible");
            System.out.println("Quitting transfer");
            return;
        }
        
        try {
            accountService.updatePin(loggedInAccount.getAccountId(), oldPin, newPin);
        } catch (Exception e) {
            System.out.println("Error updating PIN: " + e.getMessage());
        }
    }
}