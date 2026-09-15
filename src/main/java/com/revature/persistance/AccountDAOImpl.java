package com.revature.persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.domain.Account;

public class AccountDAOImpl implements AccountDAO {
    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS accounts (
                account_number VARCHAR(255) PRIMARY KEY,
                account_type VARCHAR(255) NOT NULL,
                balance DECIMAL(10, 2) NOT NULL
            );
            """;

    @Override
    public void addAccount(Account newAccount) {
        // Implementation for adding an account goes here
    }

}
