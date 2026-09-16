package com.revature.api;

import com.revature.persistance.AccountDAO;
import com.revature.persistance.AccountDAOImpl;
import com.revature.persistance.TransactionDAO;
import com.revature.persistance.TransactionDAOImpl;
import com.revature.serivce.AccountService;
import com.revature.serivce.AccountServiceImpl;
import com.revature.serivce.TransactionService;
import com.revature.serivce.TransactionServiceImpl;

public class Main {
    public static void main(String[] args) {
        AccountDAO dao = new AccountDAOImpl();
        AccountService service = new AccountServiceImpl(dao);
        TransactionDAO tDao = new TransactionDAOImpl();
        TransactionService tService = new TransactionServiceImpl(tDao);
        new BankRepl(service, tService).run();
    }
}