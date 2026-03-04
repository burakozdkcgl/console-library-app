package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * LibraryDB is a Singleton class that acts as the central data repository.
 * It manages the list of all books, available categories, and system-wide tags.
 */
public class LibraryDB {

    // Singleton instance
    private static LibraryDB instance;
    
    // Data storage for books
    private List<Book> books;
    
    // Private constructor to prevent instantiation
    private LibraryDB() {
        this.books = new ArrayList<>();
    }

    public static synchronized LibraryDB getInstance() {
        if (instance == null) {
            instance = new LibraryDB();
        }
        return instance;
    }

    // addBook method to add a book to the library
    public void addBook(Book book) {
        this.books.add(book);
    }

    // Getter for books list
    public List<Book> getBooks() { return books; }

}