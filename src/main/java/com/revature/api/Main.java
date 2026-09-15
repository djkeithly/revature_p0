package com.revature.api;

import com.revature.persistance.AccountDAO;
import com.revature.persistance.AccountDAOImpl;
import com.revature.serivce.AccountService;
import com.revature.serivce.AccountServiceImpl;

public class Main {
    public static void main(String[] args) {
        AccountDAO dao = new AccountDAOImpl();
        AccountService service = new AccountServiceImpl(dao);
        new BankRepl(service).run();
    }
}