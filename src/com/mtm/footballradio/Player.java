package com.mtm.footballradio;

public class Player {
    int id;
    String name; // Name
    int IW1; // Schuss / Shoot
    int IW2; // Pass / Pass
    int IW3; // Dribbeln / Dribbel
    int IW4; // Abwehr / Defense
    int IW5; // Fangen (nur fuer Torwart) / Catch (only for Keeper
    int AVG;
    int goals;
    int matchGoals;
    int timeInPlay; // to calculate injury chance
    boolean isInMatch; // available to play or as reserve
    boolean isInPlay; // currently on the field
    boolean isInjured;
    boolean isGoalkeeper;
    String status;
    int injury;

    // Constructor
    public Player(int playerId, boolean makeGoalkeeper, char teamChar) {
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
        if (makeGoalkeeper) {
            IW5 = (int) (Math.random() * 26) + 25;
            AVG = (IW1 + IW2 + IW3 + IW4 + IW5) / 5;
        } else {
            AVG = (IW1 + IW2 + IW3 + IW4) / 4;
        }
        matchGoals = 0; // separated match goals from total goals for championship statistics
        timeInPlay = 0;
        isInMatch = false;
        isInPlay = false;
        isInjured = false;
        isGoalkeeper = makeGoalkeeper;
        status = "";
        injury = 0;
    }

    public int getId() {
        return this.id;
    }
    public int getIW1() {
        return this.IW1;
    }
    public int getIW2() {
        return this.IW2;
    }
    public int getIW3() {
        return this.IW3;
    }
    public int getIW4() {
        return this.IW4;
    }
    public int getIW5() {
        return this.IW5;
    }
    public int getAVG() {
        return this.AVG;
    }
    public int getmatchGoals() {
        return this.matchGoals;
    }
    public int getTimeInPlay() {
        return this.timeInPlay;
    }
    public boolean getIsInMatch() {
        return this.isInMatch;
    }
    public boolean getIsInPlay() {
        return this.isInPlay;
    }
    public boolean getIsGoalkeeper() {
        return this.isGoalkeeper;
    }
    public boolean getIsInjured() {
        return this.isInjured;
    }
    public String getStatus() {
        return this.status;
    }
    public int getInjury() {
        return this.injury;
    }
    public void setInjury(int amount) {
        this.injury = amount; // TODO should it be injury-amount or injury+amount?
    }

    public enum ActionType {
        Shoot, Pass, Dribble, Defend, Catch
    }

    // TODO not sure if i need this
    public enum PlayerStatus {
        Substitute, InMatch, Injured, Empty
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
        return skillValue + bonus - this.injury >= chance;
    }
}
