package com.example;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

public class LibraryCore {
    private SearchStrategy searchStrategy;
    private SortStrategy sortStrategy;

    // Constructor ile stratejileri enjekte ediyoruz (Dependency Injection)
    public LibraryCore(SearchStrategy searchStrategy, SortStrategy sortStrategy) {
        this.searchStrategy = searchStrategy;
        this.sortStrategy = sortStrategy;
    }

    public List<Book> executeSearch(String query) {
        List<Book> allBooks = LibraryDB.getInstance().getBooks();
        
        // Arama stratejisini uygula [cite: 20]
        List<Book> filtered = allBooks.stream()
                .filter(book -> searchStrategy.matches(book, query))
                .collect(Collectors.toCollection(ArrayList::new));
        
        // Sıralama stratejisini uygula [cite: 21]
        sortStrategy.sort(filtered);
        
        return filtered;
    }
}

// --- Arama Sınıfları ---
class TitleSearch implements SearchStrategy {
    public boolean matches(Book b, String q) { return b.getTitle().toLowerCase().contains(q.toLowerCase()); }
}

class AuthorSearch implements SearchStrategy {
    public boolean matches(Book b, String q) { return b.getAuthor().toLowerCase().contains(q.toLowerCase()); }
}

class ISBNSearch implements SearchStrategy {
    public boolean matches(Book b, String q) { return b.getIsbn().contains(q); }
}

class GlobalSearch implements SearchStrategy {
    public boolean matches(Book b, String q) {
        String query = q.toLowerCase();
        return b.getTitle().toLowerCase().contains(query) || 
               b.getAuthor().toLowerCase().contains(query) ||
               b.getIsbn().contains(query) ||
               b.getTags().stream().anyMatch(t -> t.toLowerCase().contains(query)) ||
               b.getCategories().stream().anyMatch(c -> c.toLowerCase().contains(query));
    }
}

// --- Sıralama Sınıfları ---
class TitleAscSort implements SortStrategy {
    public void sort(List<Book> books) {
        books.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
    }
}

class TitleDescSort implements SortStrategy {
    public void sort(List<Book> books) {
        books.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());
    }
}


/**
 * Strategy interface for searching. [cite: 5, 6]
 */
interface SearchStrategy {
    boolean matches(Book book, String query);
}

/**
 * Strategy interface for sorting. [cite: 21]
 */
interface SortStrategy {
    void sort(List<Book> books);
}