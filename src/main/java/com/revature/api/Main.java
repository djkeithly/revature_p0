package com.revature.api;

import com.revature.exception.DatabaseConnectionException;
import com.revature.persistance.AccountDAO;
import com.revature.persistance.AccountDAOImpl;
import com.revature.persistance.TransactionDAO;
import com.revature.persistance.TransactionDAOImpl;
import com.revature.service.AccountService;
import com.revature.service.AccountServiceImpl;
import com.revature.service.TransactionService;
import com.revature.service.TransactionServiceImpl;

public class Main {
    public static void main(String[] args) {
        try {
            AccountDAO dao = new AccountDAOImpl();
            AccountService service = new AccountServiceImpl(dao);
            TransactionDAO tDao = new TransactionDAOImpl();
            TransactionService tService = new TransactionServiceImpl(tDao);
            new BankRepl(service, tService).run();
        } catch (DatabaseConnectionException e) {
            System.out.println("Service not available.");
        } catch (Exception e) {
            System.out.println("Fatal Error Detected.");
        }
    }
}