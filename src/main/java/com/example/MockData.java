package com.example;

import java.util.List;
import java.util.Random;
import java.util.Collections;

public class MockData {

    public static void fillMockData() {
        LibraryDB db = LibraryDB.getInstance();

        // 1. Master Listeler
        List<String> masterCategories = List.of("Fiction", "Dystopian", "Fantasy", "Classic", "Horror", "Philosophical");
        List<String> masterTags = List.of("Bestseller", "Masterpiece", "Must-Read", "Vintage", "Dark");

        // 2. Kitap Verileri (Elle eşleştirilmiş kategori ve tagler ile)
        // Format: {Title, Author, Year, ISBN, Publisher, Category, Tag}
        String[][] booksData = {
            {"The Great Gatsby", "F. Scott Fitzgerald", "1925", "9780743273565", "Fiction-Works Co.", "Classic", "Vintage"},
            {"1984", "George Orwell", "1949", "9780451524935", "Global-Story Press", "Dystopian", "Masterpiece"},
            {"The Hobbit", "J.R.R. Tolkien", "1937", "9780547928227", "Mythos-Media Ltd.", "Fantasy", "Bestseller"},
            {"Dune", "Frank Herbert", "1965", "9780441172719", "Interstellar-Print", "Fiction", "Must-Read"},
            {"To Kill a Mockingbird", "Harper Lee", "1960", "9780061120084", "Southern-Bound Books", "Classic", "Must-Read"},
            {"The Catcher in the Rye", "J.D. Salinger", "1951", "9780316769488", "Urban-Life Publishing", "Fiction", "Vintage"},
            {"Crime and Punishment", "Fyodor Dostoevsky", "1866", "9780140449136", "East-Side Classics", "Philosophical", "Masterpiece"},
            {"Brave New World", "Aldous Huxley", "1932", "9780060850524", "Future-Vision Inc.", "Dystopian", "Must-Read"},
            {"Animal Farm", "George Orwell", "1945", "9780451526342", "Farm-House Media", "Dystopian", "Bestseller"},
            {"The Little Prince", "Antoine de Saint-Exupéry", "1943", "9780156012195", "Sky-High Prints", "Fiction", "Must-Read"},
            {"Fahrenheit 451", "Ray Bradbury", "1953", "9781451673319", "Fire-Brand Publishing", "Dystopian", "Dark"},
            {"Pride and Prejudice", "Jane Austen", "1813", "9780141439518", "Old-England Books", "Classic", "Bestseller"},
            {"The Stranger", "Albert Camus", "1942", "9780679720201", "Absurd-Life Press", "Philosophical", "Dark"},
            {"The Picture of Dorian Gray", "Oscar Wilde", "1890", "9780141439570", "Aesthetic-Works", "Philosophical", "Vintage"},
            {"Frankenstein", "Mary Shelley", "1818", "9780141439471", "Gothic-Tales Co.", "Horror", "Dark"}
        };

        for (String[] data : booksData) {
            Book book = new Book();
            book.setTitle(data[0]);
            book.setAuthor(data[1]);
            book.setPublicationYear(data[2]);
            book.setIsbn(data[3]);
            book.setPublisher(data[4]); // "Fiction-Works Co." gibi uydurma isimler

            // Kategorileri ve Tagleri rastgele değil, listedeki karşılığına göre atıyoruz
            book.addCategory(data[5]);
            book.addTag(data[6]);

            db.addBook(book);
        }

        // --- İstatistik ve Durum Simülasyonu ---
        Random random = new Random();
        for (Book b : db.getBooks()) {
            int count = random.nextInt(15); 
            for(int i = 0; i < count; i++) {
                b.borrow(); 
                b.returnBook(); 
            }
        }

        // Rastgele 4 tanesini ödünç verilmiş yap
        List<Book> shuffleList = db.getBooks();
        Collections.shuffle(shuffleList);
        for (int i = 0; i < 4; i++) {
            shuffleList.get(i).borrow(); 
        }

        masterCategories.forEach(db::addCategoryType);
        masterTags.forEach(db::addTagType);
    }
}