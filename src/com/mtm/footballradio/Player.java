package com.mtm.footballradio;

public class Player {
    int id;
    String name; // Name
    Team team;
    int IW1; // Schuss / Shoot
    int IW2; // Pass / Pass
    int IW3; // Dribbeln / Dribbel
    int IW4; // Abwehr / Defense
    int IW5; // Fangen (nur fuer Torwart) / Catch (only for Keeper
    int goals;

    // Constructor
    public Player(int playerId, boolean isGoalkeeper, char teamChar) {
        id = playerId;
        if (playerId < 10){
            name = Names.getRandomPlayerName() + " (" + teamChar + "  " + playerId + ")";
        } else {
            name = Names.getRandomPlayerName() + " (" + teamChar + " " + playerId + ")";
        }
        IW1 = (int) (Math.random()*26)+25;
        IW2 = (int) (Math.random()*26)+25;
        IW3 = (int) (Math.random()*26)+25;
        IW4 = (int) (Math.random()*26)+25;
        if (isGoalkeeper) {
            IW5 = (int) (Math.random() * 26) + 25;
        }
        goals = 0;
    }

    public enum ActionType {
        Shoot, Pass, Dribble, Defend, Catch
    }

    public boolean performAction(ActionType actionType, int bonus) {
        int skillValue = 0;
        switch (actionType) {
            case Shoot -> skillValue = this.IW1;
            case Pass -> skillValue = this.IW2;
            case Dribble -> skillValue = this.IW3;
            case Defend -> skillValue = this.IW4;
            case Catch -> skillValue = this.IW5;
        }
        // Calculate Success
        int chance = (int) (Math.random() * 100) + 1;
        return skillValue + bonus >= chance;
    }
}
