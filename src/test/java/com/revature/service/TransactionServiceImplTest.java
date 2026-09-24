package com.revature.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.revature.domain.Account;
import com.revature.persistence.TransactionDAO;

public class TransactionServiceImplTest {
    private TransactionDAO dao;
    private TransactionService service;

    @BeforeEach 
    void setup(){
        dao = mock(TransactionDAO.class);
        service = new TransactionServiceImpl(dao);
    }

    // Test a valid deposit
    @Test
    void depositValid(){
        int fromAccountId = 44;
        BigDecimal amount = BigDecimal.valueOf(50);
        BigDecimal roundedAmount = amount.setScale(2, RoundingMode.UP);

        Account startAccount = new Account(fromAccountId, BigDecimal.ZERO);
        Account expected = new Account(fromAccountId, roundedAmount);

        when(dao.deposit(fromAccountId, roundedAmount)).thenReturn(expected);

        Account actual = service.deposit(startAccount, amount);

        assertEquals(expected.getBalance().doubleValue(), actual.getBalance().doubleValue());
        verify(dao).deposit(fromAccountId, roundedAmount);
    }

    // Test a valid withdraw
    @Test 
    void withdrawValid(){
        int fromAccountId = 44;
        BigDecimal amount = BigDecimal.valueOf(50);
        BigDecimal roundedAmount = amount.setScale(2, RoundingMode.UP);
        Account startAccount = new Account(fromAccountId, amount);

        service.withdraw(startAccount, amount);

        verify(dao).withdraw(fromAccountId, roundedAmount);
    }

    // Test an invalid one account action by not moving money
    // In this case, deposit and withdraw act in identical ways and so we can assume that one working means the other works as well
    @Test
    void oneAccountActionInvalidNoMoney(){
        int fromAccountId = 44;
        BigDecimal amount = BigDecimal.ZERO;
        Account startAccount = new Account(fromAccountId, amount);

        assertThrows(IllegalArgumentException.class, 
            () -> service.deposit(startAccount, amount));

        verifyNoInteractions(dao);
    }

    // Test an invalid withdraw where a user is attempting to over withdraw
    @Test
    void withdrawInvalidOverdraft(){
        int fromAccountId = 44;
        BigDecimal amount = BigDecimal.valueOf(100);
        Account account = new Account(fromAccountId, amount.subtract(BigDecimal.valueOf(50)));

        assertThrows(
            IllegalArgumentException.class,
            () -> service.withdraw(account, amount)
        );

        verifyNoInteractions(dao);
    }

    // Test a valid two account action
    @Test
    void twoAccountActionValid(){
        int fromAccountId = 44;
        int toAccountId = 34;
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal roundedAmount = amount.setScale(2, RoundingMode.UP);
        Account account = new Account(fromAccountId, amount);

        when(dao.transfer(fromAccountId, toAccountId, roundedAmount)).thenReturn(account); // Would not actually be valid data but simulates that both accounts are valid

        service.twoAccountAction(account, toAccountId, amount);

        verify(dao).transfer(fromAccountId, toAccountId, roundedAmount);
    }

    // Test that an account can't transfer money to itself
    @Test 
    void twoAccountActionInvalidSameAccount(){
        int fromAccountId = 44;
        int toAccountId = 44;
        BigDecimal amount = BigDecimal.valueOf(100);
        Account account = new Account(fromAccountId, amount);

        assertThrows(IllegalArgumentException.class, 
            () -> service.twoAccountAction(account, toAccountId, amount)
        );

        verifyNoInteractions(dao);
    }

    // Test that an account can't transfer no money
    @Test 
    void twoAccountActionInvalidNoMoney(){
        int fromAccountId = 44;
        int toAccountId = 34;
        BigDecimal amount = BigDecimal.ZERO;
        Account account = new Account(fromAccountId, BigDecimal.valueOf(100));

        assertThrows(IllegalArgumentException.class, 
            () -> service.twoAccountAction(account, toAccountId, amount)
        );

        verifyNoInteractions(dao);
    }

    // Ensures the transaction history hits the database
    @Test 
    void transferHistoryValid(){
        int accountId = 44;

        service.getAllTransactions(accountId);

        verify(dao).getAllTransfers(accountId);
    }
}
