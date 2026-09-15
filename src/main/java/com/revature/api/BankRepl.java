package com.revature.api;

import java.util.Scanner;

import com.revature.domain.Account;
import com.revature.serivce.AccountService;

public class BankRepl {
    private final Scanner in = new Scanner(System.in);
    private final AccountService accountService;

    public BankRepl(AccountService accountService) {
        this.accountService = accountService;
    }

    public void run(){
        while(true){
            System.out.println(">");
            
            String command = in.nextLine();

            if(command.equals("exit")){
                break;
            }

            try{
                handle(command);
            }
            catch(IllegalArgumentException e){
                System.out.println("Invalid command: " + e.getMessage());
            }
        }
    }

    private void handle(String command){
        switch(command){
            case "help" -> System.out.println("Available commands: help, add, exit");
            case "add" -> makeAccount();
            case "login" -> {Account account = login(); handleLogin(account); }
            default -> throw new IllegalArgumentException(command);
        }
    }

    private void handleLogin(Account account){
        if(account != null) {
            System.out.println("Login successful.");
        } else {
            System.out.println("Login failed.");
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