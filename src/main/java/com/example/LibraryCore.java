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

    // LibraryCore sınıfı içindeki metod:
    public static void executeCommand(Book book, Command cmd) {
        cmd.execute();
        book.addStep(cmd); // Komutu kitabın kendi geçmişine kaydet
    }
}



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
 * The common interface for all search algorithms.
 */
interface SearchStrategy {
    boolean matches(Book book, String query);
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
 * The common interface for all sorting algorithms.
 */
interface SortStrategy {
    void sort(List<Book> books);
}



/**
 * Command Pattern arayüzü [cite: 5]
 */
interface Command {
    void execute();
    void undo();
}

/**
 * Metin alanlarını (Title, Author vb.) güncellemek için komut sınıfı
 */
class UpdateFieldCommand implements Command {
    private Book book;
    private String fieldName, oldValue, newValue;

    public UpdateFieldCommand(Book book, String fieldName, String newValue) {
        this.book = book;
        this.fieldName = fieldName;
        this.newValue = newValue;
    }

    @Override
    public void execute() {
        switch (fieldName) {
            case "Title" -> { oldValue = book.getTitle(); book.setTitle(newValue); }
            case "Author" -> { oldValue = book.getAuthor(); book.setAuthor(newValue); }
            case "ISBN" -> { oldValue = book.getIsbn(); book.setIsbn(newValue); }
            case "Year" -> { oldValue = book.getPublicationYear(); book.setPublicationYear(newValue); }
            case "Publisher" -> { oldValue = book.getPublisher(); book.setPublisher(newValue); }
        }
    }

    @Override
    public void undo() {
        switch (fieldName) {
            case "Title" -> book.setTitle(oldValue);
            case "Author" -> book.setAuthor(oldValue);
            case "ISBN" -> book.setIsbn(oldValue);
            case "Year" -> book.setPublicationYear(oldValue);
            case "Publisher" -> book.setPublisher(oldValue);
        }
    }
}

/**
 * Kategori ve Tag listelerini güncellemek için komut sınıfı [cite: 17]
 */
class ListModifyCommand implements Command {
    private List<String> targetList;
    private String item;
    private boolean isAddition;

    public ListModifyCommand(List<String> list, String item, boolean isAddition) {
        this.targetList = list;
        this.item = item;
        this.isAddition = isAddition;
    }

    @Override
    public void execute() {
        if (isAddition) targetList.add(item);
        else targetList.remove(item);
    }

    @Override
    public void undo() {
        if (isAddition) targetList.remove(item);
        else targetList.add(item);
    }
}

