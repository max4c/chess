package ui;

import chess.ChessGame;

public class DataCache {
    private String authToken;
    private int gameID;
    String playerColor;
    private ChessGame game;

    private static final DataCache instance = new DataCache();

    private DataCache() {
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public static DataCache getInstance() {
        return instance;
    }



    // in the future, potentially add current game id, and current player color
}
