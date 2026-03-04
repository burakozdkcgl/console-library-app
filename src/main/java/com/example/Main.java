package com.example;

/**
 * Main serves as the entry point for the Library Management System.
 * It initializes the database, populates it with mock data, and starts the UI.
 */
public class Main {
    public static void main(String[] args) {

        // Initialize the singleton database instance
        LibraryDB.getInstance();

        // Populate the database with mock data for testing and demonstration
        MockData.fillMockData();

        // Start the console-based user interface
        new LibraryHub();

    }

}