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

    /**
     * The entry point for the UI flow. 
     * It keeps the application alive in a loop until the user chooses to exit.
     */
    public LibraryHub() {
        scanner = new Scanner(System.in);
        isRunning = true;
        display();
    }

    private static void display() {
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
        
        formatHubRow("New Book", "1", "Add A New Book To Library!",     "      __...--~~~~~-._    _.-~~~~--..._");
        formatHubRow("Search", "2", "Access Global Search & Sort Engine", "    //                `V'              \\");
        formatHubRow("Borrow", "3", "Book Borrowing & Return Portal","   //                  |                \\");
        formatHubRow("Edit Book", "4", "Modify Book Details","  //__...--~~~~~~-.___ | _.-~~~~~~--...__\\");
        
        System.out.println(S_LINE + "     \\                  `-'                //"); 
        formatHubRow("Exit",   "0", "Close The Application",  "   \\____...--~~~~~~~~~~~~~~~--...______//");
        
        System.out.println(D_LINE + "           `~~~~~~~--..........--~~~~~~~'");
        System.out.print("» Hub Command: ");
    }

    /**
     * Internal router to direct the flow based on user input.
     */
    private static void routeCommand(String input) {
        switch (input) {
            case "1": break;
            case "2": break;
            case "3": showBorrowModule(); break;
            case "4": break;
            case "0": 
                System.out.println("\n[!] System shutting down. Goodbye!\n");
                isRunning = false; // Graceful exit from the loop
                break;
            default: 
                System.out.println("\n[!] Invalid Command. Please try again.");
        }
    }

    // --- Sub-Module Methods ---
    private static void showBorrowModule() {
        startModule("B O O K    B O R R O W I N G");
        printRow("List","1","View All Books and Availability");
        printRow("Borrow","2","Borrow a Book");
        printRow("Return","3","Return a Borrowed Book");
        endModule();
        switch (selection) {
            case "0": break;
            case "1": showBorrowList(); break;
            case "2": showBorrowSection(); break;
            case "3": showReturnSection(); break;
            default: 
                System.out.println("\n[!] Invalid Command. Returning to Hub.");
        }
    }

    private static void showBorrowList() {
        printAvailableBooks();
        printBorrowedBooks();
        endModule();
        switch (selection) {
            case "0": break;
            default: 
                System.out.println("\n[!] Invalid Command. Returning to Hub.");
        }
    }

    // --- Sub-Module Methods ---

    private static void showBorrowSection() {
        List<Book> allBooks = LibraryDB.getInstance().getBooks();
        List<Book> availableBooks = new ArrayList<>();

        printHeader("A V A I L A B L E    B O O K S");
        int displayIndex = 1;
        for (Book b : allBooks) {
            if (b.isAvailable()) {
                System.out.format("%d. %s by %s [%s]%n", displayIndex++, b.getTitle(), b.getAuthor(), b.getIsbn());
                availableBooks.add(b);
            }
        }

        System.out.println(S_LINE);
        printRow("Borrow", "X", "Enter the list number (eg. 1, 2, 3...)");
        endModule(); // Bu metod 'selection' değişkenini doldurur

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
                System.out.format("%d. %s by %s [%s]%n", displayIndex++, b.getTitle(), b.getAuthor(), b.getIsbn());
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

    private static void printAvailableBooks() {
        printHeader("A V A I L A B L E    B O O K S");
        for (Book b : LibraryDB.getInstance().getBooks()) {
            if (b.isAvailable()) {
                System.out.format("» %s by %s (%s) [%s]%n", b.getTitle(), b.getAuthor(), b.getPublicationYear(), b.getIsbn());
            }
        }
    }
    
    private static void printBorrowedBooks() {
        printHeader("B O R R O W E D    B O O K S");
        for (Book b : LibraryDB.getInstance().getBooks()) {
            if (!b.isAvailable()) {
                System.out.format("» %s by %s (%s) [%s]%n", b.getTitle(), b.getAuthor(), b.getPublicationYear(), b.getIsbn());
            }
        }
    }

    // --- Formatting Helpers ---

    private static void startModule(String title) {
        System.out.println();
        printHeader(title);
    }

    private static void endModule() {
        System.out.println(S_LINE);
        printRow("Back", "0", "Return to the Primary Hub");
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