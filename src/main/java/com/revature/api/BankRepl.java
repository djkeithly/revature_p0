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
            case "help":
                System.out.println("Available commands: help, exit");
                break;
            case "add":
                makeAccount();
                break;
            default:
                throw new IllegalArgumentException(command);
        }
    }

    // Needs to collect name and pin. ID will be auto-generated and balance will default to 0.00
    private void makeAccount(){
        System.out.println("Full name: ");
        String fullName = in.nextLine();


        System.out.println("Pin number: ");
        int pin = in.nextInt();
        in.nextLine(); // consume the newline character after the integer input

        accountService.createAccount(new Account(pin, fullName));
    }
}