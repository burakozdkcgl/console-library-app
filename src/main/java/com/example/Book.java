package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a book in the library system.
 * Constraints: Maximum of 3 categories and 3 tags per book.
 */
public class Book {
    private String title;
    private String author;
    private String publicationYear;
    private String isbn;
    private String publisher;
    
    // Requirements: Up to three categories and tags 
    private List<String> categories;
    private List<String> tags;
    
    // Borrowing tracking 
    private int borrowCount;
    private boolean isAvailable;

    public Book() {
        this.categories = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.borrowCount = 0;
        this.isAvailable = true;
    }

    /**
     * Adds a category if the limit of 3 is not reached.
     */
    public boolean addCategory(String category) {
        if (categories.size() < 3) {
            categories.add(category);
            return true;
        }
        return false;
    }

    /**
     * Adds a tag if the limit of 3 is not reached.
     */
    public boolean addTag(String tag) {
        if (tags.size() < 3) {
            tags.add(tag);
            return true;
        }
        return false;
    }

    // --- Getters & Setters ---

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublicationYear() { return publicationYear; }
    public void setPublicationYear(String year) { this.publicationYear = year; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public List<String> getCategories() { return categories; }
    public List<String> getTags() { return tags; }

    public int getBorrowCount() { return borrowCount; }

    public boolean isAvailable() { return isAvailable; }

    public void borrow() {
        if (isAvailable) {
            borrowCount++;
            isAvailable = false;
        }
    }

    public void returnBook() {
        if (!isAvailable) {
            isAvailable = true;
        }
    }

    @Override
    public String toString() {
        return String.format("Book: %s | Author: %s | ISBN: %s | Status: %s", 
                title, author, isbn, isAvailable ? "Available" : "Borrowed");
    }
}