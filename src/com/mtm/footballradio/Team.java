package com.mtm.footballradio;

import java.util.*;
import java.util.stream.Collectors;
// import java.util.ArrayList;

public class Team {
    String name;
    String longName;
    int score;
    int actionPenalty;
    private List<Player> players;
    private List<Player> playersInMatch;
    private List<Player> playersInPlay;
    private List<Player> substitutes;

    // Constructor
    public Team(char abc, int counter) {
        this.name = "Team " + abc;
        this.longName = Names.getRandomClubName();
        this.score = 0;
        this.actionPenalty = 0;
        this.players = new ArrayList<>();
        this.playersInMatch = new ArrayList<>();
        this.playersInPlay = new ArrayList<>();
        this.substitutes = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            if (i<3){
                players.add(new Player(counter, true, abc));
            } else {
                players.add(new Player(counter, false, abc));
            }
            counter++;
        }
    }

    // Get Goal Keeper Player
    public Player getGoalkeeper() {
        return players.stream()
                .filter(p -> p.isGoalkeeper && p.isInPlay)
                .findFirst()
                .orElse(null);
    }

    // Checks if Player is member of a Team
    public boolean isMember(Player player) {
        return players.contains(player);
    }

    // Gets a random Field Player from the Team
    // TODO Deprecated
    public Player getRandomPlayer() {
        int randomIndex = (int) (Math.random() * 10) + 1;
        return players.get(randomIndex);
    }

    // Gets a random Field Player in Play from the Team
    public Player getRandomFieldPlayerInPlay() {
        int randomIndex = (int) (Math.random() * (getAmountOfPlayersInPlay() - 2)); // - 1 as we don't count the Goalkeeper - 1 for index 0
        return playersInMatch.stream()
                .filter(p -> p.isInPlay)
                .filter(p -> !p.isGoalkeeper)
                .collect(Collectors.toList())
                .get(randomIndex);
    }

    // Gets a random Field Player from the Team, except the Ball Carrier for Passing
    // TODO need to check isInMatch, !isInjured, isInPlay and also it is very inefficient
    // TODO Deprecated
    public Player getRandomOtherPlayer(Player ballCarrier) {
        int randomIndex;
        boolean isSamePlayer;
        do {
            randomIndex = (int) (Math.random() * 10) + 1; // + 1 so it jumps over the Goalkeeper
            isSamePlayer = players.get(randomIndex).equals(ballCarrier);
        }
        while (isSamePlayer);
        return players.get(randomIndex);
    }

    // Gets a random Field Player from the Team who is in Play (for Passing)
    public Player getRandomOtherFieldPlayerInPlayFromTeam(Player ballCarrier) {
        int randomIndex = (int) (Math.random() * (getAmountOfPlayersInPlay() - 2)); // - 1 as we don't count the Goalkeeper - 1 for index 0
        return playersInMatch.stream()
                .filter(p -> p.isInPlay)
                .filter(p -> !p.isGoalkeeper)
                .filter(p -> p != ballCarrier)
                .collect(Collectors.toList())
                .get(randomIndex);
    }

    // ASSEMBLE Team
    // TODO if no Goalkeepers are healthy Team can't play ?
    // TODO Goalkeepers should be sorted according to their catch skill
    public List<Player> assembleTeam() {
        int goalkeeperCount = 0;
        int playerCount = 0;
        int inPlayPlayerCount = 0;
        List<Player> sortedPlayers = new ArrayList<>(getPlayers());
        sortedPlayers.sort((p1, p2) -> Integer.compare(p2.IW5, p1.IW5));
        // Add first as InPlay Goalkeeper & Add second as substitute goalkeeper
        for (Player player : sortedPlayers) {
            if (goalkeeperCount == 2){
                break;
            }
            if (!player.isInjured && player.isGoalkeeper && goalkeeperCount < 3) { // only the 1. condition is absolutely necessary
                playersInMatch.add(player);
                player.timeInPlay = 0;
                player.matchGoals = 0;
                player.isInMatch = true;
                goalkeeperCount++;
                playerCount++;
                // Set first and best Goalkeeper to be in Play for the start
                if (goalkeeperCount == 1) {
                    player.isInPlay = true;
                    player.status = "Im Spiel";
                    playersInPlay.add(player);
                } else {
                    player.status = "Ersatz";
                }
            }
        }
        // Then for the rest we have to make sure the player os not inPlay
        sortedPlayers.sort((p1, p2) -> Integer.compare(p2.AVG, p1.AVG));
        for (Player player : sortedPlayers) {
            // Each team can only take 11 + 3 Substitutes to a Match
            if (playerCount == 14) {
                break;
            }
            if ( !player.isInjured && !player.isGoalkeeper) {
                playersInMatch.add(player);
                player.timeInPlay = 0;
                player.matchGoals = 0;
                player.isInMatch = true;
                playerCount++;
                // Set first (and best) 11 players to be in Play for the start
                if (inPlayPlayerCount < 10) {
                    player.isInPlay = true;
                    inPlayPlayerCount++;
                    player.status = "Im Spiel";
                    playersInPlay.add(player);
                } else {
                    player.status = "Ersatz";
                }
            }
        }
        if (goalkeeperCount == 0) {
            // This can only happen in the future with Tournaments
            // Team is unavailable
            // perhaps take an injured Goalkeeper with Penalty, like in play
            // TODO ASK Christoph! A solution would be that all players have Catch attribute and could substitute a Goalkeeper
            return null;
        }
        return playersInMatch;
    }

    // Get all Players
    public List<Player> getPlayers() {
        return players;
    }

    // Get amount of Players in Play
    public int getAmountOfPlayersInPlay() {
        return (int) playersInMatch.stream()
                .filter(p -> p.isInPlay).count();
    }

    // Get Substitutes
    public List<Player> getSubstitutes() {
        for (Player player : players) {
            if (player.isInMatch && !player.isInPlay && !player.isInjured) {
                substitutes.add(player);
            }
        }
        return substitutes;
    }

    // Get Methods to get Minimum of each attribute for the Players
    public int getMinIW1ForTeam() {
        int smallestIW1 = 50;
        for (Player player : players) {
            if (player.IW1 < smallestIW1) {
                smallestIW1 = player.IW1;
            }
        }
        return smallestIW1;
    }
    public int getMinIW2ForTeam() {
        int smallestIW2 = 50;
        for (Player player : players) {
            if (player.IW2 < smallestIW2) {
                smallestIW2 = player.IW2;
            }
        }
        return smallestIW2;
    }
    public int getMinIW3ForTeam() {
        int smallestIW3 = 50;
        for (Player player : players) {
            if (player.IW3 < smallestIW3) {
                smallestIW3 = player.IW3;
            }
        }
        return smallestIW3;
    }
    public int getMinIW4ForTeam() {
        int smallestIW4 = 50;
        for (Player player : players) {
            if (player.IW4 < smallestIW4) {
                smallestIW4 = player.IW4;
            }
        }
        return smallestIW4;
    }
    public int getMinIW5ForTeam() {
        int smallestIW5 = 50;
        for (Player player : players) {
            if (player.IW5 < smallestIW5 && player.isGoalkeeper) {
                smallestIW5 = player.IW5;
            }
        }
        return smallestIW5;
    }
    public int getMinAVGForTeam() {
        int smallestAVG = 50;
        for (Player player : players) {
            if (player.AVG < smallestAVG) {
                smallestAVG = player.AVG;
            }
        }
        return smallestAVG;
    }

    // Get Methods to get Maximum of each attribute for the Players
    public int getMaxIW1ForTeam() {
        int largestIW1 = 25;
        for (Player player : players) {
            if (player.IW1 > largestIW1) {
                largestIW1 = player.IW1;
            }
        }
        return largestIW1;
    }
    public int getMaxIW2ForTeam() {
        int largestIW2 = 25;
        for (Player player : players) {
            if (player.IW2 > largestIW2) {
                largestIW2 = player.IW2;
            }
        }
        return largestIW2;
    }
    public int getMaxIW3ForTeam() {
        int largestIW3 = 25;
        for (Player player : players) {
            if (player.IW3 > largestIW3) {
                largestIW3 = player.IW3;
            }
        }
        return largestIW3;
    }
    public int getMaxIW4ForTeam() {
        int largestIW4 = 25;
        for (Player player : players) {
            if (player.IW4 > largestIW4) {
                largestIW4 = player.IW4;
            }
        }
        return largestIW4;
    }
    public int getMaxIW5ForTeam() {
        int largestIW5 = 25;
        for (Player player : players) {
            if (player.IW5 > largestIW5 && player.isGoalkeeper) {
                largestIW5 = player.IW5;
            }
        }
        return largestIW5;
    }
    public int getMaxAVGForTeam() {
        int largestAVG = 25;
        for (Player player : players) {
            if (player.AVG > largestAVG) {
                largestAVG = player.AVG;
            }
        }
        return largestAVG;
    }

    // Get Methods to get Average of each attribute for the Players
    public int getAvgIW1ForTeam() {
        int playerCount = this.players.size();
        int totalIW1 = 0;
        for (Player player : players) {
            totalIW1 = totalIW1 + player.IW1;
        }
        return totalIW1 / playerCount;
    }
    public int getAvgIW2ForTeam() {
        int playerCount = this.players.size();
        int totalIW2 = 0;
        for (Player player : players) {
            totalIW2 = totalIW2 + player.IW2;
        }
        return totalIW2 / playerCount;
    }
    public int getAvgIW3ForTeam() {
        int playerCount = this.players.size();
        int totalIW3 = 0;
        for (Player player : players) {
            totalIW3 = totalIW3 + player.IW3;
        }
        return totalIW3 / playerCount;
    }
    public int getAvgIW4ForTeam() {
        int playerCount = this.players.size();
        int totalIW4 = 0;
        for (Player player : players) {
            totalIW4 = totalIW4 + player.IW4;
        }
        return totalIW4 / playerCount;
    }
    public int getAvgIW5ForTeam() {
        int playerCount = 0;
        int totalIW5 = 0;
        for (Player player : players) {
            if (player.isGoalkeeper) {
                totalIW5 = totalIW5 + player.IW5;
                playerCount++;
            }
        }
        return totalIW5 / playerCount;
    }
    public int getAvgAVGForTeam() {
        int playerCount = this.players.size();
        int totalAVG = 0;
        for (Player player : players) {
            totalAVG = totalAVG + player.AVG;
        }
        return totalAVG / playerCount;
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
        System.out.println("┌---------------┬--------------------------------------------------------------------------------┐");
        System.out.printf("%1s %13s %1s %-76s %3s","│", this.name, "│ ", this.longName, "│\n");
        System.out.println("├---------------┴--------------------┬-------┬-------┬--------┬-------┬-------┬-------┬----------┤");
        System.out.println("│         Name (Team Nummer)         │ Schuß │ Pass  │Dribbeln│Abwehr │Fangen │   Ø   │  Status  │");
        System.out.println("├------------------------------------┼-------┼-------┼--------┼-------┼-------┼-------┼----------┤");
        for (Player each : players){
            System.out.printf("%1s %32s %3s %3d %3s %3d %3s %4d %3s %3d %3s %3d %3s %3d %3s %-8s %1s%n","│", each.name, "│", each.IW1, "│", each.IW2, "│", each.IW3, "│", each.IW4, "│", each.IW5, "│", each.AVG, "│", each.status, "│");
        }
        System.out.println("├------------------------------------┼-------┼-------┼--------┼-------┼-------┼-------┼----------┤");
        System.out.printf("%1s %32s %3s %3d %3s %3d %3s %4d %3s %3d %3s %3d %3s %3d %3s %-8s %1s%n","│", "Höchstwerte", "│", this.getMaxIW1ForTeam(), "│", this.getMaxIW2ForTeam(), "│", this.getMaxIW3ForTeam(), "│", this.getMaxIW4ForTeam(), "│", this.getMaxIW5ForTeam(), "│", this.getMaxAVGForTeam(),  "│", " ", "│");
        System.out.println("├------------------------------------┼-------┼-------┼--------┼-------┼-------┼-------┼----------┤");
        System.out.printf("%1s %32s %3s %3d %3s %3d %3s %4d %3s %3d %3s %3d %3s %3d %3s %-8s %1s%n","│", "Durchschnittswerte", "│", this.getAvgIW1ForTeam(), "│", this.getAvgIW2ForTeam(), "│", this.getAvgIW3ForTeam(), "│", this.getAvgIW4ForTeam(), "│", this.getAvgIW5ForTeam(), "│", this.getAvgAVGForTeam(),  "│", " ", "│");
        System.out.println("├------------------------------------┼-------┼-------┼--------┼-------┼-------┼-------┼----------┤");
        System.out.printf("%1s %32s %3s %3d %3s %3d %3s %4d %3s %3d %3s %3d %3s %3d %3s %-8s %1s%n","│", "Mindestwerte", "│", this.getMinIW1ForTeam(), "│", this.getMinIW2ForTeam(), "│", this.getMinIW3ForTeam(), "│", this.getMinIW4ForTeam(), "│", this.getMinIW5ForTeam(), "│", this.getMinAVGForTeam(),  "│", " ", "│");
        System.out.println("└------------------------------------┴-------┴-------┴--------┴-------┴-------┴-------┴----------┘\n");
    }

    // DISPLAY Team Members with skills and goals TODO timeInPlay, Status, and all time goal columns
    public void displayTeamWithResults() {
        System.out.println("┌--------------┬-----------------------------------------------------------┬-----------------------┬-------┐");
        System.out.printf("%1s %10s %4s %-54s %3s %18s %2s %4d %3s","│", this.name, "│ ", this.longName, "│", "Tore der Mannschaft:", "│", this.score, "│\n");
        System.out.println("├--------------┴---------------------┬-------┬-------┬--------┬-------┬----┴--┬----------┬---------┼-------┤");
        System.out.println("│         Name (Team Nummer)         │ Schuß │ Pass  │Dribbeln│Abwehr │Fangen │  Status  │Spielzeit│ Tore  │");
        System.out.println("├------------------------------------┼-------┼-------┼--------┼-------┼-------┼----------┼---------┼-------┤");
        for (Player each : players){
            if (each.matchGoals > 0) {
                System.out.printf("%1s %32s %3s %3d %3s %3d %3s %4d %3s %3d %3s %3d %3s %-8s %1s %5d %3s %4d %2s%n", "│", each.name, "│", each.IW1, "│", each.IW2, "│", each.IW3, "│", each.IW4, "│", each.IW5, "│", each.status, "│", each.timeInPlay, "│", each.matchGoals, "│");
            } else {
                System.out.printf("%1s %32s %3s %3d %3s %3d %3s %4d %3s %3d %3s %3d %3s %-8s %1s %5d %3s %4s %2s%n", "│", each.name, "│", each.IW1, "│", each.IW2, "│", each.IW3, "│", each.IW4, "│", each.IW5, "│", each.status, "│", each.timeInPlay, "│",  " ", "│");
            }
        }
        // System.out.println("├------------------------------------┼-------┼-------┼--------┼-------┼-------┼-------┤");
        // System.out.println("│         Name (Team Nummer)         │ Schuß │ Pass  │Dribbeln│Abwehr │Fangen │ Tore  │");
        System.out.println("└------------------------------------┴-------┴-------┴--------┴-------┴-------┴----------┴---------┴-------┘\n");
    }
}
