package com.mtm.footballradio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// TODO WAIT FOR USER INPUT IN THE BEGINNING
// TODO DISPLAY OPTIONS
// TODO SLEEP ADJUSTMENTS
// TODO REFACTOR
// TODO CHANGE TEAM A & B TO CLUB NAME SHORTS FOR DISPLAYS
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

    public boolean coinFlip() {
        double result = Math.random();
        return 0.5 < result;
    }

    // Constructor
    public Game(int speed) throws InterruptedException, IOException {
        if (speed == 0) {
            speed = 1;
        }
        team1 = new Team('A', 1);
        team2 = new Team('B', 1);
        this.goals = new ArrayList<>();
        // TODO Display TEAMS and PLAYERS wait for promt to start
        team1.displayTeam();
        team2.displayTeam();
        System.out.print("Platzieren Sie Ihre Wetten, dann Bitte drücken Sie Enter um fortzufahren...");
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
        ballCarrier = attackingTeam.getRandomPlayer();
        // DISPLAY START
        System.out.print("\n" + ballCarrier.name + " von " + attackingTeam.longName + " hat Anstoß!");
        Match(0,45, speed);
        System.out.print("\nPfeife!!! Es ist Halbzeit!\n"  + team1.name + " - " + team2.name + "\n     " + team1.score + " : " + team2.score + "\n\n");
        Thread.sleep(5000 / speed);
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
        ballCarrier = attackingTeam.getRandomPlayer();
        // DISPLAY START
        System.out.println("Die zweite Halbzeit beginnt!");
        System.out.print("\n" + ballCarrier.name + " von " + attackingTeam.longName + " hat Anstoß!");
        Match(45,45, speed);
        // DISPLAY END RESULT TODO Refactor END
        System.out.print("\nPfeife!!! Das Spiel ist vorbei!\nEndergebnis:\n"  + team1.name + " - " + team2.name + "\n     " + team1.score + " : " + team2.score + "\n");
        if (team1.score > team2.score) {
            System.out.print("\n" + team1.longName + " hat gewonnen!\n\n");
        } else if (team2.score > team1.score){
            System.out.print("\n" + team2.longName + " hat gewonnen!\n\n");
        } else {
            System.out.print("\nEs ist ein Unentschieden!");
            Thread.sleep(2000 / speed);
            System.out.print("\nEs beginnt eine 2-mal 15-minütige Nachspielzeit.\n");
            Thread.sleep(2000 / speed);
            attackingTeam = startingTeam;
            if (team1.equals(attackingTeam)) {
                defendingTeam = team2;
            } else {
                defendingTeam = team1;
            }
            ballCarrier = attackingTeam.getRandomPlayer();
            // DISPLAY START
            System.out.print(ballCarrier.name + " von " + attackingTeam.longName + " hat Anstoß!");
            Match(90,15, speed);
            System.out.print("\nPfeife!!! Die erste Hälfte der Verlängerung ist beendet!\n"  + team1.name + " - " + team2.name + "\n     " + team1.score + " : " + team2.score + "\n\n");
            Thread.sleep(2000 / speed);
            System.out.print("\nDie zweite Halbzeit der Verlängerung beginnt!\n");
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
            ballCarrier = attackingTeam.getRandomPlayer();
            // DISPLAY START
            System.out.print("\n" + ballCarrier.name + " von " + attackingTeam.longName + " hat Anstoß!");
            Match(105,15, speed);
            // DISPLAY END RESULT TODO Refactor END
            System.out.print("\nPfeife!!! Ende der Verlängerung!\nEndergebnis:\n"  + team1.name + " - " + team2.name + "\n     " + team1.score + " : " + team2.score + "\n");
            if (team1.score > team2.score) {
                System.out.print("\n" + team1.longName + " hat gewonnen!\n\n");
            } else if (team2.score > team1.score){
                System.out.print("\n" + team2.longName + " hat gewonnen!\n\n");
            } else {
                System.out.print("\nEs ist ein Unentschieden!\n");
                // TODO Penalty Shoot
                Thread.sleep(2000 / speed);
                System.out.print("\nDas Elfmeterschießen beginnt!.\n");
                Thread.sleep(2000 / speed);
                Penalty(120, speed);
            }
            System.out.print("\nEnde des Elfmeterschießens!\nEndergebnis:\n"  + team1.name + " - " + team2.name + "\n     " + team1.score + " : " + team2.score + "\n");
            if (team1.score > team2.score) {
                System.out.print("\n" + team1.longName + " hat gewonnen!\n\n");
            } else if (team2.score > team1.score){
                System.out.print("\n" + team2.longName + " hat gewonnen!\n\n");
            } else {
                System.out.print("\nEs ist ein Unentschieden!\n");
                // This should not happen!
            }
        }

        // DISPLAY GOALS
        if (goals.size() > 0) {
            System.out.println("Tore:");
            for (Goal goal : goals) {
                if (goal.isPenalty) {
                    System.out.println(goal.minute + ". Spielminute, " + goal.standing + ", " + goal.playerName + ", " + goal.clubName + ", Elfmeter");
                } else {
                    System.out.println(goal.minute + ". Spielminute, " + goal.standing + ", " + goal.playerName + ", " + goal.clubName);
                }
            }
            System.out.println("\n");
        }
        team1.displayTeamWithResults();
        team2.displayTeamWithResults();
    }

    public void Match(int startingMinute, int length, int speed) throws InterruptedException {
        for (int minutes = startingMinute; minutes <= startingMinute+length; minutes ++) {
            Thread.sleep(1000 / speed);
            if (minutes < 10) {
                System.out.print("\n  " + minutes + ". Minute - " + team1.score + ":" + team2.score + " -");
            } else if (minutes < 100) {
                System.out.print("\n " + minutes + ". Minute - " + team1.score + ":" + team2.score + " -");
            } else {
                System.out.print("\n" + minutes + ". Minute - " + team1.score + ":" + team2.score + " -");
            }
            // TODO ? only display this after change of ballCarrier - System.out.print(" - " + ballCarrier.name + " hat den Ball!");
            // Determine Opponent
            opponent = defendingTeam.getRandomPlayer();
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
                    opponentKeeper = defendingTeam.getGoalKeeper();
                    if (!opponentKeeper.performAction(Player.ActionType.Catch, 0)){
                        System.out.print(" GOOOAAAL!!! " + ballCarrier.name + " von " + attackingTeam.longName + " hat ein Tor geschossen!");
                        attackingTeam.score++;
                        ballCarrier.goals++;
                        goals.add(new Goal(minutes, "Tor " + team1.score + ":" + team2.score, ballCarrier.name, attackingTeam.longName, false));
                    } else {
                        // Goal Keeper catches the ball!
                        System.out.print(" Er schießt sehr gut! Der Ball fliegt an der Verteidigung vorbei, aber  " + opponentKeeper.name + " fängt den Ball!");
                    }
                    // In either way, the Teams are swapped and the bonus is reset
                    // Change possession of the Ball
                    ballCarrier = defendingTeam.getRandomPlayer();
                    // System.out.print("\n" + ballCarrier.name + " hat den Ball!");
                } else if (isDefenseSuccessful) {
                    // Shooter succeeds & Defense succeeds (TODO SAME AS NEXT, BUT DIFFERENT COMMENT NEEDED)
                    System.out.print(" Schöner Schuss! Aber " + opponent.name + " blockt den Schuss und schnappt sich den Ball!");
                    ballCarrier = opponent;
                    // System.out.print("\n" + ballCarrier.name + " hat den Ball!");
                } else if (!isOffenseSuccessful && isDefenseSuccessful) {
                    // Shooter Fails & Defense succeeds (TODO SAME AS PREVIOUS, BUT DIFFERENT COMMENT NEEDED)
                    System.out.print(" Er schießt daneben, und " + opponent.name + " nimmt den Ball!");
                    ballCarrier = opponent;
                    // System.out.print("\n" + ballCarrier.name + " hat den Ball!");
                } else if (!isOffenseSuccessful && !isDefenseSuccessful) {
                    // Both Fail
                    System.out.print(" Er verfehlt den Schuss, kann aber den Ball halten.");
                    bonus = 0; // After a Shot, reset Bonus, even if he keeps the ball
                    continue;
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
                    // System.out.print("\n" + ballCarrier.name + " hat den Ball!");
                    // Swap Teams
                    bonus = 0;
                    temporaryTeam = defendingTeam;
                    defendingTeam = attackingTeam;
                    attackingTeam = temporaryTeam;
                } else if (!isOffenseSuccessful && isDefenseSuccessful) {
                    // Dribble Fail, opponent takes the Ball (TODO SAME AS MANY OTHER, BUT DIFFERENT COMMENT)
                    System.out.print(" " + ballCarrier.name + " kann sich nicht vorrücken, und " + opponent.name + " nimmt ihm den Ball weg!");
                    ballCarrier = opponent;
                    // System.out.print("\n" + ballCarrier.name + " hat den Ball!");
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
                receivingPlayer = attackingTeam.getRandomOtherPlayer(ballCarrier);
                isOffenseSuccessful = ballCarrier.performAction(Player.ActionType.Pass, bonus);
                if (isOffenseSuccessful && !isDefenseSuccessful) {
                    // Manages to Pass
                    System.out.print(" " + ballCarrier.name + " passt den Ball zu " + receivingPlayer.name + ".");
                    ballCarrier = receivingPlayer;
                    // System.out.print("\n" + ballCarrier.name + " hat den Ball!");
                    bonus = bonus + 5;
                } else if (isOffenseSuccessful && isDefenseSuccessful) {
                    // Good Pass, but it is intercepted (TODO SAME AS MANY OTHER, BUT DIFFERENT COMMENT)
                    System.out.print(" " + ballCarrier.name + " passt den Ball zu " + receivingPlayer.name + ", aber " + opponent.name + " fängt den Ball ab!");
                    ballCarrier = opponent;
                    // System.out.print("\n" + ballCarrier.name + " hat den Ball!");
                    // Swap Teams
                    bonus = 0;
                    temporaryTeam = defendingTeam;
                    defendingTeam = attackingTeam;
                    attackingTeam = temporaryTeam;
                } else if (!isOffenseSuccessful && isDefenseSuccessful) {
                    // Pass Fail, opponent takes the Ball (TODO SAME AS MANY OTHER, BUT DIFFERENT COMMENT)
                    System.out.print(" " + ballCarrier.name + " versucht, den Ball zu " + receivingPlayer.name + " passen, aber er verfehlt und "+ opponent.name + " fängt den Ball ab.");
                    ballCarrier = opponent;
                    // System.out.print("\n" + ballCarrier.name + " hat den Ball!");
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
        }
    }

    // Penalty Shoot-out
    public void Penalty(int startingMinute, int speed) throws InterruptedException {
        // TODO sort Players according to their shooting skill
        boolean draw = true;
        boolean isShootingSuccessful;
        boolean isCatchingSuccessful;
        List<Player> sortedPlayers1 = new ArrayList<>(team1.getPlayers());
        sortedPlayers1.sort((p1, p2) -> Integer.compare(p2.IW1, p1.IW1));
        Player goalKeeper1 = team1.getGoalKeeper();
        List<Player> sortedPlayers2 = new ArrayList<>(team2.getPlayers());
        sortedPlayers2.sort((p1, p2) -> Integer.compare(p2.IW1, p1.IW1));
        Player goalKeeper2 = team2.getGoalKeeper();
        int rounds = startingMinute + 1;
        int counter = 0;
        do {
            System.out.print("\n" + rounds + ". Minute - " + team1.name + " - " + team2.name + " " + team1.score + ":" + team2.score);
            System.out.print(" " + sortedPlayers1.get(counter).name + " schießt");
            for (int i = 0; i < 5; i++) {
                Thread.sleep(100 / speed);
                System.out.print(".");
            }
            isShootingSuccessful = sortedPlayers1.get(counter).performAction(Player.ActionType.Shoot, 0);
            isCatchingSuccessful = goalKeeper2.performAction(Player.ActionType.Catch, 0);
            if (isShootingSuccessful && !isCatchingSuccessful) {
                System.out.print("\nGOOOAAAL!!!");
                team1.score++;
                sortedPlayers1.get(counter).goals++;
                goals.add(new Goal(rounds, "Tor " + team1.score + ":" + team2.score, sortedPlayers1.get(counter).name, team1.longName, true));
            } else if (isShootingSuccessful && isCatchingSuccessful) {
                System.out.print(" Schöner Schuss! Aber " + goalKeeper2.name + " fängt den Ball!");
            } else {
                System.out.print(" Er schießt daneben!");
            }
            Thread.sleep(1000 / speed);
            rounds++;
            // Repeat with other Team
            System.out.print("\n" + rounds + ". Minute - " + team1.name + " - " + team2.name + " " + team1.score + ":" + team2.score);
            System.out.print(" " + sortedPlayers2.get(counter).name + " schießt");
            for (int i = 0; i < 5; i++) {
                Thread.sleep(100 / speed);
                System.out.print(".");
            }
            isShootingSuccessful = sortedPlayers2.get(counter).performAction(Player.ActionType.Shoot, 0);
            isCatchingSuccessful = goalKeeper1.performAction(Player.ActionType.Catch, 0);
            if (isShootingSuccessful && !isCatchingSuccessful) {
                System.out.print("\nGOOOAAAL!!!");
                team2.score++;
                sortedPlayers2.get(counter).goals++;
                goals.add(new Goal(rounds, "Tor " + team1.score + ":" + team2.score, sortedPlayers2.get(counter).name, team2.longName, true));
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
            // Check for draw
            if (team1.score != team2.score) {
                draw = false;
            }
        } while (rounds < 132 || (!draw && rounds >= 132)); // FIXME if after 5 Players from each Team have taken a shot and it is not a draw then exit
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

    public void displayGameEvents(){
        // TODO refactor prints here
        // there are many ways to make commentary more varied and built from random blocks
        // if this is done, many actions which are the same for different results could be refactored too
        // as only the commentaries are different
        // TODO OPTION to display calculated values
    }
}