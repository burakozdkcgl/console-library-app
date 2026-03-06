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
    
    // Data storage for books, categories, and tags
    private List<Book> books;
    private List<String> categories;
    private List<String> tags;
    
    // Private constructor to prevent instantiation
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

    public void addBook(Book book) {    this.books.add(book);    }

    public void addCategoryType(String category) {  if (!this.categories.contains(category)) {  this.categories.add(category);  }   }
    public void addTagType(String tag) {    if (!this.tags.contains(tag)) { this.tags.add(tag); }    }
    public void removeCategoryType(String category) { this.categories.remove(category); }
    public void removeTagType(String tag) { this.tags.remove(tag); }

    public List<Book> getBooks() { return books; }
    public List<String> getCategories() { return categories; }
    public List<String> getTags() { return tags; }
}