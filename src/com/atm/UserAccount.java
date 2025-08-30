package com.atm;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class UserAccount {
    private String accountNumber;
    private String pin;
    private double balance;
    private Queue<String> transactions = new LinkedList<>();
    private static final String FILE_PATH = "account.txt";

    public UserAccount(String accountNumber, String pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        loadFromFile(); // load saved data if exists
    }

    public boolean validatePin(String enteredPin) {
        return this.pin.equals(enteredPin);
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        addTransaction("Deposited: " + amount);
        saveToFile();
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            addTransaction("Withdrawn: " + amount);
            saveToFile();
            return true;
        } else {
            addTransaction("Failed Withdrawal Attempt: " + amount);
            saveToFile();
            return false;
        }
    }

    private void addTransaction(String detail) {
        if (transactions.size() == 5) { // store only last 5
            transactions.poll();
        }
        transactions.add(detail);
    }

    public void printMiniStatement() {
        System.out.println("\n--- Mini Statement (Last 5 Transactions) ---");
        for (String t : transactions) {
            System.out.println(t);
        }
    }

    // Save account details to file
    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(accountNumber + "," + pin + "," + balance);
            writer.newLine();
            for (String t : transactions) {
                writer.write("TXN:" + t);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load account details from file
    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String[] accData = reader.readLine().split(",");
            this.accountNumber = accData[0];
            this.pin = accData[1];
            this.balance = Double.parseDouble(accData[2]);

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("TXN:")) {
                    transactions.add(line.substring(4));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
