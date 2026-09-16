package com.revature.api;

import java.util.Scanner;

import com.revature.domain.Account;
import com.revature.serivce.AccountService;

public class BankRepl {
    private final Scanner in = new Scanner(System.in);
    private final AccountService accountService;
    private Account loggedInAccount;

    public BankRepl(AccountService accountService) {
        this.accountService = accountService;
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
            case "help" -> System.out.println("Available commands: help, balance, logout");
            case "balance" -> System.out.println("Your balance is: " + loggedInAccount.getBalance());
            case "logout" -> loggedInAccount = null;
            default -> throw new IllegalArgumentException(command);
        }
    }
    
    // Needs to collect name and pin. ID will be auto-generated and balance will default to 0.00
    private void makeAccount(){
        System.out.println("Full name: ");
        String fullName = in.nextLine();


        System.out.println("Pin number: ");
        int pin = in.nextInt();
        in.nextLine(); // consume the newline character after the integer input

        try {
            int accountId = accountService.createAccount(new Account(pin, fullName));
            System.out.println("Your account number is: " + accountId + " ensure you remember this.");
        } catch (Exception e) {
            System.out.println("Failed to create account.");
        }
    }

    private Account login(){
        System.out.println("Enter Account Id and PIN");
        System.out.print("Account Id: ");
        int accountId = in.nextInt();
        System.out.print("PIN: ");
        int pin = in.nextInt();
        in.nextLine(); // consume the newline character after the integer input

        try {
            Account account = accountService.login(accountId, pin);
            System.out.println("Welcome, " + account.getFullName() + "! Your balance is: " + account.getBalance());
            return account;
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            return null;
        }
    }
}