package com.revature.exception;

// Designed for it the database crashes or has not started.
// This should be thrown by ConnectionFactory and nothing else. 
public class DatabaseConnectionException extends RuntimeException {

    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseConnectionException(String message) {
        super(message);
    }
}