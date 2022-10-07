package com.simulator.app;

import java.util.ArrayList;
import java.util.Random;

import com.simulator.app.Scoreboard.Game;
import com.simulator.app.Scoreboard.GameResult;

public class Tournament {
    private String name;
    private int bestOf;
    private ArrayList<Player> players = new ArrayList<>();
    private Random rand = new Random();
    private Scoreboard scoreB;

    public Tournament(String name, int bestOf, ArrayList<Player> players) {
        this.name = name;
        this.bestOf = bestOf;
        this.players = players;
        this.scoreB = new Scoreboard(bestOf);
    }

    public void simulate(){
        int idxServerPlayer = rand.nextBoolean() ? 0 : 1;
        int idxReceiverPlayer = (idxServerPlayer == 0) ? 1 : 0;

        Player serverPlayer = players.get(idxServerPlayer);
        Player receiverPlayer = players.get(idxReceiverPlayer);

        int gameNumber = 1;
        int setNumber = 1;
        while (!scoreB.isFinished()) {

            printPreGame(gameNumber, serverPlayer);
            GameResult gameResult = playGame(serverPlayer, receiverPlayer);
            printGameResult(gameNumber, gameResult, serverPlayer, receiverPlayer);
            gameNumber++;

            scoreB.updateResults(gameResult);

            if (scoreB.hasNewSetStarted()) {
                printSetResult(setNumber, serverPlayer, receiverPlayer);
                printFullGameStatus(setNumber, serverPlayer, receiverPlayer);
                setNumber++;
                gameNumber = 1;

                Player tmp = serverPlayer;
                serverPlayer = receiverPlayer;
                receiverPlayer = tmp;
            }
        }

        Player winner = (scoreB.getWinner() == GameResult.SERVER_WON) ? players.get(idxServerPlayer) : players.get(idxReceiverPlayer);
        printGameWinner(winner);
    }

    private GameResult playGame(Player server, Player receiver) {
        Ball ball = new Ball();
        Game game = scoreB.new Game();
        game.setInTieBreak(scoreB.inTieBreak());
        GameResult gameResult = GameResult.NO_WIN;

        while(!game.isFinished()) {
            server.firstHit(ball);

            while(true) {
                if (!receiver.hit(ball)) {
                    gameResult = game.update(GameResult.SERVER_WON);
                    printGameStatus(GameResult.SERVER_WON, game);
                    break;
                }

                if(!server.hit(ball)) {
                    gameResult = game.update(GameResult.RECEIVER_WON);
                    printGameStatus(GameResult.RECEIVER_WON, game);
                    break;
                }
            }
        }
        return gameResult;
    }

    public void rematch() {
        this.scoreB = new Scoreboard(this.bestOf);
    }

    private void printPreGame(int gameNumber, Player server) {
        System.out.println("\nGame #" + gameNumber + "| server: " + server.getName());
    }

    private void printGameStatus(GameResult gameResult, Game game) {
        String winner = (gameResult == GameResult.SERVER_WON) ? "Server won!  " : "Receiver won!";
        System.out.println(winner + " | server score: " + game.getServerScore() + " | receiver score: " + game.getReceiverScore());
        if (game.getServerScore() == 3 && game.getReceiverScore() == 3) {
            System.out.println("---------------------- DEUCE ----------------------");
        }
    }

    private void printGameResult(int gameNumber, GameResult gameResult, Player server, Player receiver) {
        String winnersName = (gameResult == GameResult.SERVER_WON) ? server.getName() : receiver.getName();
        System.out.println("| Winner: " + winnersName);
    }
    private void printSetResult(int setNumber, Player server, Player receiver) {
        GameResult gameResult = scoreB.getCurrentSetWinner();
        String winnerName = (gameResult == GameResult.SERVER_WON) ? server.getName() : receiver.getName();
        System.out.println("\nSET #" + setNumber + " | Winner: " + winnerName + "\n");
    }

    private void printFullGameStatus(int setNumber, Player server, Player receiver) {
        ArrayList<ArrayList<Integer>> scores = scoreB.getPlayersPerSetScore();
        ArrayList<Integer> firstServerScores = (setNumber % 2 == 0) ? scores.get(1) : scores.get(0);
        ArrayList<Integer> firstReceiverScores = (setNumber % 2 == 0) ? scores.get(0) : scores.get(1);

        String gameStatus = (scoreB.isFinished()) ? "FINISHED" : "IN-GAME-";

        System.out.println("|-" + gameStatus + "-----------------------");
        System.out.println("|" + server.getName() + " - " + firstServerScores);
        System.out.println("|" + receiver.getName() + " - " + firstReceiverScores);
        System.out.println("|--------------------------------");

    }

    private void printGameWinner(Player winner) {
        System.out.println(this.name.toUpperCase() + " WINNER: " + winner.getName().toUpperCase() + "!\n");
    }
}
