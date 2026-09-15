package com.revature.api;

import java.util.Scanner;

public class BankRepl {
    private final Scanner in = new Scanner(System.in);

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
            default:
                throw new IllegalArgumentException(command);
        }
    }

    private void makeAccount(){
        System.out.println("Pin number: ");
        int pin = in.nextInt();
        in.nextLine(); // consume the newline character after the integer input
    }
}