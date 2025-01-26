package com.mtm.footballradio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// TODO implement actionPenalty into chance calculations
// FIXME why 92 minutes of play?
// TODO DISPLAY OPTIONS
// TODO REFACTOR
// TODO Language EN & DE SWITCH
// TODO Later Implement AI voice for the text :)
// TODO ALL OTHER TODOs

public class Game {
    // Team[] playingTeams;
    Team team1;
    Team team2;
    Team startingTeam;
    Team attackingTeam;
    Team defendingTeam;
    Team temporaryTeam;
    Player ballCarrier;
    Player opponent;
    Player opponentKeeper;
    Player receivingPlayer;
    int bonus;
    boolean isOffenseSuccessful;
    boolean isDefenseSuccessful;
    private List<Goal> goals;
    private List<Player> injuredPlayers; // TODO How long they are injured for? I might need it to be public later

    public boolean coinFlip() {
        double result = Math.random();
        return 0.5 < result;
    }

    // Constructor
    public Game(int speed) throws InterruptedException, IOException {
        if (speed == 0) { // To avoid division by 0
            speed = 1;
        }
        team1 = new Team('*', 1);
        team2 = new Team('#', 1);
        this.goals = new ArrayList<>();
        team1.assembleTeam();
        team2.assembleTeam();
        team1.displayTeam();
        team2.displayTeam();
        System.out.print("Platzieren Sie Ihre Wetten, dann Bitte drücken Sie Enter um fortzufahren...\n\n");
        System.in.read();
        // Determine starting Team
        if (coinFlip()) {
            startingTeam = team1;
            attackingTeam = team1;
            defendingTeam = team2;
        } else {
            startingTeam = team2;
            attackingTeam = team2;
            defendingTeam = team1;
        }
        // Select random Player to start kickoff
        ballCarrier = attackingTeam.getRandomFieldPlayerInPlay();
        // DISPLAY START
        Match(0,45, speed);
        System.out.print("\n>>> Pfeife!!! Es ist Halbzeit!\n");
        Game.displayStanding(team1,team2);
        Thread.sleep(4000 / speed);
        // Swap Teams & Reset Bonus
        bonus = 0;
        if (startingTeam.name.equals(team1.name)) {
            attackingTeam = team2;
            defendingTeam = team1;
        } else {
            attackingTeam = team1;
            defendingTeam = team2;
        }
        // Select random Player to start kickoff TODO Refactor START
        ballCarrier = attackingTeam.getRandomFieldPlayerInPlay();
        // DISPLAY START
        System.out.println(">>> Die zweite Halbzeit beginnt!");
        Match(45,45, speed);
        // DISPLAY END RESULT TODO Refactor END
        System.out.print("\n>>> Pfeife!!! Das Spiel ist vorbei!\n");
        Game.displayStanding(team1,team2);
        if (team1.score > team2.score) {
            System.out.print("\n>>> " + team1.longName + " hat gewonnen!\n\n");
        } else if (team2.score > team1.score){
            System.out.print("\n>>> " + team2.longName + " hat gewonnen!\n\n");
        } else {
            System.out.print("\n>>> Es ist ein Unentschieden!");
            Thread.sleep(2000 / speed);
            System.out.print("\n>>> Es beginnt eine 2-mal 15-minütige Nachspielzeit.\n");
            Thread.sleep(2000 / speed);
            attackingTeam = startingTeam;
            if (team1.equals(attackingTeam)) {
                defendingTeam = team2;
            } else {
                defendingTeam = team1;
            }
            ballCarrier = attackingTeam.getRandomFieldPlayerInPlay();
            // DISPLAY START
            Match(90,15, speed);
            System.out.print("\n>>> Pfeife!!! Die erste Hälfte der Verlängerung ist beendet!\n");
            Game.displayStanding(team1,team2);
            Thread.sleep(2000 / speed);
            System.out.print("\n>>> Die zweite Halbzeit der Verlängerung beginnt!\n");
            Thread.sleep(2000 / speed);
            // Swap Teams & Reset Bonus
            bonus = 0;
            if (startingTeam.name.equals(team1.name)) {
                attackingTeam = team2;
                defendingTeam = team1;
            } else {
                attackingTeam = team1;
                defendingTeam = team2;
            }
            // Select random Player to start kickoff TODO Refactor START
            ballCarrier = attackingTeam.getRandomFieldPlayerInPlay();
            // DISPLAY START
            Match(105,15, speed);
            // DISPLAY END RESULT TODO Refactor END
            System.out.print("\n>>> Pfeife!!! Ende der Verlängerung!\n");
            Game.displayStanding(team1,team2);
            if (team1.score > team2.score) {
                System.out.print("\n>>> " + team1.longName + " hat gewonnen!\n\n");
            } else if (team2.score > team1.score){
                System.out.print("\n>>> " + team2.longName + " hat gewonnen!\n\n");
            } else {
                System.out.print("\n>>> Es ist ein Unentschieden!\n");
                Thread.sleep(2000 / speed);
                System.out.print("\n>>> Das Elfmeterschießen beginnt!.\n");
                Thread.sleep(2000 / speed);
                Penalty(120, speed);
                System.out.print("\n>>> Ende des Elfmeterschießens!\n");
                Game.displayStanding(team1, team2);
                if (team1.score > team2.score) {
                    System.out.print("\n>>> " + team1.longName + " hat gewonnen!\n\n");
                } else if (team2.score > team1.score) {
                    System.out.print("\n>>> " + team2.longName + " hat gewonnen!\n\n");
                } else {
                    System.out.print("\n>>> Es ist ein Unentschieden!\n");
                    // This should never happen!
                }
            }
        }

        // DISPLAY GOALS // TODO Refactor to function, DISPLAY Table, condition is not needed anymore
        Game.displayGoals(goals);
        team1.displayTeamWithResults();
        team2.displayTeamWithResults();
    }

    // TODO integrate injury mechanic
    //  => check each minute all players and if a player.timeInPlay % 15 == 0 than roll dice for 3%
    //  => just realised timeInPlay must not be 0 too
    public void Match(int startingMinute, int length, int speed) throws InterruptedException {
        System.out.print("\n>>> " + ballCarrier.name + " von " + attackingTeam.longName + " hat Anstoß!");
        for (int minutes = startingMinute; minutes <= startingMinute+length; minutes ++) {
            // CHECK Injuries & REPLACE Injured Players
            checkInjuriesForTeam(team1);
            replaceInjuredPlayersForTeam(team1);
            checkInjuriesForTeam(team2);
            replaceInjuredPlayersForTeam(team2);
            // SET Penalty for Team with fewer than 11 players
            setPenaltyForTeam(team1);
            setPenaltyForTeam(team2);
            Thread.sleep(1000 / speed);
            if (minutes < 10) {
                System.out.print("\n  " + minutes + ". Minute - " + team1.score + ":" + team2.score + " -");
            } else if (minutes < 100) {
                System.out.print("\n " + minutes + ". Minute - " + team1.score + ":" + team2.score + " -");
            } else {
                System.out.print("\n" + minutes + ". Minute - " + team1.score + ":" + team2.score + " -");
            }
            // Determine Opponent
            opponent = defendingTeam.getRandomFieldPlayerInPlay();
            // It is more efficient to calculate Defensive Action's Success beforehand
            isDefenseSuccessful = opponent.performAction(Player.ActionType.Defend, 0);
            // Determine Action to be taken and calculate it's Success
            int offenseAction = (int) (Math.random() * 100) + 1;
            if (offenseAction < 11) {
                // Ball Carrier Shoots to attempt to score a Goal
                isOffenseSuccessful = ballCarrier.performAction(Player.ActionType.Shoot, bonus);
                System.out.print(" " + ballCarrier.name + " schießt");
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(200 / speed);
                    System.out.print(".");
                }
                if (isOffenseSuccessful && !isDefenseSuccessful){
                    // Shooter succeeds & Defense Fails
                    // opponent Team's Goal Keeper has a chance to Catch the Ball
                    opponentKeeper = defendingTeam.getGoalkeeper();
                    if (!opponentKeeper.performAction(Player.ActionType.Catch, 0)){
                        System.out.print(" GOOOAAAL!!! " + ballCarrier.name + " von " + attackingTeam.longName + " hat ein Tor geschossen!");
                        attackingTeam.score++;
                        ballCarrier.goals++;
                        ballCarrier.matchGoals++;
                        goals.add(new Goal(minutes, team1.score + ":" + team2.score, ballCarrier.name, attackingTeam.longName, false));
                    } else {
                        // Goal Keeper catches the ball!
                        System.out.print(" Er schießt sehr gut! Der Ball fliegt an der Verteidigung vorbei, aber  " + opponentKeeper.name + " fängt den Ball!");
                    }
                    // In either way, the Teams are swapped and the bonus is reset
                    // Keeper passes the Ball to one of his Teammates
                    ballCarrier = defendingTeam.getRandomFieldPlayerInPlay();
                } else if (isOffenseSuccessful && isDefenseSuccessful) { // TODO CHECK Conditions
                    // Shooter succeeds & Defense succeeds
                    System.out.print(" Schöner Schuss! Aber " + opponent.name + " blockt den Schuss und schnappt sich den Ball!");
                    ballCarrier = opponent;
                } else if (!isOffenseSuccessful && isDefenseSuccessful) { // TODO CHECK Conditions
                    // Shooter Fails & Defense succeeds
                    System.out.print(" Er schießt daneben, und " + opponent.name + " nimmt den Ball!");
                    ballCarrier = opponent;
                } else if (!isOffenseSuccessful && !isDefenseSuccessful) { // TODO CHECK Conditions
                    // Both Fail
                    System.out.print(" Er verfehlt den Schuss, kann aber den Ball halten.");
                    bonus = 0; // After a Shot, reset Bonus, even if he keeps the ball
                    increaseTimeInPlayForTeam(team1);
                    increaseTimeInPlayForTeam(team2);
                    continue; // Not to swap the Teams
                }
                // Swap Teams
                temporaryTeam = defendingTeam;
                defendingTeam = attackingTeam;
                attackingTeam = temporaryTeam;
                bonus = 0; // After a Shot, reset Bonus
            } else if (80 < offenseAction) {
                // Ball Carrier Dribbles forward
                isOffenseSuccessful = ballCarrier.performAction(Player.ActionType.Dribble, bonus);
                if (isOffenseSuccessful && !isDefenseSuccessful) {
                    // Dribble forward
                    System.out.print(" " + ballCarrier.name + " dribbelt nach vorne und weicht " + opponent.name + " aus.");
                    bonus = bonus + 5;
                } else if (isOffenseSuccessful && isDefenseSuccessful) {
                    // Dribble forward but tackled (TODO SAME AS MANY OTHER, BUT DIFFERENT COMMENT)
                    System.out.print(" " + ballCarrier.name + " dribbelt nach vorne, aber " + opponent.name + " greift ihn an und schnappt sich den Ball!");
                    ballCarrier = opponent;
                    // Swap Teams
                    bonus = 0;
                    temporaryTeam = defendingTeam;
                    defendingTeam = attackingTeam;
                    attackingTeam = temporaryTeam;
                } else if (!isOffenseSuccessful && isDefenseSuccessful) {
                    // Dribble Fail, opponent takes the Ball (TODO SAME AS MANY OTHER, BUT DIFFERENT COMMENT)
                    System.out.print(" " + ballCarrier.name + " kann sich nicht vorrücken, und " + opponent.name + " nimmt ihm den Ball weg!");
                    ballCarrier = opponent;
                    // Swap Teams
                    bonus = 0;
                    temporaryTeam = defendingTeam;
                    defendingTeam = attackingTeam;
                    attackingTeam = temporaryTeam;
                } else if (!isOffenseSuccessful && !isDefenseSuccessful) {
                    // Both fail
                    System.out.print(" " + ballCarrier.name + " kann nicht nach vorne dribbeln, aber " + opponent.name + " kann ihm den Ball nicht abgrätschen.");
                }
            } else {
                // Ball Carrier Passes the Ball to a Teammate
                receivingPlayer = attackingTeam.getRandomOtherFieldPlayerInPlayFromTeam(ballCarrier);
                isOffenseSuccessful = ballCarrier.performAction(Player.ActionType.Pass, bonus);
                if (isOffenseSuccessful && !isDefenseSuccessful) {
                    // Manages to Pass
                    System.out.print(" " + ballCarrier.name + " passt den Ball zu " + receivingPlayer.name + ".");
                    ballCarrier = receivingPlayer;
                    bonus = bonus + 5;
                } else if (isOffenseSuccessful && isDefenseSuccessful) {
                    // Good Pass, but it is intercepted (TODO SAME AS MANY OTHER, BUT DIFFERENT COMMENT)
                    System.out.print(" " + ballCarrier.name + " passt den Ball zu " + receivingPlayer.name + ", aber " + opponent.name + " fängt den Ball ab!");
                    ballCarrier = opponent;
                    // Swap Teams
                    bonus = 0;
                    temporaryTeam = defendingTeam;
                    defendingTeam = attackingTeam;
                    attackingTeam = temporaryTeam;
                } else if (!isOffenseSuccessful && isDefenseSuccessful) {
                    // Pass Fail, opponent takes the Ball (TODO SAME AS MANY OTHER, BUT DIFFERENT COMMENT)
                    System.out.print(" " + ballCarrier.name + " versucht, den Ball zu " + receivingPlayer.name + " passen, aber er verfehlt und "+ opponent.name + " fängt den Ball ab.");
                    ballCarrier = opponent;
                    // Swap Teams
                    bonus = 0;
                    temporaryTeam = defendingTeam;
                    defendingTeam = attackingTeam;
                    attackingTeam = temporaryTeam;
                } else if (!isOffenseSuccessful && !isDefenseSuccessful) {
                    // Both fail
                    System.out.print(" " + ballCarrier.name + " versucht, den Ball zu " + receivingPlayer.name + " passen, aber er verfehlt. " + opponent.name + " versucht, den Ball abzufangen, aber der Ball springt zu " + ballCarrier.name + " zurück!");
                }
            }
            increaseTimeInPlayForTeam(team1);
            increaseTimeInPlayForTeam(team2);
        }
    }

    // Penalty Shoot-out
    public void Penalty(int startingMinute, int speed) throws InterruptedException {
        // TODO sort Players according to their shooting skill
        boolean draw = true;
        boolean isShootingSuccessful;
        boolean isCatchingSuccessful;
        List<Player> sortedShooters1 = new ArrayList<>(team1.getPlayers());
        sortedShooters1.sort((p1, p2) -> Integer.compare(p2.IW1, p1.IW1));
        Player goalKeeper1 = team1.getGoalkeeper();
        List<Player> sortedShooters2 = new ArrayList<>(team2.getPlayers());
        sortedShooters2.sort((p1, p2) -> Integer.compare(p2.IW1, p1.IW1));
        Player goalKeeper2 = team2.getGoalkeeper();
        int rounds = startingMinute + 1;
        int counter = 0;
        do {
            System.out.print("\n" + rounds + ". Minute - " + team1.name + " - " + team2.name + " " + team1.score + ":" + team2.score);
            System.out.print(" " + sortedShooters1.get(counter).name + " schießt");
            for (int i = 0; i < 5; i++) {
                Thread.sleep(100 / speed);
                System.out.print(".");
            }
            isShootingSuccessful = sortedShooters1.get(counter).performAction(Player.ActionType.Shoot, 0);
            isCatchingSuccessful = goalKeeper2.performAction(Player.ActionType.Catch, 0);
            if (isShootingSuccessful && !isCatchingSuccessful) {
                System.out.print(" GOOOAAAL!!!");
                team1.score++;
                sortedShooters1.get(counter).goals++;
                sortedShooters1.get(counter).matchGoals++;
                goals.add(new Goal(rounds, team1.score + ":" + team2.score, sortedShooters1.get(counter).name, team1.longName, true));
            } else if (isShootingSuccessful && isCatchingSuccessful) {
                System.out.print(" Schöner Schuss! Aber " + goalKeeper2.name + " fängt den Ball!");
            } else {
                System.out.print(" Er schießt daneben!");
            }
            Thread.sleep(1000 / speed);
            rounds++;
            // Repeat with other Team
            System.out.print("\n" + rounds + ". Minute - " + team1.name + " - " + team2.name + " " + team1.score + ":" + team2.score);
            System.out.print(" " + sortedShooters2.get(counter).name + " schießt");
            for (int i = 0; i < 5; i++) {
                Thread.sleep(100 / speed);
                System.out.print(".");
            }
            isShootingSuccessful = sortedShooters2.get(counter).performAction(Player.ActionType.Shoot, 0);
            isCatchingSuccessful = goalKeeper1.performAction(Player.ActionType.Catch, 0);
            if (isShootingSuccessful && !isCatchingSuccessful) {
                System.out.print(" GOOOAAAL!!!");
                team2.score++;
                sortedShooters2.get(counter).goals++;
                sortedShooters1.get(counter).matchGoals++;
                goals.add(new Goal(rounds, team1.score + ":" + team2.score, sortedShooters2.get(counter).name, team2.longName, true));
            } else if (isShootingSuccessful && isCatchingSuccessful) {
                System.out.print(" Schöner Schuss! Aber " + goalKeeper1.name + " fängt den Ball!");
            } else {
                System.out.print(" Er schießt daneben!");
            }
            Thread.sleep(1000 / speed);
            rounds++;
            counter++;
            if (counter > 10) {
                // if all Players have shot, start again
                counter = 0;
            }
        } while (rounds < 132 || (team1.score == team2.score && rounds >= 132)); // FIXME if after 5 Players from each Team have taken a shot and it is not a draw then exit
    }

    static class Goal {
        int minute;
        String standing;
        String playerName;
        String clubName;
        boolean isPenalty;

        // Constructor
        public Goal(int minute, String standing, String playerName, String clubName, boolean isPenalty) {
            this.minute = minute;
            this.standing = standing;
            this.playerName = playerName;
            this.clubName = clubName;
            this.isPenalty = isPenalty;
        }
    }

    // DISPLAY STANDING
    static void displayStanding(Team team1, Team team2) {
        System.out.println("\n┌--------------------------------┬--------------------------------┐");
        System.out.printf("%1s %30s %1s %-30s %1s","│", team1.longName, "│", team2.longName, "│\n");
        System.out.println("├--------------------------------┼--------------------------------┤");
        System.out.printf("%1s %30s %1s %-30s %1s","│", team1.score, "│", team2.score, "│\n");
        System.out.println("└--------------------------------┴--------------------------------┘\n");
    }

    // DISPLAY GOALS
    static void displayGoals(List<Goal> goals) {
        System.out.println("\n┌----------------------------------------------------------------------------------------------------┐");
        System.out.println("│                                             Tore                                                   │");
        System.out.println("├------┬-------------------┬--------------------------------┬-----------------------------┬----------┤");
        for (Goal goal : goals) {
            if (goal.isPenalty) {
                System.out.printf("%1s %4s %1s %17s %1s %30s %1s %27s %1s %8s %1s%n", "│", goal.standing, "│", goal.minute + ". Spielminute", "│", goal.playerName, "│", goal.clubName, "│", "Elfmeter", "│");
            } else {
                System.out.printf("%1s %4s %1s %17s %1s %30s %1s %27s %1s %8s %1s%n", "│", goal.standing, "│", goal.minute + ". Spielminute", "│", goal.playerName, "│", goal.clubName, "│",  "    ", "│");
            }
        }
        System.out.println("└------┴-------------------┴--------------------------------┴-----------------------------┴----------┘\n");

        //Tore:
        //72. Spielminute, Tor 0:1, Eliseo Guinn (#  9), Udinese
    }

    // CHECK Injury for Team
    static void checkInjuriesForTeam(Team team) {
        for (Player player : team.getPlayers()) {
            if (player.isInPlay && player.timeInPlay != 0 && player.timeInPlay % 15 == 0) {
                // Roll Dice for the 3% chance of getting injured
                if ((int) (Math.random() * 100) + 1 <= 3) { // could be ((int) (Math.random() * 100) < 3) but as its a 100 sided cube, it should not have a 0 side
                    player.isInjured = true;
                    //player.isInPlay = false; // we need this condition for the replacement
                    player.status = "Verletzt";
                    // DISPLAY Injury
                    System.out.print("\n>>> " + player.name + " wurde verletzt!");
                }
            }
        }
    }

    // REPLACE Injured Players
    static void replaceInjuredPlayersForTeam(Team team) {
        for (Player player : team.getPlayers()) {
            if (player.isInPlay && player.isInjured) {
                // Find substitute for Player
                if (player.isGoalkeeper && player.injury != -25) { // we need to check if we have already tried to replace him but couldn't
                    // Check if there is a Substitute Goalkeeper
                    for (Player substitute : team.getSubstitutes()) {
                        if (substitute.isGoalkeeper) {
                            player.isInPlay = false;
                            substitute.isInPlay = true;
                            substitute.status = "In Spiel";
                            System.out.print(" " + substitute.name + " wurde als Ersatz für " + player.name + " eingestellt.");
                            break;
                        }
                    }
                    // FIXME the code comes here even for the first goalkeeper change
                    // No Substitute Goalkeeper is found => TODO Team can't play ?
                    // Leave Goalkeeper in play with 25% penalty to his skills ?
                    // player.isInPlay = true;
                    player.setInjury(-25);
                    System.out.print(" Da es keinen Ersatztorhüter gibt, wird " + player.name + " als verletzt (mit Strafstoß) weiterspielen.");
                    continue;
                }
                // Substitute field Player
                for (Player substitute : team.getSubstitutes()) {
                    if (!substitute.isGoalkeeper) {
                        player.isInPlay = false;
                        substitute.isInPlay = true;
                        substitute.status = "In Spiel";
                        System.out.print(" " + substitute.name + " wurde als Ersatz für " + player.name + " eingestellt.");
                        break;
                    }
                }
            }
        }
    }

    // CHECK In Play Players for Team and SET Penalty accordingly
    static void setPenaltyForTeam(Team team) {
        int amountOfPlayersInPlay = team.getAmountOfPlayersInPlay();
        team.actionPenalty = -5 * (11 - team.getAmountOfPlayersInPlay());
        if (amountOfPlayersInPlay < 11) {
            System.out.print(" Da Mannschaft " + team.longName + " mit " + team.getAmountOfPlayersInPlay() + " Spielern antritt, haben sie es schwer.");
        }
    }

    // INCREASE Player's timeInPlay counter
    static void increaseTimeInPlayForTeam(Team team) {
        for (Player player : team.getPlayers()) {
            if (player.isInPlay) { //  && !player.isInjured => Goalkeepers can keep on playing if there are no substitutes for them
                player.timeInPlay++;
            }
        }
    }

    static void displayGameEvents(){
        // TODO refactor System.out lines here
        // there are many ways to make commentary more varied and built from random blocks
        // if this is done, many actions which are the same for different results could be refactored too
        // as only the commentaries are different
        // TODO OPTION to display calculated values
        // TODO Modify System.out.print lines to this format: perhaps without the Team part
        // 121. Minute │ Team * │ Team # │ 1:1 │ >>> Tavon Bermudez (* 11) schießt..... Er schießt daneben!
        // 122. Minute │ Team * │ Team # │ 1:1 │ >>> Stetson Shoemaker (#  9) schießt..... Er schießt daneben!
        // 123. Minute │ Team * │ Team # │ 1:1 │ >>> Gannon Craven (*  5) schießt..... GOOOAAAL!!!
        // 124. Minute │ Team * │ Team # │ 2:1 │ >>> Grant Willett (#  1) schießt..... Er schießt daneben!
        // 125. Minute │ Team * │ Team # │ 2:1 │ >>> Rashad Alexander (*  8) schießt..... Er schießt daneben!
    }
}