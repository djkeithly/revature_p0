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
                balance         NUMERIC DEFAULT 0.00
            );
            """;

    // Inserts value into bank, zero in account balance by default
    // Auto generates id, and then returns it so that we can display it
    private static final String INSERT_ACCOUNT_SQL = "INSERT INTO account (pin) VALUES (?) RETURNING account_id;";
    private static final String LOGIN_SQL = "SELECT account_id, balance FROM account WHERE account_id = ? AND pin = ?;";
    private static final String UPDATE_BALANCE_SQL = "UPDATE account SET balance = balance + ? WHERE account_id = ? RETURNING account_id, balance;";
    private static final String UPDATE_PIN_SQL = "UPDATE account SET pin = ? WHERE account_id = ?;";
    private static final String DELETE_ACCOUNT_SQL = "DELETE FROM account WHERE account_id = ?;";

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
    public int createAccount(Account newAccount) {try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_ACCOUNT_SQL)) {
                // Ensures that failure can be rolled back
                connection.setAutoCommit(false);

                // Set data
                statement.setInt(1, newAccount.getAccountPin());

                // Executes and is returned 1 account_id to return to the user
                var resultSet = statement.executeQuery();

                // Assuming the account_id was generated successfully, commit and return else rollback
                if (resultSet.next()) {
                    int accountId = resultSet.getInt(1);
                    connection.commit();
                    return accountId;
                } else {
                    connection.rollback();
                    throw new IllegalStateException("Failed to retrieve generated account id");
                }
        } catch (SQLException e){
                throw databaseError("Could not add account", e);
         }
    }

    // Checks if account credentials are valid and returns the corresponding Account object
    @Override 
    public Account login(int accountId, int pin) {
        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
            PreparedStatement statement = connection.prepareStatement(LOGIN_SQL)) {
                statement.setInt(1, accountId);
                statement.setInt(2, pin);
                var resultSet = statement.executeQuery();
                if(resultSet.next()){
                    int id = resultSet.getInt("account_id");
                    double balance = resultSet.getDouble("balance");
                    return new Account(id, balance);
                } else {
                    return null;
                }
        } catch (SQLException e){
            throw databaseError("Could not login", e);
        }
   }

   @Override 
   public Account updateBalance(int accountId, double amount, Connection connection){
        try(PreparedStatement statement = connection.prepareStatement(UPDATE_BALANCE_SQL)) {
                statement.setDouble(1, amount);
                statement.setInt(2, accountId);

                var resultSet = statement.executeQuery();

                if(resultSet.next()){
                    int id = resultSet.getInt("account_id");
                    double balance = resultSet.getDouble("balance");
                    return new Account(id, balance);
                } else {
                    throw new IllegalStateException("Invalid account");
                }
                
        } catch (SQLException e){
            throw databaseError("Error updating balance: ", e);
        }
   }

   @Override
   public void updatePin(int accountId, int newPin){
        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_PIN_SQL)) {
            statement.setInt(1, newPin);
            statement.setInt(2, accountId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw databaseError("Could not update PIN", e);
        }
   }

   @Override 
   public void deleteAccount(int accountId){
        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_ACCOUNT_SQL)) {
            statement.setInt(1, accountId);

statement.executeUpdate();
        } catch
 (SQLException e) {
            throw databaseError("Could not delete account", e);
        }
   }
}