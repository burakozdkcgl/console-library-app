package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * LibraryDB is a Singleton class that acts as the central data repository.
 * It manages the list of all books, available categories, and system-wide tags.
 */
public class LibraryDB {
    private static LibraryDB instance;
    
    private List<Book> books;
    private List<String> categories; // Master list of available categories
    private List<String> tags;       // Master list of available tags
    
    private LibraryDB() {
        this.books = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    public static synchronized LibraryDB getInstance() {
        if (instance == null) {
            instance = new LibraryDB();
        }
        return instance;
    }

    // --- Book Management ---
    public void addBook(Book book) {
        this.books.add(book);
    }
    public List<Book> getBooks() { return books; }

    // --- Category Management ---
    public void addCategory(String category) {
        if (!categories.contains(category)) {
            categories.add(category);
        }
    }
    public List<String> getCategories() { return categories; }

    // --- Tag Management ---
    public void addTag(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }
    public List<String> getTags() { return tags; }
}