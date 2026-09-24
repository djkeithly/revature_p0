package com.revature.api;

import com.revature.exception.DatabaseConnectionException;
import com.revature.persistence.AccountDAO;
import com.revature.persistence.AccountDAOImpl;
import com.revature.persistence.TransactionDAO;
import com.revature.persistence.TransactionDAOImpl;
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