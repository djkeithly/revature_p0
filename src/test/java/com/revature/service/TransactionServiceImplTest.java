package com.revature.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.revature.domain.Account;
import com.revature.persistance.TransactionDAO;
import com.revature.serivce.TransactionService;
import com.revature.serivce.TransactionServiceImpl;

public class TransactionServiceImplTest {
    private TransactionDAO dao;
    private TransactionService service;

    @BeforeEach 
    void setup(){
        dao = mock(TransactionDAO.class);
        service = new TransactionServiceImpl(dao);
    }

    // Test a valid oneAccountAction, deposit
    @Test
    void oneAccountActionDeposit(){
        int fromAccountId = 44;
        int amount = 50;
        Account expected = new Account(1234, fromAccountId, amount);

        when(dao.deposit(fromAccountId, amount)).thenReturn(expected);

        Account actual = service.oneAccountAction(fromAccountId, amount);

        assertEquals(expected, actual);
        verify(dao).deposit(fromAccountId, amount);
    }

    // Test a valid withdraw. A withdraw just has negative money
    @Test 
    void oneAccountActionWithraw(){
        int fromAccountId = 44;
        int amount = -50;

        service.oneAccountAction(fromAccountId, amount);

        verify(dao).withdraw(fromAccountId, amount);
    }

    // Test an invalid one account action by not moving money
    @Test
    void oneAccountActionInvalidNoMoney(){
        int fromAccountId = 44;
        int amount = 0;

        assertThrows(IllegalArgumentException.class, 
            () -> service.oneAccountAction(fromAccountId, amount));

        verifyNoInteractions(dao);
    }

    // Test a valid two account action
    @Test
    void twoAccountActionValid(){
        int fromAccountId = 44;
        int toAccountId = 34;
        int amount = 100;

        service.twoAccountAction(fromAccountId, toAccountId, amount);

        verify(dao).transfer(fromAccountId, toAccountId, amount);
    }

    // Test that an account can't transfer money to itself
    @Test 
    void twoAccountActionInvalidSameAccount(){
        int fromAccountId = 44;
        int toAccountId = 44;
        int amount = 100;

        assertThrows(IllegalArgumentException.class, 
            () -> service.twoAccountAction(fromAccountId, toAccountId, amount)
        );

        verifyNoInteractions(dao);
    }

    // Test that an account can't transfer no money
    @Test 
    void twoAccountActionInvalidNoMoney(){
        int fromAccountId = 44;
        int toAccountId = 34;
        int amount = 0;

        assertThrows(IllegalArgumentException.class, 
            () -> service.twoAccountAction(fromAccountId, toAccountId, amount)
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
