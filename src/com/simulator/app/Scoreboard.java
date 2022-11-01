package com.simulator.app;

import java.util.ArrayList;

public class Scoreboard {
    private int bestOf;
    private ArrayList<Set> sets = new ArrayList<>();
    private int idxCurrentSet = 0;
    private boolean finished = false;
    private boolean newSetStarted = false;
    private GameResult winner = GameResult.NO_WIN;

    public Scoreboard(int bestOf) {
        this.bestOf = bestOf;

        for(int i = 0; i < bestOf; ++i) {
            sets.add(new Set());
        }
    }

    public static enum GameResult {
        SERVER_WON,
        RECEIVER_WON,
        NO_WIN
    }

    public class Game {
        private int serverScore = 0;
        private int receiverScore = 0;
        private boolean inDeuce = false;
        private boolean inTieBreak = true;
        private boolean finished = false;
        private GameResult winner = GameResult.NO_WIN;

        public GameResult update(GameResult gameResult) {
            if (inDeuce && !inTieBreak) {
                return updateInDeuce(gameResult);
            }

            if (gameResult == GameResult.SERVER_WON) {
                serverScore++;
            } else {
                receiverScore++;
            }

            if (inTieBreak) {
                return getGameInSetInTieBreakCondition();
            }
            
            return getGameInSetCondition();
        }

        private GameResult getGameInSetInTieBreakCondition() {
            if (serverScore >= 7 && serverScore - 2 >= receiverScore) {
                return processWin(GameResult.SERVER_WON);
            } else if (receiverScore >= 7 && receiverScore - 2 >= serverScore) {
                return processWin(GameResult.RECEIVER_WON);
            }

            return GameResult.NO_WIN;
        }

        private GameResult updateInDeuce(GameResult gameResult) {
            if (gameResult == GameResult.SERVER_WON) {
                if (serverScore == 3 && receiverScore == 3) {
                    serverScore = 4;
                } else if (serverScore == 3 && receiverScore == 4) {
                    receiverScore = 3;
                } else if (serverScore == 4) {
                    serverScore = 5;
                    return processWin(GameResult.SERVER_WON);
                }
            } else {
                if (receiverScore == 3 && serverScore == 3) {
                    receiverScore = 4;
                } else if (receiverScore == 3 && serverScore == 4) {
                    serverScore = 3;
                } else if (receiverScore == 4) {
                    receiverScore = 5;
                    return processWin(GameResult.RECEIVER_WON);
                }
            }

            return GameResult.NO_WIN;
        }

        private GameResult getGameInSetCondition() {
            if (serverScore == 4 && receiverScore <= 2) {
                return processWin(GameResult.SERVER_WON);
            } else if (receiverScore == 4 && serverScore <= 2) {
                return processWin(GameResult.RECEIVER_WON);
            } 
            
            if (serverScore == 3 && receiverScore == 3) {
                inDeuce = true;
            }

            return GameResult.NO_WIN;
        }

        private GameResult processWin(GameResult gameResult) {
            this.finished = true;
            this.winner = gameResult;
            return gameResult;
        }

        public void setInTieBreak(boolean inTieBreak) {
            this.inTieBreak = inTieBreak;
        }

        public int getServerScore() {
            return serverScore;
        }

        public int getReceiverScore() {
            return receiverScore;
        }

        public boolean isFinished() {
            return finished;
        }
    }

    private class Set {
        private int serverGames = 0;
        private int receiverGames = 0;
        private boolean isTieBreak = false;
        private boolean finished = false;
        private GameResult winner = GameResult.NO_WIN;

        public GameResult update(GameResult gameResult) {
            if (gameResult == GameResult.SERVER_WON) {
                serverGames++;
            } else {
                receiverGames++;
            }

            if (isTieBreak) {
                return getSetInTieBreakResult(gameResult);
            }

            return getSetCondition();
        }

        private GameResult getSetInTieBreakResult(GameResult gameResult) {
            return processWin(gameResult);
        }

        private GameResult getSetCondition(){
            if (serverGames - 2 >= receiverGames && serverGames >= 6) {
                return  processWin(GameResult.SERVER_WON);
            } else if (receiverGames - 2 >= serverGames && receiverGames >= 6) {
                return processWin(GameResult.RECEIVER_WON);
            }

            if (serverGames == 6 && receiverGames == 6) {
                System.out.println("\n============= TIE-BREAKER!!! =============");
                isTieBreak = true;
            }

            return GameResult.NO_WIN;
        }

        private GameResult processWin(GameResult gameResult) {
            this.finished = true;
            this.winner = gameResult;
            return gameResult;
        }

        public boolean isFinished() {
            return finished;
        }
    }

    public void updateResults(GameResult gameResult){
        if (sets.get(idxCurrentSet).isFinished()){
            idxCurrentSet++;
        }

        Set currentSet = sets.get(idxCurrentSet);
        currentSet.update(gameResult);

        if (currentSet.isFinished()) {
            checkFinishedCondition();
        }

        setNewSetStarted(currentSet.isFinished());
    }

    private void checkFinishedCondition() {
        int playedSets = idxCurrentSet + 1;
        int firstServerSetWinsCount = 0;
        for(int i = 0; i <= this.idxCurrentSet; ++i) {
            if (i % 2 == 0 && sets.get(i).winner == GameResult.SERVER_WON) {
                firstServerSetWinsCount++;
            }
        }

        int firstReceiverSetWinsCount = playedSets - firstServerSetWinsCount;
        int requiredSetWinsToFinish = (this.bestOf / 2) + 1;
        
        if (firstServerSetWinsCount >= requiredSetWinsToFinish) {
            this.finished = true;
            this.winner = GameResult.SERVER_WON;
        } else if (firstReceiverSetWinsCount >= requiredSetWinsToFinish) {
            this.finished = true;
            this.winner = GameResult.RECEIVER_WON;
        }
    }

    public GameResult getCurrentSetWinner() {
        Set currentSet = sets.get(idxCurrentSet);
        if (currentSet.isFinished()) {
            return currentSet.winner;
        }

        return GameResult.NO_WIN;
    }

    public ArrayList<ArrayList<Integer>> getPlayersPerSetScore() {
        ArrayList<Integer> serverScore = new ArrayList<>();
        ArrayList<Integer> receiverScore = new ArrayList<>();

        for(int i = 0; i < (this.idxCurrentSet + 1); ++ i) {
            if (i % 2 == 0) {
                serverScore.add(sets.get(i).serverGames);
                receiverScore.add(sets.get(i).receiverGames);
            } else {
                serverScore.add(sets.get(i).receiverGames);
                receiverScore.add(sets.get(i).serverGames);
            }
        }

        ArrayList<ArrayList<Integer>> scores = new ArrayList<>();
        scores.add(serverScore);
        scores.add(receiverScore);

        return scores;
    }

    public boolean inTieBreak() {
        return sets.get(idxCurrentSet).isTieBreak;
    }

    public GameResult getWinner() {
        return this.winner;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setNewSetStarted(boolean newSetStarted) {
        this.newSetStarted = newSetStarted;
    }

    public boolean hasNewSetStarted() {
        return newSetStarted;
    }
}
