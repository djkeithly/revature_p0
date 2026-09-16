package com.revature.persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionDAOImpl implements TransactionDAO {
    private final String CREATE_TABLE_SQL = """
            CREATE TABLE transaction(
                transaction_id          SERIAL PRIMARY KEY,
                from_account_id         INTEGER NOT NULL REFERENCES account(account_id),
                type                    VARCHAR(20) NOT NULL,
                amount                  NUMERIC(12, 2) NOT NULL,
                to_account_id           INTEGER REFERENCES account(account_id),
                timestamp               TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
            );
    """;

    private final String INSERT_DEPOSIT_SQL = """
            INSERT INTO transaction (from_account_id, type, amount, timestamp)
            VALUES (?, 'Deposit', ?, CURRENT_TIMESTAMP);
    """;

    private final String INSERT_WITHDRAWAL_SQL = """
            INSERT INTO transaction (from_account_id, type, amount, timestamp)
            VALUES (?, 'Withdrawal', ?, CURRENT_TIMESTAMP);
    """;

    private final String INSERT_TRANSFER_SQL = """
            INSERT INTO transaction (from_account_id, to_account_id, type, amount, timestamp)
            VALUES (?, ?, 'Transfer', ?, CURRENT_TIMESTAMP);
    """;

    public TransactionDAOImpl() {
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
    public void deposit(int fromAccountId, double amount){
        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
        PreparedStatement statement = connection.prepareStatement(INSERT_DEPOSIT_SQL)) {
            statement.setInt(1, fromAccountId);
            statement.setDouble(2, amount);

            statement.executeQuery();
        } catch (Exception e) {
            throw databaseError("Error depositing", (SQLException) e);
        }
    }

    @Override 
    public void withdraw(int fromAccountId, double amount){
        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
        PreparedStatement statement = connection.prepareStatement(INSERT_WITHDRAWAL_SQL)) {
            statement.setInt(1, fromAccountId);
            statement.setDouble(2, amount);

            statement.executeQuery();
        } catch (Exception e) {
            throw databaseError("Error depositing", (SQLException) e);
        }
    }
}