package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * LibraryDB, singleton class that acts as the central data repository.
 */
public class LibraryDB {

    // Singleton instance
    private static LibraryDB instance;
    
    // Data storage for books, categories, and tags
    private List<Book> books;
    private List<String> categories;
    private List<String> tags;
    
    // Private constructor to prevent instantiation from outside the class
    private LibraryDB() {
        this.books = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    // Method to get the singleton instance of LibraryDB and initialize it if it doesn't exist
    public static synchronized LibraryDB getInstance() {
        if (instance == null) {
            instance = new LibraryDB();
        }
        return instance;
    }

    // Public getters and methods to manage books, categories, and tags of the instance
    public List<Book> getBooks() { return books; }
    public List<String> getCategories() { return categories; }
    public List<String> getTags() { return tags; }

    public void addBook(Book book) {    this.books.add(book);    }

    public void addCategoryType(String category) {  if (!this.categories.contains(category)) {  this.categories.add(category);  }   }
    public void addTagType(String tag) {    if (!this.tags.contains(tag)) { this.tags.add(tag); }    }
    public void removeCategoryType(String category) { this.categories.remove(category); }
    public void removeTagType(String tag) { this.tags.remove(tag); }

}