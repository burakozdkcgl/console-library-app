package com.example;

/**
 * LibraryDB is designed as a singleton to ensure that there is only one instance of the database throughout the application lifecycle.
 * This allows for centralized management of the library's data and ensures consistency across different modules.
 */
public class LibraryDB {
    // Static instance of the class
    private static LibraryDB instance;
    
    /**
     * Private Constructor to prevent instantiation from outside the class.
     */
    private LibraryDB() {
        
    }

    /**
     * Global access point for the LibraryDB instance.
     */
    public static synchronized LibraryDB getInstance() {
        if (instance == null) {
            instance = new LibraryDB();
        }
        return instance;
    }

}