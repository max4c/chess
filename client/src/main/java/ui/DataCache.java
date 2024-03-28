package ui;

public class DataCache {
    private String authToken;

    private static final DataCache instance = new DataCache();

    private DataCache() {
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
