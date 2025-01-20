package com.mtm.footballradio;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("\n\n--------------------------\nWelcome to Football Radio!\n--------------------------\n");
        if (args.length > 0) {
            if (args[0].equals("speed")) {
                Game startGame = new Game(100);
            } else if (args[0].equals("fast")) {
                Game startGame = new Game(10);
            }
        } else {
            Game startGame = new Game(1);
        }
    }
}
