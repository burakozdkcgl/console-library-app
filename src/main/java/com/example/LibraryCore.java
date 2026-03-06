package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

/**
 * The Context class in the Strategy Pattern. 
 * This class uses interchangeable search and sort algorithms without 
 * needing to know the implementation details of each.
 */
public class LibraryCore {
    // Reference to the strategy used for filtering books
    private SearchStrategy searchStrategy;
    // Reference to the strategy used for ordering results
    private SortStrategy sortStrategy;

    /**
     * Constructor that enables Dependency Injection.
     * This allows the client to decide which algorithms to use at runtime.
     */
    public LibraryCore(SearchStrategy searchStrategy, SortStrategy sortStrategy) {
        this.searchStrategy = searchStrategy;
        this.sortStrategy = sortStrategy;
    }

    /**
     * Executes the search workflow:
     * 1. Retrieves all books from the database.
     * 2. Filters the books using the provided search strategy.
     * 3. Sorts the filtered results using the provided sort strategy.
     */
    public List<Book> executeSearch(String query) {
        // Access the singleton database to get the full list of books
        List<Book> allBooks = LibraryDB.getInstance().getBooks();
        
        // Use Java Streams to filter books based on the strategy's matching logic
        List<Book> filtered = allBooks.stream()
                .filter(book -> searchStrategy.matches(book, query))
                .collect(Collectors.toCollection(ArrayList::new));
        
        // Pass the mutable list to the sort strategy for in-place ordering
        sortStrategy.sort(filtered);
        
        return filtered;
    }
}

// --- Search Strategy Implementations ---

/**
 * Concrete strategy for searching books by their title.
 */
class TitleSearch implements SearchStrategy {
    @Override
    public boolean matches(Book b, String q) { 
        return b.getTitle().toLowerCase().contains(q.toLowerCase()); 
    }
}

/**
 * Concrete strategy for searching books by their author.
 */
class AuthorSearch implements SearchStrategy {
    @Override
    public boolean matches(Book b, String q) { 
        return b.getAuthor().toLowerCase().contains(q.toLowerCase()); 
    }
}

/**
 * Concrete strategy for searching books by their unique ISBN.
 */
class ISBNSearch implements SearchStrategy {
    @Override
    public boolean matches(Book b, String q) { 
        return b.getIsbn().contains(q); 
    }
}

/**
 * Comprehensive strategy that searches across titles, authors, ISBNs, tags, and categories.
 */
class GlobalSearch implements SearchStrategy {
    @Override
    public boolean matches(Book b, String q) {
        String query = q.toLowerCase();
        return b.getTitle().toLowerCase().contains(query) || 
               b.getAuthor().toLowerCase().contains(query) ||
               b.getIsbn().contains(query) ||
               b.getTags().stream().anyMatch(t -> t.toLowerCase().contains(query)) ||
               b.getCategories().stream().anyMatch(c -> c.toLowerCase().contains(query));
    }
}


/**
 * Concrete strategy for sorting books by title in ascending order (A-Z).
 */
class TitleAscSort implements SortStrategy {
    @Override
    public void sort(List<Book> books) {
        // Uses case-insensitive order for natural alphabetical sorting
        books.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
    }
}

/**
 * Concrete strategy for sorting books by title in descending order (Z-A).
 */
class TitleDescSort implements SortStrategy {
    @Override
    public void sort(List<Book> books) {
        // Reverses the case-insensitive comparator
        books.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());
    }
}

/**
 * The common interface for all search algorithms.
 */
interface SearchStrategy {
    /**
     * Determines if a book meets the specific search criteria.
     * @param book The book object to check.
     * @param query The search string.
     * @return true if the book matches, false otherwise.
     */
    boolean matches(Book book, String query);
}

/**
 * The common interface for all sorting algorithms.
 */
interface SortStrategy {
    /**
     * Reorders the provided list of books.
     * @param books The list to be sorted.
     */
    void sort(List<Book> books);
}