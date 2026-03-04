package com.example;

import java.util.List;

/**
 * MockData handles the initial population of the LibraryDB.
 * It creates categories and tags first, then assigns them to books 
 * while respecting the "maximum of 3" constraint. 
 */
public class MockData {

    public static void fillMockData() {
        LibraryDB db = LibraryDB.getInstance();

        // 1. Populate Master Categories
        String[] masterCategories = {"Fiction", "Science", "History", "Fantasy", "Biography", "Classic"};
        for (String cat : masterCategories) db.addCategory(cat);

        // 2. Populate Master Tags
        String[] masterTags = {"Bestseller", "Award-Winning", "Must-Read", "Vintage", "Educational"};
        for (String tag : masterTags) db.addTag(tag);

        // 3. Create Books and Assign from Master Lists
        // Data: {Title, Author, Year, ISBN, Publisher}
        String[][] booksData = {
            {"The Great Gatsby", "F. Scott Fitzgerald", "1925", "9780743273565", "Scribner"},
            {"1984", "George Orwell", "1949", "9780451524935", "Signet Classics"},
            {"The Hobbit", "J.R.R. Tolkien", "1937", "9780547928227", "George Allen & Unwin"},
            {"Dune", "Frank Herbert", "1965", "9780441172719", "Chilton Books"},
            {"The Alchemist", "Paulo Coelho", "1988", "9780062315007", "HarperTorch"},
            {"Sapiens", "Yuval Noah Harari", "2011", "9780062316097", "Harper"},
            {"Meditations", "Marcus Aurelius", "180", "9780140449334", "Penguin"},
            {"The Odyssey", "Homer", "-800", "9780140268867", "Penguin Classics"},
            {"Crime and Punishment", "Fyodor Dostoevsky", "1866", "9780140449136", "The Russian Messenger"},
            {"Brave New World", "Aldous Huxley", "1932", "9780060850524", "Chatto & Windus"},
            {"Animal Farm", "George Orwell", "1945", "9780451526342", "Secker & Warburg"},
            {"The Little Prince", "Antoine de Saint-Exupéry", "1943", "9780156012195", "Reynal & Hitchcock"},
            {"Fahrenheit 451", "Ray Bradbury", "1953", "9781451673319", "Ballantine Books"},
            {"A Brief History of Time", "Stephen Hawking", "1988", "9780553380163", "Bantam"},
            {"The Art of War", "Sun Tzu", "-500", "9781590302255", "Shambhala"}
        };

        List<String> cats = db.getCategories();
        List<String> tags = db.getTags();

        for (int i = 0; i < booksData.length; i++) {
            Book book = new Book();
            book.setTitle(booksData[i][0]);
            book.setAuthor(booksData[i][1]);
            book.setPublicationYear(Integer.parseInt(booksData[i][2]));
            book.setIsbn(booksData[i][3]);
            book.setPublisher(booksData[i][4]);

            // Assign categories and tags from the central LibraryDB lists
            // Respecting the PDF limit: Max 3 categories and 3 tags 
            book.addCategory(cats.get(i % cats.size())); 
            book.addTag(tags.get(i % tags.size()));
            
            // Adding a second optional tag/category for variety
            if (i % 2 == 0) {
                book.addCategory(cats.get((i + 1) % cats.size()));
                book.addTag(tags.get((i + 1) % tags.size()));
            }

            db.addBook(book);
        }
    }
}