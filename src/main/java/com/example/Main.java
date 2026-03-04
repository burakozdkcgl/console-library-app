package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Initialize the singleton database instance
        LibraryDB database = LibraryDB.getInstance();

        // Display the console UI and start the application loop
        Scanner scanner = new Scanner(System.in);
        HubDisplay hub = new HubDisplay(scanner);
        hub.display();
        
    }
}