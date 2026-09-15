package com.revature.persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.revature.domain.Account;

public class AccountDAOImpl implements AccountDAO {
    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS account(
                account_id      INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                pin             INTEGER NOT NULL,
                full_name       VARCHAR(255),
                balance         NUMERIC DEFAULT 0.00
            );
            """;

    private static final String INSERT_ACCOUNT_SQL = "INSERT INTO account (pin, full_name) VALUES (?, ?)";

    public AccountDAOImpl() {
        initializeSchema();
    }

    private void initializeSchema() {
        try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(CREATE_TABLE_SQL)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw databaseError("Could not initialize database schema", e);
        }
    }

    private IllegalStateException databaseError(String message, SQLException cause) {
        return new IllegalStateException(message, cause);
    }

    @Override
    public void createAccount(Account newAccount) {
        // Implementation for adding an account goes here
        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_ACCOUNT_SQL)) {
                statement.setInt(1, newAccount.getAccountPin());
                statement.setString(2, newAccount.getFullName());
                statement.executeUpdate();
        } catch (SQLException e){
                throw databaseError("Could not add account", (SQLException) e);
         }
    }
}
