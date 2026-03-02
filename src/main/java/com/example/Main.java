package com.example;

import java.util.Scanner;

public class Main {
    /**
     * Main application entry point.
     * Delegates all control flow to the HubDisplay engine.
     */
    public static void main(String[] args) {
        // Create the hub and start the application life cycle
        HubDisplay hub = new HubDisplay(new Scanner(System.in));
        hub.display();
    }
}