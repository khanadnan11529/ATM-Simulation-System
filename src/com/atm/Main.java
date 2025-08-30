package com.atm;

public class Main {
    public static void main(String[] args) {
        // Create a sample account
        UserAccount user = new UserAccount("1234567890", "1234", 5000.0);

        ATM atm = new ATM(user);
        atm.start();
    }
}
