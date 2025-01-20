package com.mtm.footballradio;

import java.util.*;
// import java.util.ArrayList;

public class Team {
    String name;
    String longName;
    int score;
    private List<Player> players;

    // Constructor
    public Team(char abc, int counter) {
        this.name = "Team " + abc;
        this.longName = Names.getRandomClubName();
        this.score = 0;
        this.players = new ArrayList<>();
        players.add(new Player(counter, true, abc));
        counter++;
        for (int i = 0; i < 10; i++){
            players.add(new Player(counter, false, abc));
            counter++;
        }
    }

    // Get Goal Keeper Player
    public Player getGoalKeeper() {
        return players.get(0);
    }

    // Checks if Player is member of a Team
    public boolean isMember(Player player) {
        return players.contains(player);
    }

    // Gets a random Field Player from the Team
    public Player getRandomPlayer() {
        int randomIndex = (int) (Math.random() * 10) + 1; // + 1 so it jumps over the GoalKeeper
        return players.get(randomIndex);
    }

    // Gets a random Field Player from the Team, except the Ball Carrier for Passing
    public Player getRandomOtherPlayer(Player ballCarrier) {
        int randomIndex;
        boolean isSamePlayer;
        do {
            randomIndex = (int) (Math.random() * 10) + 1; // + 1 so it jumps over the GoalKeeper
            isSamePlayer = players.get(randomIndex).equals(ballCarrier);
        }
        while (isSamePlayer);
        return players.get(randomIndex);
    }

    // Get all players
    public List<Player> getPlayers() {
        return players;
    }

    // Get 5 of the best shooters from the Team
    // TODO Deprecated
    public Player[] getBestShooters() {
        int counter = 0;
        Player[] bestShooters;
        bestShooters = new Player[5];
        List<Player> sortedPlayersShootingSkill = new ArrayList<>(players);
        // Sort players by Shooting Skill (descending)
        sortedPlayersShootingSkill.sort((p1, p2) -> Integer.compare(p2.IW1, p1.IW1));
        // Add first 5 to the best shooters list
        while (counter < 5) {
            bestShooters[counter] = sortedPlayersShootingSkill.get(counter);
            counter++;
        }
        return bestShooters;
    }

    // DISPLAY Team Members and skills
    public void displayTeam() {
        System.out.println("┌--------------┬--------------------------------------------------------------┐");
        System.out.printf("%1s %10s %4s %-58s %3s","│", this.name, "│ ", this.longName, "│\n");
        System.out.println("├--------------┴---------------------┬-------┬-------┬--------┬-------┬-------┤");
        System.out.println("│         Name (Team Nummer)         │ Schuß │ Pass  │Dribbeln│Abwehr │Fangen │");
        System.out.println("├------------------------------------┼-------┼-------┼--------┼-------┼-------┤");
        for (Player each : players){
            System.out.printf("%1s %32s %3s %3d %3s %3d %3s %4d %3s %3d %3s %4d %2s%n","│", each.name, "│", each.IW1, "│", each.IW2, "│", each.IW3, "│", each.IW4, "│", each.IW5, "│");
        }
        // System.out.println("├------------------------------------┼-------┼-------┼--------┼-------┼-------┤");
        // System.out.println("│         Name (Team Nummer)         │ Schuß │ Pass  │Dribbeln│Abwehr │Fangen │");
        System.out.println("└------------------------------------┴-------┴-------┴--------┴-------┴-------┘\n");
    }

    // DISPLAY Team Members with skills and goals
    public void displayTeamWithResults() {
        System.out.println("┌--------------┬---------------------------------------------------┬----------┬-------┐");
        System.out.printf("%1s %10s %4s %-46s %3s %6s %3s %4d %3s","│", this.name, "│ ", this.longName, "│", "Tore", "│", this.score, "│\n");
        System.out.println("├--------------┴---------------------┬-------┬-------┬--------┬----┴--┬-------┼-------┤");
        System.out.println("│         Name (Team Nummer)         │ Schuß │ Pass  │Dribbeln│Abwehr │Fangen │ Tore  │");
        System.out.println("├------------------------------------┼-------┼-------┼--------┼-------┼-------┼-------┤");
        for (Player each : players){
            System.out.printf("%1s %32s %3s %3d %3s %3d %3s %4d %3s %3d %3s %3d %3s %4d %2s%n","│", each.name, "│", each.IW1, "│", each.IW2, "│", each.IW3, "│", each.IW4, "│", each.IW5, "│", each.goals, "│");
        }
        // System.out.println("├------------------------------------┼-------┼-------┼--------┼-------┼-------┼-------┤");
        // System.out.println("│         Name (Team Nummer)         │ Schuß │ Pass  │Dribbeln│Abwehr │Fangen │ Tore  │");
        System.out.println("└------------------------------------┴-------┴-------┴--------┴-------┴-------┴-------┘\n");
    }
}
