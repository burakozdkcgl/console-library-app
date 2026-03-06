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
        formatHubRow("Modify", "5", "Modify Book Details","  \\                  `-'                //");
        formatHubRow("Manage", "6", "Category & Tag Management","   \\____...--~~~~~~~~~~~~~~~--...______//");
        System.out.println(S_LINE + "          `~~~~~~~--..........--~~~~~~~'"); 
        formatHubRow("Exit",   "0", "Close The Application",  "");
        System.out.println(D_LINE);
        System.out.print("» Hub Command: ");
    }

    /**
     * Internal router to direct the flow based on user input.
     */
    private static void routeCommand(String input) {
        switch (input) {
            case "1": showViewList(); break;
            case "2": showAddBookModule();break;
            case "3": showSearchModule(); break;
            case "4": showBorrowModule(); break;
            case "5": showModifyModule(); break;
            case "6": showManagementModule(); break;
            case "0": 
                System.out.println("\n[!] System shutting down. Goodbye!\n");
                isRunning = false; // Graceful exit from the loop
                break;
            default: 
                System.out.println("\n[!] Invalid selection.");
        }
    }


    private static void showAddBookModule() {
        printHeader("A D D    A    N E W    B O O K");
        System.out.println("║ Please enter the following details:");
        System.out.println(S_LINE);
        
        Book newBook = new Book();
        
        System.out.print("» Title: ");
        newBook.setTitle(scanner.nextLine().trim());
        
        System.out.print("» Author: ");
        newBook.setAuthor(scanner.nextLine().trim());
        
        System.out.print("» Publication Year: ");
        newBook.setPublicationYear(scanner.nextLine().trim());
        
        System.out.print("» ISBN: ");
        newBook.setIsbn(scanner.nextLine().trim());
        
        System.out.print("» Publisher: ");
        newBook.setPublisher(scanner.nextLine().trim());

        // Categories and Tags can be added in the Modify section after creation
        LibraryDB.getInstance().addBook(newBook);
        
        System.out.println("\n[+] Success: '" + newBook.getTitle() + "' has been added to the library.");
    }


private static void showSearchModule() {
    printHeader("S E A R C H    &    S O R T    E N G I N E");
    
    // 1. Arama Kriteri Seçimi (Alt alta listeleme)
    System.out.println("║ Select Search Field:");
    System.out.println("║ [1] Title");
    System.out.println("║ [2] Author");
    System.out.println("║ [3] ISBN");
    System.out.println("║ [4] Global (All Fields)");
    System.out.println(S_LINE);
    System.out.println("║ [0] Back to Hub");
    System.out.println(S_LINE);
    System.out.print("» Selection: "); // Sabit selection formatı
    String sChoice = scanner.nextLine();
    
    if (sChoice.equals("0")) {
        return; // Hub'a geri dön
    }
    // Input Validation: Geçersiz seçimde direkt Hub'a döner
    if (!sChoice.matches("[1-4]")) {
        System.out.println("\n[!] Invalid Search Field. Returning to Hub...");
        return;
    }
    
    SearchStrategy searcher = switch (sChoice) {
        case "1" -> new TitleSearch();
        case "2" -> new AuthorSearch();
        case "3" -> new ISBNSearch();
        default  -> new GlobalSearch();
    };

    // 2. Arama Kelimesi [cite: 31]
    System.out.print("» Enter Search Query: ");
    String query = scanner.nextLine();
    if (query.trim().isEmpty()) {
        System.out.println("\n[!] Search query cannot be empty.");
        return;
    }

    // 3. Sıralama Düzeni Seçimi (Alt alta listeleme)
    System.out.println(S_LINE);
    System.out.println("║ Select Sort Order:");
    System.out.println("║ [1] Sort by Title (Ascending A-Z)");
    System.out.println("║ [2] Sort by Title (Descending Z-A)");
    System.out.println("║ [0] Back to Hub");
    System.out.println(S_LINE);
    System.out.print("» Selection: "); // Sabit selection formatı
    String sortChoice = scanner.nextLine();
    
    if (sortChoice.equals("0")) {
        return; // Hub'a geri dön
    }
    // Input Validation: Geçersiz seçimde direkt Hub'a döner
    if (!sortChoice.matches("[1-2]")) {
        System.out.println("\n[!] Invalid Sort Choice. Returning to Hub...");
        return;
    }
    
    SortStrategy sorter = sortChoice.equals("2") 
        ? new TitleDescSort() 
        : new TitleAscSort();

    // 4. Stratejileri Core (Context) Yapısına Gönder ve Çalıştır [cite: 5, 8]
    LibraryCore engine = new LibraryCore(searcher, sorter);
    List<Book> results = engine.executeSearch(query);

    // 5. Sonuçları Göster
    if (results.isEmpty()) {
        System.out.println("\n[!] No books found matching: " + query);
    } else {
        renderResultsTable(results);
    }
}

private static void renderResultsTable(List<Book> results) {
    printHeader("M A T C H I N G    R E S U L T S");
    
    // Tablo Başlığı
    System.out.format("   %-5s | %-12s | %-30s%n", "Index", "Status", "Title & Author");
    System.out.println(S_LINE);

    for (int i = 0; i < results.size(); i++) {
        Book b = results.get(i);
        String status = b.isAvailable() ? "[Available]" : "[Borrowed]";
        System.out.format("   %-5d | %-12s | %s by %s%n", 
            (i + 1), status, truncate(b.getTitle(), 25), truncate(b.getAuthor(), 20));
    }

    System.out.println(S_LINE);
    printRow("Details", "X", "Enter index number to view/borrow");
    printRow("Back", "0", "Return to Hub");
    System.out.println(D_LINE);
    System.out.print("» Selection: ");
    
    String resSelection = scanner.nextLine();

        // Check if the user wants to go back before attempting to parse
        if (resSelection.equals("0")) {
            return;
        }

        try {
            int idx = Integer.parseInt(resSelection);
            // VALIDATION: Check if the index is within the bounds of the results list
            if (idx > 0 && idx <= results.size()) {
                // Pass the selected book to the interaction handler
                handleBookInteraction(results.get(idx - 1));
            } else {
                // FIX: Added explicit error message for out-of-bounds numbers
                System.out.println("\n[!] Invalid selection: Index " + idx + " is not in the list.");
            }
        } catch (NumberFormatException e) {
            // FIX: Added explicit error message for non-numeric inputs
            System.out.println("\n[!] Invalid input: Please enter a valid index number.");
        }
}

private static void showManagementModule() {
        startModule("M A N A G E M E N T    P A N E L");
        printRow("Categories", "1", "Manage System Categories");
        printRow("Tags", "2", "Manage System Tags");
        endModule();

        switch (selection) {
            case "1": manageType("Category"); break;
            case "2": manageType("Tag"); break;
            case "0": break;
            default: 
                System.out.println("\n[!] Invalid selection.");
        }
    }
/**
     * Generic helper method to manage either Categories or Tags to avoid code duplication.
     */
    private static void manageType(String type) {
        LibraryDB db = LibraryDB.getInstance();
        boolean managing = true;

        while (managing) {
            // 1. Dynamic Spaced Header: Converts "TAG" to "T A G"
            String rawHeader = type.toUpperCase() + " MANAGEMENT";
            StringBuilder spacedHeader = new StringBuilder();
            for (char c : rawHeader.toCharArray()) {
                spacedHeader.append(c).append(" ");
            }
            
            // Now calling printHeader with the spaced-out string
            printHeader(spacedHeader.toString().trim());
            
            // Access the appropriate list from the singleton database
            List<String> items = type.equals("Category") ? db.getCategories() : db.getTags();

            // Display existing items
            if (items.isEmpty()) {
                // Width adjusted to 61 characters to perfectly align with the border
                System.out.format("║ %-61s ║%n", "No " + type.toLowerCase() + "s defined in the system.");
            } else {
                for (int i = 0; i < items.size(); i++) {
                    // Increased padding to 57 to push the right border '║' to the edge
                    System.out.format("║ [%d] %-57s ║%n", (i + 1), items.get(i));
                }
            }

            System.out.println(S_LINE);
            // Switched to numeric selections [1] and [2] for uniform UI
            printRow("Add", "1", "Add a new " + type.toLowerCase());
            printRow("Remove", "2", "Remove an existing " + type.toLowerCase());
            printRow("Back", "0", "Return to Hub");
            System.out.println(D_LINE);
            System.out.print("» Selection: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    // Process adding a new item
                    System.out.print("» Enter new " + type.toLowerCase() + " name: ");
                    String newName = scanner.nextLine().trim();
                    if (!newName.isEmpty()) {
                        if (type.equals("Category")) db.addCategoryType(newName);
                        else db.addTagType(newName);
                        System.out.println("\n[+] Success: " + type + " added.");
                    }
                    break;

                case "2":
                    // Process removal of an item by index
                    System.out.print("» Enter index to remove: ");
                    try {
                        int idx = Integer.parseInt(scanner.nextLine());
                        if (idx > 0 && idx <= items.size()) {
                            String removed = items.get(idx - 1);
                            if (type.equals("Category")) db.removeCategoryType(removed);
                            else db.removeTagType(removed);
                            System.out.println("\n[-] Success: " + type + " '" + removed + "' removed.");
                        } else {
                            System.out.println("\n[!] Invalid index selection.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("\n[!] Error: Please enter a valid numeric index.");
                    }
                    break;

                case "0":
                    managing = false;
                    break;

                default:
                    System.out.println("\n[!] Invalid selection.");
            }
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

    private static void showModifyModule() {
        List<Book> allBooks = LibraryDB.getInstance().getBooks();
        List<Book> modifiedBooks = new ArrayList<>();
        printHeader("M O D I F Y    B O O K    D E T A I L S");

        int displayIndex = 1;
        for (Book b : allBooks) {
            
            System.out.format("%d. %s by %s [%s]%n", 
            displayIndex++, b.getTitle(), b.getAuthor(), b.getIsbn());
            modifiedBooks.add(b);
            
        }

        System.out.println(S_LINE);
        printRow("Modify", "X", "Enter the list number (eg. 1, 2, 3...)");
        endModule();

        try {
            int choice = Integer.parseInt(selection);
            if (choice > 0 && choice <= modifiedBooks.size()) {
                Book selectedBook = modifiedBooks.get(choice - 1);
                showModifyDetails(selectedBook);
            } else if (choice != 0) {
                System.out.println("\n[!] Invalid selection.");
            }
        } catch (NumberFormatException e) {
            if (!selection.equals("0")) System.out.println("\n[!] Please enter a valid number.");
        }
    }

    private static void showModifyDetails(Book book){

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