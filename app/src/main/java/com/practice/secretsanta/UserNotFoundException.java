package com.practice.secretsanta;

public class UserNotFoundException extends Exception {
    public String getMessage() {
        return "The current User-ID could not be found.";
    }
}
