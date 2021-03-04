package com.sg.guessnumber.models;

import java.sql.Timestamp;


public class Round {
    private int roundID;
    private int gameID;
    private Timestamp time;
    private String guess;
    private String results;

//getters
    public void setRoundID(int roundID) {
        this.roundID = roundID;
    }
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
    public void setTime(Timestamp time) {
        this.time = time;
    }
    public void setGuess(String guess) {
        this.guess = guess;
    }
    public void setResults(String results) {
        this.results = results;
    }
//setters
    public int getRoundID() {
        return roundID;
    }
    public int getGameID() {
        return gameID;
    }
    public Timestamp getTime() {
        return time;
    }
    public String getGuess() {
        return guess;
    }
    public String getResults() {
        return results;
    }
}
