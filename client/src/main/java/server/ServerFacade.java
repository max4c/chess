package server;

import com.google.gson.Gson;
import model.GameData;
import ui.ResponseException;
import model.UserData;
import model.AuthData;
import model.GameResponse;
import model.ListGamesResponse;

import java.io.*;
import java.net.*;
import java.util.Map;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    /*
    private class GameResponse {
        private int gameID;

        public void setGameID(int gameID) {
            this.gameID = gameID;
        }

        public int getGameID() {
            return gameID;
        }
    }
    */


    public int createGame(GameData game, String authToken)throws ResponseException{
        var path = "/game";
        GameResponse gameResponse = this.makeRequest("POST",path,authToken,game, GameResponse.class);
        return gameResponse.gameID();
    }

    public AuthData register(UserData user)throws ResponseException{
        var path = "/user";
        return this.makeRequest("POST",path,null,user, AuthData.class);
    }

    public AuthData login(UserData user)throws ResponseException{
        var path = "/session";
        return this.makeRequest("POST",path,null,user, AuthData.class);
    }

    public ListGamesResponse listGames(String authToken)throws ResponseException{
        var path = "/game";
        return this.makeRequest("GET", path, authToken, null, ListGamesResponse.class);
    }

    public void logout(String authToken)throws ResponseException{
        var path = "/session";
        this.makeRequest("DELETE", path, authToken, null, null);
    }

    public void joinGame(String authToken,int gameID, String playerColor)throws ResponseException{
        var path = "/game";
        Map<String, Object> request= Map.of("playerColor", playerColor, "gameID", gameID);
        this.makeRequest("PUT", path, authToken, request, null);
    }

    private <T> T makeRequest(String method, String path, String header, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if(header != null){
                http.addRequestProperty("authorization", header);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
