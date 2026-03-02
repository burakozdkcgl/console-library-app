package com.example;

import java.util.Scanner;

/**
 * Premium Console Library Hub for BIM492.
 * Cleaned with DRY principles while maintaining UI alignment.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    
    // UI Constants
    private static final String D_LINE = "=================================================================";
    private static final String S_LINE = "-----------------------------------------------------------------";
    private static final String STARS  = "*****************************************************************";
    
    // Exact format to keep the frame stable
    private static final String HUB_ROW = "║   %-18s │   %-38s  ║   %s%n";
    private static final String MODULE_ROW = "║   %-18s │   %-38s  ║%n";

    public static void main(String[] args) {
        while (true) {
            showMainHub();
        }
    }

    private static void showMainHub() {
        printHubBanner();
        
        // The "Open Book" ASCII art - meticulously aligned to the right of the frame
        System.out.format(HUB_ROW, "Create [1]", "Initialize Book Creation Wizard",     "      __...--~~~~~-._   _.-~~~~~--..._");
        System.out.format(HUB_ROW, "Search [2]", "Access Global Search & Sort Engine", "    //               `V'               \\\\");
        System.out.format(HUB_ROW, "Borrow [3]", "Inventory Borrowing & Return Portal","   //                 |                 \\\\");
        System.out.format(HUB_ROW, "Modify [4]", "System Management & Undo Operations","  //__...--~~~~~~-._  | _.-~~~~~~--...__\\\\");
        
        // Manual alignment for the book's bottom curve outside the S_LINE and D_LINE
        System.out.println(S_LINE + "        \\\\                `-'                 //"); 
        System.out.format(HUB_ROW, "Exit   [0]", "Terminate Session and Secure Data",  "   \\\\____...--~~~~~~~~~~~~~~~--..._____//");
        
        System.out.println(D_LINE + "             `~~~~~~~--..........--~~~~~~~'");
        System.out.print("» Hub Command: ");

        handleInput(scanner.nextLine());

    }

    private static void printHubBanner() {
        System.out.println("\n" + STARS);
        printHeader("L I B R A R Y    H U B    C O N S O L E");
    }

    private static void handleInput(String input) {
        switch (input) {
            case "1": openCreateModule(); break;
            case "2": openSearchModule(); break;
            case "3": openBorrowModule(); break;
            case "4": openModifyModule(); break;
            case "0": 
                System.out.println("\n[!] Session Terminated. Closing Hub...");
                System.exit(0); 
                break;
            default:
                System.out.println("\n[!] Invalid Hub Command. Please try again.");
        }
    }

    // --- REFACTORED MODULES USING DRY PRINCIPLE ---

    private static void openCreateModule() {
        renderModule("B O O K    C R E A T I O N", 
            "Details [1]", "Enter Title, Author, and ISBN",
            "Save    [2]", "Commit Book to the Database");
    }

    private static void openModifyModule() {
        renderModule("B O O K    M O D I F I C A T I O N", 
            "Update  [1]", "Update details such as author or ISBN",
            "Undo    [2]", "Revert the most recent modification");
    }

    private static void openSearchModule() {
        renderModule("B O O K    S E A R C H", 
            "Search  [1]", "Search by title, author, or ISBN",
            "Sort    [2]", "Ascending or Descending by title");
    }

    private static void openBorrowModule() {
        renderModule("B O O K    B O R R O W I N G", 
            "Borrow  [1]", "Borrow a book from the library",
            "Return  [2]", "Return a borrowed book");
    }

    /**
     * Reusable helper to render sub-module menus consistently.
     */
    private static void renderModule(String title, String op1, String desc1, String op2, String desc2) {
        System.out.println();
        printHeader(title);
        System.out.format(MODULE_ROW, op1, desc1);
        System.out.format(MODULE_ROW, op2, desc2);
        System.out.println(S_LINE);
        System.out.format(MODULE_ROW, "Back    [0]", "Return to the Primary Hub");
        System.out.println(D_LINE);
        System.out.print("» Selection: ");
        scanner.nextLine(); // Wait for user acknowledgment or back command
    }

    private static void printHeader(String title) {
        System.out.println(D_LINE);
        int padding = (D_LINE.length() - title.length()) / 2;
        System.out.println(" ".repeat(Math.max(0, padding)) + title);
        System.out.println(D_LINE);
    }
}