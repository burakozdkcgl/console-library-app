package com.example;

import java.util.Scanner;

/**
 * HubDisplay handles all User Interface logic and application flow.
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
        
        formatHubRow("Create", "1", "Initialize Book Creation Wizard",     "      __...--~~~~~-._    _.-~~~~--..._");
        formatHubRow("Search", "2", "Access Global Search & Sort Engine", "    //                `V'              \\\\");
        formatHubRow("Borrow", "3", "Inventory Borrowing & Return Portal","   //                  |                \\\\");
        formatHubRow("Modify", "4", "System Management & Undo Operations","  //__...--~~~~~~-.___ | _.-~~~~~~--...__\\\\");
        
        System.out.println(S_LINE + "     \\\\                  `-'                //"); 
        formatHubRow("Exit",   "0", "Terminate Session and Secure Data",  "   \\\\____...--~~~~~~~~~~~~~~~--...______//");
        
        System.out.println(D_LINE + "           `~~~~~~~--..........--~~~~~~~'");
        System.out.print("» Hub Command: ");
    }

    /**
     * Internal router to direct the flow based on user input.
     */
    private static void routeCommand(String input) {
        switch (input) {
            case "1": showCreateModule(); break;
            case "4": showModifyModule(); break;
            case "0": 
                System.out.println("\n[!] System shutting down. Goodbye!\n");
                isRunning = false; // Graceful exit from the loop
                break;
            default: 
                System.out.println("\n[!] Invalid Command. Please try again.");
        }
    }

    // --- Sub-Module Methods ---

    private static void showCreateModule() {
        startModule("B O O K    C R E A T I O N");
        printRow("Details", "1", "Enter Title, Author, and ISBN");
        printRow("Save",    "2", "Commit Book to the Database");
        endModule();
    }

    private static void showModifyModule() {
        startModule("B O O K    M O D I F I C A T I O N");
        printRow("Update",  "1", "Update details (author, year, etc.)");
        printRow("Undo",    "2", "Revert the most recent modification");
        endModule();
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
        scanner.nextLine(); // Wait for user input to go back
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