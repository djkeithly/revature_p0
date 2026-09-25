package com.revature.service;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.revature.domain.Account;
import com.revature.persistence.AccountDAO;

public class AccountServiceImplTest {
    private AccountDAO dao;
    private AccountService service;

    @BeforeEach 
    void setup(){
        dao = mock(AccountDAO.class);
        service = new AccountServiceImpl(dao);
    }

    // Ensure that an account can be added
    // Positive test
    @Test 
    void addAccountValidStoresAccount(){
        String pin = "1111";

        service.createAccount(pin);

        verify(dao).createAccount(pin);
    }

    // Ensure that an account id is returned when creating an account
    // Positive test
    @Test 
    void addAccountValidReturnsAccount(){
        int accountId = 44;
        String pin = "1234";

        when(dao.createAccount(pin)).thenReturn(44);

        int id = service.createAccount(pin);

        assertEquals(accountId, id);   
    }

    // Ensures that createAccount's error handling works for no AccountId
    // Negative test
    @Test
    void addAccountInvalidNoAccountId(){
        assertThrows(
            IllegalArgumentException.class,
            () -> service.createAccount(null)
        );

        verifyNoInteractions(dao);
    }

    // Ensure that a PIN cannot be negative in createAccount
    // Negative test
    @Test
    void addAccountInvalidNegativePin(){
        String pin = "-2000";

        assertThrows(
            IllegalArgumentException.class,
            () -> service.createAccount(pin)
        );

        verifyNoInteractions(dao);
    }

    // Ensures that a PIN must be 4 digits
    // Negative test
    @Test 
    void addAccountInvalidInvalidPin(){
        String pin = "12";

        assertThrows(
            IllegalArgumentException.class, 
            () -> service.createAccount(pin)
        );

        verifyNoInteractions(dao);
    }

    // Test login gets the identical information correctly
    // Positive Test
    @Test
    void loginTestValid(){
        String pin = "1234";
        int accountId= 44;

        when(dao.login(accountId, pin)).thenReturn(new Account(44,BigDecimal.ZERO));

        Account returnAccount = service.login(accountId, pin);

        assertEquals(accountId, returnAccount.getAccountId());
    }

    // Ensure login throws errors on no match
    // Negative Test
    @Test 
    void loginTestInvalid(){
        String pin = "1234";
        int accountId = 44;

        when(dao.login(accountId, pin)).thenReturn(null);

        assertThrows(
            IllegalArgumentException.class, 
            () -> service.login(accountId, pin)
        );
    }

    // Ensures that login requires a valid 4 digit PIN
    // Negative Test
    @Test 
    void loginTestInvalidPin(){
        String pin = "12";
        int accountId = 44;

        assertThrows(
            IllegalArgumentException.class,
            () -> service.login(accountId, pin)
        );
    }

    // Ensures update PIN works
    // Positive Test
    @Test
    void updatePinValid(){
        String oldPin = "1234";
        String newPin = "4321";
        int accountId = 44;

        when(dao.login(accountId, oldPin)).thenReturn(new Account(accountId, BigDecimal.ZERO));

        service.updatePin(accountId, oldPin, newPin);

        verify(dao).updatePin(accountId, newPin);
    }

    // Ensures that if oldPin and newPin are the same, error is thrown
    // Negative Test
    @Test 
    void updatePinInvalidSamePin() {
        String oldPin = "1234";
        String newPin = "1234";
        int accountId = 44;

        assertThrows(
            IllegalArgumentException.class,
            () -> service.updatePin(accountId, oldPin, newPin)
        );

        verifyNoInteractions(dao);
    }

    // Ensures that if the account being changes does not exist, error is thrown
    // Negative Test
    @Test 
    void updatePinInvalidSameNoAccount(){
        String oldPin = "1234";
        String newPin = "1234";
        int accountId = 44;

        when(dao.login(accountId, oldPin)).thenReturn(null);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.updatePin(accountId, oldPin, newPin)
        );

        verifyNoInteractions(dao);
    }

    // Ensures that a new PIN cannot be changed to an illegal PIN (<=0)
    // Negative Test
    @Test
    void updatePinInvalidIllegalPin(){
        String oldPin = "1234";
        String newPin = "-1234";
        int accountId = 44;

        assertThrows(
            IllegalArgumentException.class,
            () -> service.updatePin(accountId, oldPin, newPin)
        );

        verifyNoInteractions(dao);
    }

    // Ensures that a new PIN must be 4 digits long
    // Negative test
    @Test 
    void updatePinInvalidIlnvalidPIN(){
        String oldPin = "1234";
        String newPin = "123";
        int accountId = 44;

        assertThrows(
            IllegalArgumentException.class,
            () -> service.updatePin(accountId, oldPin, newPin)
        );

        verifyNoInteractions(dao);
    }
}
