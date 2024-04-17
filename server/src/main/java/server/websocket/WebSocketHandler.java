package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
import dataAccess.Exception.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.HttpException;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.Leave;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameDAO gameAccess;
    private final AuthDAO authAccess;
    private final UserDAO userAccess;

    public WebSocketHandler() {
        this.gameAccess = new MySqlGameDAO();
        this.authAccess = new MySqlAuthDAO();
        this.userAccess = new MemoryUserDAO();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws HttpException, DataAccessException {
        System.out.println(message);
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(new Gson().fromJson(message, JoinPlayer.class), session);
            case JOIN_OBSERVER -> joinObserver(); // call the new GSON with the class of the specific command
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave(new Gson().fromJson(message, Leave.class));
            case RESIGN -> resign();
        }
    }

    private void resign() {

    }

    private void leave(Leave command) throws HttpException, DataAccessException {
        connections.remove(command.getGameID(), command.getAuthString());
        AuthData authData;
        GameData gameData;
        try {
            authData = authAccess.getAuthData(command.getAuthString());
            gameData = gameAccess.getGame(command.getGameID());

        }catch (Exception e) {
            throw new HttpException(e.getMessage(), 500);
        }

        if(Objects.equals(gameData.whiteUsername(), authData.username())){
            gameAccess.updateGame(command.getGameID(), "whiteUsername", null);
        } else{
            gameAccess.updateGame(command.getGameID(), "blackUsername", null);
        }

        String playerName = authData.username();
        String notification = String.format("%s left the game",playerName);
        var notifMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION,notification);
        try {
            broadcast(command.getAuthString(), notifMessage, command.getGameID());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void makeMove() {

    }

    private void joinObserver() {
    }

    private void joinPlayer(JoinPlayer command, Session session) throws HttpException{
        connections.add(command.getAuthString(), session, command.getGameID());
        String[] result = joinPlayerDatabaseCheck(command.getAuthString(), command.getGameID(), command.getPlayerColor().toString());
        String playerName = result[0];
        String errorMessage = result[1];
        if(errorMessage.isEmpty()){
            String game = "loaded game";
            var rootMessage = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME,game);
            String notification = String.format("%s joined the game as %s",playerName, command.getPlayerColor().toString());
            var notifMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION,notification);
            try {
                send(new Gson().toJson(rootMessage),session);
                broadcast(command.getAuthString(), notifMessage, command.getGameID());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            var error = new Error(ServerMessage.ServerMessageType.ERROR, errorMessage);
            try {
                send(new Gson().toJson(error),session);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void send(String message,Session session) throws IOException {
        session.getRemote().sendString(message);
    }

    public void broadcast(String exceptThisAuthToken, ServerMessage message, int rootGameID) throws IOException {
        for (Map.Entry<Integer, Map<String, Session>> entry : connections.getEntrySet()) { // for each entry in connections
            Integer gameID = entry.getKey();
            Map<String, Session> sessionMap = entry.getValue();

            if(gameID == rootGameID){
                for (Map.Entry<String, Session> sessionEntry : sessionMap.entrySet()) { // for each session in each entry
                    String authToken = sessionEntry.getKey();
                    Session session = sessionEntry.getValue();

                    if (!authToken.equals(exceptThisAuthToken)) {
                        send(new Gson().toJson(message),session);
                    }
                }
            }
        }

    }

    public String[] joinPlayerDatabaseCheck(String authToken, int gameID, String playerColor) throws HttpException {
        AuthData authData;
        GameData gameData;

        try {
            authData = authAccess.getAuthData(authToken);
            gameData = gameAccess.getGame(gameID);
        } catch (Exception e) {
            throw new HttpException(e.getMessage(), 500);
        }

        String[] result = new String[2];

        if (authData == null || !Objects.equals(authToken, authData.authToken())){
            result[1] = "error incorrect authToken";
        }
        else if(playerColor.equals("BLACK") && (gameData == null || gameData.blackUsername() == null || !gameData.blackUsername().equals(authData.username())) ){
            result[1] = "error incorrect color joining";
        }
        else if(playerColor.equals("WHITE") && (gameData == null || gameData.whiteUsername() == null || !gameData.whiteUsername().equals(authData.username()))){
            result[1] = "error incorrect color joining";
        }
        else if(gameData == null || gameData.gameID() != gameID){
            result[1] = "error incorrect gameID";
        }
        else{
            result[0] = authData.username();
            result[1] = "";
        }
        return result;
    }
}
