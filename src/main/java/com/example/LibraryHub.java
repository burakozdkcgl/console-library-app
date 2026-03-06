package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * LibraryHub handles all User Interface logic and application flow.
 * It encapsulates the scanner and manages the main execution loop.
 */
public class LibraryHub {
    // UI Visual Constants
    private static final String D_LINE = "=================================================================";
    private static final String S_LINE = "-----------------------------------------------------------------";
    private static final String STARS  = "*****************************************************************";
    private static final int DESC_WIDTH = 38;

    // Table formats for the Console UI
    private static final String HUB_FORMAT    = "║   %-10s [%s] │   %-38s   ║   %s%n";
    private static final String MODULE_FORMAT = "║   %-10s [%s] │   %-38s   ║%n";

    private static Scanner scanner;
    private static boolean isRunning;

    private static String selection; // For tracking user choices in sub-menus

    public static void display() {
        scanner = new Scanner(System.in);
        isRunning = true;
        while (isRunning) {
            showMenu();
            String input = scanner.nextLine();
            routeCommand(input);
        }
    }

    /**
     * Renders the primary dashboard with ASCII art elements.
     */
    private static void showMenu() {
        System.out.println("\n" + STARS);
        printHeader("L I B R A R Y    H U B    C O N S O L E");
        formatHubRow("View", "1", "List of All Books", "      __...--~~~~~-._    _.-~~~~--..._");
        formatHubRow("Add", "2", "Add A New Book To Library!",     "    //                `V'              \\");
        formatHubRow("Search", "3", "Access Global Search & Sort Engine", "   //                  |                \\");
        formatHubRow("Borrow", "4", "Book Borrowing & Return Portal","  //__...--~~~~~~-.___ | _.-~~~~~~--...__\\");
        formatHubRow("Edit", "5", "Modify Book Details","  \\                  `-'                //");
        System.out.println(S_LINE + "      \\____...--~~~~~~~~~~~~~~~--...______//"); 
        formatHubRow("Exit",   "0", "Close The Application",  "        `~~~~~~~--..........--~~~~~~~'");
        System.out.println(D_LINE);
        System.out.print("» Hub Command: ");
    }

    /**
     * Internal router to direct the flow based on user input.
     */
    private static void routeCommand(String input) {
        switch (input) {
            case "1": showViewList(); break;
            case "2": break;
            case "3": break;
            case "4": showBorrowModule(); break;
            case "5": break;
            case "0": 
                System.out.println("\n[!] System shutting down. Goodbye!\n");
                isRunning = false; // Graceful exit from the loop
                break;
            default: 
                System.out.println("\n[!] Invalid selection.");
        }
    }

    // --- Sub-Module Methods ---
    private static void showBorrowModule() {
        startModule("B O O K    B O R R O W I N G");
        printRow("Borrow","1","Borrow a Book");
        printRow("Return","2","Return a Borrowed Book");
        printRow("Stats","3","View Most Popular Books");
        endModule();
        switch (selection) {
            case "0": break;
            case "1": showBorrowSection(); break;
            case "2": showReturnSection(); break;
            case "3": showBorrowStats(); break;
            default: 
                System.out.println("\n[!] Invalid Command. Returning to Hub.");
        }
    }

private static void handleBookInteraction(Book book) {
    renderDetailPanel(book);

    String actionLabel = book.isAvailable() ? "Borrow" : "Return";
    String actionDesc  = book.isAvailable() ? "Borrow this book now" : "Return this book to library";

    // Seçenekleri basıyoruz
    printRow(actionLabel, "1", actionDesc);
    printRow("Back", "0", "Return to Catalog");
    
    System.out.println(D_LINE);
    System.out.print("» Selection: ");
    selection = scanner.nextLine();

    if (selection.equals("1")) {
        if (book.isAvailable()) {
            book.borrow();
            System.out.println("\n[+] Success: You have borrowed '" + book.getTitle() + "'.");
        } else {
            book.returnBook();
            System.out.println("\n[+] Success: '" + book.getTitle() + "' has been returned.");
        }
    } else if (selection.equals("0")) {
        return;
    } else {
        System.out.println("\n[!] Invalid selection.");
    }
}

private static void renderDetailPanel(Book book) {
    System.out.println("\n" + STARS);
    printHeader("B O O K    D E T A I L S");
    
    // Format: ║(1) + space(2) + Label(12) + space(1) + :(1) + space(1) + Veri(44) + space(2) + ║(1) = 65
    System.out.format("║  %-12s : %-44s  ║%n", "Title", truncate(book.getTitle(), 44));
    System.out.format("║  %-12s : %-44s  ║%n", "Author", truncate(book.getAuthor(), 44));
    System.out.format("║  %-12s : %-44s  ║%n", "ISBN", book.getIsbn());
    System.out.format("║  %-12s : %-44s  ║%n", "Publisher", truncate(book.getPublisher(), 44));
    System.out.format("║  %-12s : %-44s  ║%n", "Year", book.getPublicationYear());
    
    if (!book.getCategories().isEmpty() || !book.getTags().isEmpty()) {
        System.out.println(S_LINE);
        if (!book.getCategories().isEmpty()) {
            System.out.format("║  %-12s : %-44s  ║%n", "Categories", truncate(String.join(", ", book.getCategories()), 44));
        }
        if (!book.getTags().isEmpty()) {
            System.out.format("║  %-12s : %-44s  ║%n", "Tags", truncate(String.join(", ", book.getTags()), 44));
        }
    }
    
    System.out.println(S_LINE);
    
    // Status ve Borrow Count birleştirildi
    String statusStr = book.isAvailable() ? "AVAILABLE" : "BORROWED";
    System.out.format("║  %-12s : %-44s  ║%n", "Status", statusStr);
    System.out.format("║  %-12s : %-44d  ║%n", "Borrow Count", book.getBorrowCount()); // Borrow count eklendi
    
    System.out.println(D_LINE);
}

    private static String truncate(String text, int width) {
        if (text == null) return "";
        return (text.length() > width) ? text.substring(0, width - 3) + "..." : text;
    }

    private static void showViewList() {
    List<Book> allBooks = LibraryDB.getInstance().getBooks();
    
    printHeader("L I B R A R Y    C A T A L O G");
    
    System.out.format("   %-5s | %-12s | %-30s%n", "Index", "Status", "Title & Author");
    System.out.println(S_LINE);

    for (int i = 0; i < allBooks.size(); i++) {
        Book b = allBooks.get(i);
        String status = b.isAvailable() ? "[Available]" : "[Borrowed]";
        System.out.format("   %-5d | %-12s | %s by %s%n", 
            (i + 1), status, b.getTitle(), b.getAuthor());
    }

    System.out.println(S_LINE);
    // Burayı tam istediğin açıklamaya çevirdik:
    printRow("Details", "X", "Enter list number (eg 1, 2, 3..)");
    
    // endModule() yerine manuel bitiriyoruz ki Back[0] ve Selection bir arada olsun
    printRow("Back", "0", "Return to the Hub");
    System.out.println(D_LINE);
    System.out.print("» Selection: ");
    selection = scanner.nextLine();

    try {
        int choice = Integer.parseInt(selection);
        if (choice > 0 && choice <= allBooks.size()) {
            handleBookInteraction(allBooks.get(choice - 1));
        } else if (choice != 0) {
            System.out.println("\n[!] Invalid selection.");
        }
    } catch (NumberFormatException e) {
        if (!selection.equals("0")) System.out.println("\n[!] Please enter a valid number.");
    }

    }

    private static void showBorrowSection() {
        List<Book> allBooks = LibraryDB.getInstance().getBooks();
        List<Book> availableBooks = new ArrayList<>();

        printHeader("A V A I L A B L E    B O O K S");
        int displayIndex = 1;
        for (Book b : allBooks) {
            if (b.isAvailable()) {
                System.out.format("%d. %s by %s [%s]%n", 
                    displayIndex++, b.getTitle(), b.getAuthor(), b.getIsbn());
                availableBooks.add(b);
            }
        }

        System.out.println(S_LINE);
        printRow("Borrow", "X", "Enter the list number (eg. 1, 2, 3...)");
        endModule();

        try {
            int choice = Integer.parseInt(selection);
            if (choice > 0 && choice <= availableBooks.size()) {
                Book selectedBook = availableBooks.get(choice - 1);
                selectedBook.borrow();
                System.out.println("\n[+] Success: You have borrowed '" + selectedBook.getTitle() + "'.");
            } else if (choice != 0) {
                System.out.println("\n[!] Invalid selection.");
            }
        } catch (NumberFormatException e) {
            if (!selection.equals("0")) System.out.println("\n[!] Please enter a valid number.");
        }
    }

    private static void showReturnSection() {
        List<Book> allBooks = LibraryDB.getInstance().getBooks();
        List<Book> borrowedBooks = new ArrayList<>();

        printHeader("B O R R O W E D    B O O K S");
        int displayIndex = 1;
        for (Book b : allBooks) {
            if (!b.isAvailable()) {
                System.out.format("%d. %s by %s [%s]%n", 
                    displayIndex++, b.getTitle(), b.getAuthor(), b.getIsbn());
                borrowedBooks.add(b);
            }
        }

        System.out.println(S_LINE);
        printRow("Return", "X", "Enter the list number (eg. 1, 2, 3...)");
        endModule();

        try {
            int choice = Integer.parseInt(selection);
            if (choice > 0 && choice <= borrowedBooks.size()) {
                Book selectedBook = borrowedBooks.get(choice - 1);
                selectedBook.returnBook();
                System.out.println("\n[+] Success: '" + selectedBook.getTitle() + "' has been returned.");
            } else if (choice != 0) {
                System.out.println("\n[!] Invalid selection.");
            }
        } catch (NumberFormatException e) {
            if (!selection.equals("0")) System.out.println("\n[!] Please enter a valid number.");
        }
    }

    private static void showBorrowStats() {
        printHeader("B O R R O W    I N S I G H T S");
        
        List<Book> statsList = new ArrayList<>(LibraryDB.getInstance().getBooks());
        statsList.sort((b1, b2) -> Integer.compare(b2.getBorrowCount(), b1.getBorrowCount()));

        System.out.format("║ %-31s │ %-17s │ %-6s  ║%n", "Book Title", "ISBN", "Count");
        System.out.println(S_LINE);

        for (Book b : statsList) {
            String title = b.getTitle();
            if (title.length() > 31) {
                title = title.substring(0, 28) + "...";
            }

            System.out.format("║ %-31s │ %-17s │   %-5d ║%n", 
                title, 
                b.getIsbn(), 
                b.getBorrowCount());
        }

        endModule();
    }

    private static void startModule(String title) {
        System.out.println();
        printHeader(title);
    }

    private static void endModule() {
        System.out.println(S_LINE);
        printRow("Back", "0", "Return to the Hub");
        System.out.println(D_LINE);
        System.out.print("» Selection: ");
        selection = scanner.nextLine();
    }

    private static void printRow(String label, String key, String desc) {
        String safeDesc = (desc.length() > DESC_WIDTH) ? desc.substring(0, DESC_WIDTH - 3) + "..." : desc;
        System.out.format(MODULE_FORMAT, label, key, safeDesc);
    }

    private static void formatHubRow(String label, String key, String desc, String art) {
        String safeDesc = (desc.length() > DESC_WIDTH) ? desc.substring(0, DESC_WIDTH - 3) + "..." : desc;
        System.out.format(HUB_FORMAT, label, key, safeDesc, art);
    }

    private static void printHeader(String title) {
        System.out.println(D_LINE);
        int padding = (D_LINE.length() - title.length()) / 2;
        System.out.println(" ".repeat(Math.max(0, padding)) + title);
        System.out.println(D_LINE);
    }
}