package com.revature.persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.domain.Account;
import com.revature.domain.Transfer;

public class TransactionDAOImpl implements TransactionDAO {
    private final AccountDAO accountDAO;

    private final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS transaction(
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

    private final String SELECT_ALL_TRANSFERS = """
            SELECT *
                FROM transaction
                WHERE from_account_id = ?
                OR to_account_id = ?;
    """;

    public TransactionDAOImpl() {
        initializeSchema();
        accountDAO = new AccountDAOImpl();
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
    public Account deposit(int fromAccountId, double amount){
        Connection connection = null;

        try{
            connection = ConnectionFactory.getConnectionFactory().getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_DEPOSIT_SQL);

            // Begin transaction for deposit operation
            connection.setAutoCommit(false);

            // Handle create deposit record
            statement.setInt(1, fromAccountId);
            statement.setDouble(2, amount);
            statement.executeUpdate();

            // Handle updating the user
            Account account = accountDAO.updateBalance(fromAccountId, amount);

            if(account != null){
                connection.commit();
                return account;
            }
        } catch (Exception e) {
            if(connection != null){
                try {
                    connection.rollback();
                } catch (Exception ex) {
                    System.out.println("Fatal Error: " + e);
                }
            }
            throw new IllegalStateException("Error depositing", e);
        } finally {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Fatal Error: " + e);
            }
        }}
        return null;
    }

    @Override 
    public Account withdraw(int fromAccountId, double amount){
        Connection connection = null;

        try{
            connection = ConnectionFactory.getConnectionFactory().getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_WITHDRAWAL_SQL);

            // Begin transaction for deposit operation
            connection.setAutoCommit(false);

            // Handle create deposit record
            statement.setInt(1, fromAccountId);
            statement.setDouble(2, amount);
            statement.executeUpdate();

            // Handle updating the user
            Account account = accountDAO.updateBalance(fromAccountId, amount);

            if(account != null){
                connection.commit();
                return account;
            }
        } catch (Exception e) {
            if(connection != null){
                try {
                    connection.rollback();
                } catch (Exception ex) {
                    System.out.println("Fatal Error: " + e);
                }
            }
            throw new IllegalStateException("Error withdrawing", e);
        } finally {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Fatal Error: " + e);
            }
        }}
    return null;
    }

    @Override
    public Account transfer(int fromAccountId, int toAccountId, double amount){
        Connection connection = null;

        try {
            connection = ConnectionFactory.getConnectionFactory().getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_TRANSFER_SQL);

            connection.setAutoCommit(false);

            statement.setInt(1, fromAccountId);
            statement.setInt(2, toAccountId);
            statement.setDouble(3, amount);

            statement.executeUpdate();

            accountDAO.updateBalance(toAccountId, amount);

            Account account = accountDAO.updateBalance(fromAccountId, -amount);

            if(account != null){
                connection.commit();
                return account;
            }



        } catch (Exception e) {
            if(connection != null){
                try {
                    connection.rollback();
                } catch (Exception ex) {
                    System.out.println("Fatal Error: " + e);
                }
            }
            throw new IllegalStateException("Error withdrawing", e);
        } finally {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Fatal Error: " + e);
            }
        }}
        return null;
    }

    @Override 
    public List<Transfer> getAllTransfers(int AccountId){
        List<Transfer> transfers = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TRANSFERS)) {

                statement.setInt(1, AccountId);
                statement.setInt(2, AccountId);
            
                ResultSet set = statement.executeQuery();
                while(set.next()){
                    transfers.add(mapTransfer(set));
                }
                return transfers;
        } catch (SQLException e) {
            throw databaseError("Could not initialize database schema", e);
        }
    }

    public Transfer mapTransfer(ResultSet set){
        try {
            return new Transfer(set.getInt("transaction_id"), 
                                set.getInt("from_account_id"), 
                                set.getString("type"), 
                                set.getDouble("amount"), 
                                set.getInt("to_account_id"), set.getString("timestamp"));
        } catch (SQLException e) {
            System.out.print("Error retrieving transactions");
            return null;
        }
    }
}