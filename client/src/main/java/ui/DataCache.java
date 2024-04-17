package ui;

public class DataCache {
    private String authToken;
    private int gameID;

    private static final DataCache instance = new DataCache();
    // store the current game

    private DataCache() {
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
