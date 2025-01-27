package com.mtm.footballradio;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("                           ┌---------------------------------------┐");
        System.out.println("                           │     Willkommen beim Fußballradio!     │");
        System.out.println("                           └---------------------------------------┘");
        if (args.length > 0) {
            if (args[0].equals("faster")) {
                Game startGame = new Game(100);
            } else if (args[0].equals("fast")) {
                Game startGame = new Game(10);
            }
        } else {
            Game startGame = new Game(1);
        }
    }
    public static final String C_RESET = "\u001B[0m";
    public static final String C_BLACK = "\u001B[30m";
    public static final String C_RED = "\u001B[31m";
    public static final String C_GREEN = "\u001B[32m";
    public static final String C_YELLOW = "\u001B[33m";
    public static final String C_BLUE = "\u001B[34m";
    public static final String C_PURPLE = "\u001B[35m";
    public static final String C_CYAN = "\u001B[36m";
    public static final String C_WHITE = "\u001B[37m";
    public static final String B_BLACK = "\u001B[40m";
    public static final String B_RED = "\u001B[41m";
    public static final String B_GREEN = "\u001B[42m";
    public static final String B_YELLOW = "\u001B[43m";
    public static final String B_BLUE = "\u001B[44m";
    public static final String B_PURPLE = "\u001B[45m";
    public static final String B_CYAN = "\u001B[46m";
    public static final String B_WHITE = "\u001B[47m";
}
