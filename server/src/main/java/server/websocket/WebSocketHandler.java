package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.*;
import dataAccess.Exception.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.HttpException;
import webSocketMessages.serverMessages.*;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameDAO gameAccess;
    private final AuthDAO authAccess;

    public WebSocketHandler() {
        this.gameAccess = new MySqlGameDAO();
        this.authAccess = new MySqlAuthDAO();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws HttpException, DataAccessException, IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(new Gson().fromJson(message, JoinPlayer.class), session);
            case JOIN_OBSERVER -> joinObserver(new Gson().fromJson(message, JoinObserver.class), session); // call the new GSON with the class of the specific command
            case MAKE_MOVE -> makeMove(new Gson().fromJson(message, MakeMove.class), session);
            case LEAVE -> leave(new Gson().fromJson(message, Leave.class));
            case RESIGN -> resign(new Gson().fromJson(message, Resign.class), session);
        }
    }

    private void resign(Resign command, Session session) throws HttpException, IOException, DataAccessException {
        AuthData authData;
        GameData gameData;
        String errorMessage = "";

        try {
            authData = authAccess.getAuthData(command.getAuthString());
            gameData = gameAccess.getGame(command.getGameID());
        }catch (Exception e) {
            throw new HttpException(e.getMessage(), 500);
        }
        ChessGame game = gameData.game();

        if (!Objects.equals(authData.username(), gameData.whiteUsername()) && !Objects.equals(authData.username(), gameData.blackUsername())){
            errorMessage = "error observer can't make move";
            handleError(errorMessage,session);
        }
        else if(game.isResigned()){
            errorMessage = "error can't resign twice";
            handleError(errorMessage,session);
        }
        else{
            game.setResigned(true);
            var gameJson = new Gson().toJson(game);
            gameAccess.updateGame(command.getGameID(), "game", gameJson);
            String playerName = authData.username();
            String notification = String.format("%s resigned",playerName);
            var notifMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION,notification);
            try {
                if(session.isOpen()){
                    send(new Gson().toJson(notifMessage),session);
                }
                broadcast(command.getAuthString(), notifMessage, command.getGameID());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
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

    private void makeMove(MakeMove command, Session session) throws HttpException, IOException {
        AuthData authData;
        GameData gameData;
        String errorMessage = "";
        boolean isWhite;

        try {
            authData = authAccess.getAuthData(command.getAuthString());
            gameData = gameAccess.getGame(command.getGameID());
        } catch (Exception e) {
            throw new HttpException(e.getMessage(), 500);
        }

        ChessGame game = gameData.game();
        isWhite = Objects.equals(authData.username(), gameData.whiteUsername());

        if (!Objects.equals(authData.username(), gameData.whiteUsername()) && !Objects.equals(authData.username(), gameData.blackUsername())){
            errorMessage = "error observer can't make move";
        }
        else if(Objects.equals(game.getBoard().getPiece(command.getMove().getStartPosition()).getTeamColor().toString(), "BLACK") && isWhite){
            errorMessage = "error player can't move other player's pieces";
        }
        else if(Objects.equals(game.getBoard().getPiece(command.getMove().getStartPosition()).getTeamColor().toString(), "WHITE") && !isWhite){
            errorMessage = "error player can't move other player's pieces";
        }
        else if(!Objects.equals(game.getTeamTurn().toString(), game.getBoard().getPiece(command.getMove().getStartPosition()).getTeamColor().toString())){
            errorMessage = "error can't make move on other player's team";
        }
        else if(game.isResigned()){
            errorMessage = "error can't make moves after resignation";
        }
        else{
            try {
                game.makeMove(command.getMove());
                var gameJson = new Gson().toJson(game);
                gameAccess.updateGame(command.getGameID(), "game", gameJson);
            } catch (InvalidMoveException e){
                errorMessage = "error invalid move";
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        }

        if(errorMessage.isEmpty()) {
            var rootMessage = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game);
            String playerName = authData.username();
            String notification = String.format("%s made a move", playerName);
            var notifMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, notification);
            try {
                if (session.isOpen()) {
                    send(new Gson().toJson(rootMessage), session);
                }
                broadcast(command.getAuthString(), rootMessage, command.getGameID());
                broadcast(command.getAuthString(), notifMessage, command.getGameID());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else{
            handleError(errorMessage,session);
        }
    }

    private void handleSession(Integer gameID, String authString, Session session, String notifyText) throws IOException, DataAccessException {
        connections.add(authString, session, gameID);
        ChessGame game = gameAccess.getGame(gameID).game();
        var rootMessage = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME,game);
        var notifMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, notifyText);

        try {
            if(session.isOpen()){
                send(new Gson().toJson(rootMessage), session);
            }
            broadcast(authString, notifMessage, gameID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleError(String errorMessage, Session session) throws IOException {
        var error = new Error(ServerMessage.ServerMessageType.ERROR, errorMessage);
        try {
            send(new Gson().toJson(error), session);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void joinObserver(JoinObserver command, Session session) throws HttpException, DataAccessException, IOException {
        String errorMessage = validateObserver(command);
        String playerName = ""; // or implement logic to determine player name

        if (errorMessage.isEmpty()) {
            String notifyText = String.format("%s joined the game as an observer", playerName);
            handleSession(command.getGameID(), command.getAuthString(), session, notifyText);
        } else {
            handleError(errorMessage, session);
        }
    }

    private void joinPlayer(JoinPlayer command, Session session) throws HttpException, DataAccessException, IOException {
        String[] result = joinPlayerDatabaseCheck(command.getAuthString(), command.getGameID(), command.getPlayerColor().toString());
        String playerName = result[0];
        String errorMessage = result[1];

        if (errorMessage.isEmpty()) {
            String notifyText = String.format("%s joined the game as %s", playerName, command.getPlayerColor().toString());
            handleSession(command.getGameID(), command.getAuthString(), session, notifyText);
        } else {
            handleError(errorMessage, session);
        }
    }

    private String validateObserver(JoinObserver command) throws HttpException, DataAccessException {
        AuthData authData;
        GameData gameData;
        String errorMessage = "";

        try {
            authData = authAccess.getAuthData(command.getAuthString());
            gameData = gameAccess.getGame(command.getGameID());
        } catch (Exception e) {
            throw new HttpException(e.getMessage(), 500);
        }

        if (authData == null || !Objects.equals(command.getAuthString(), authData.authToken())) {
            errorMessage = "error incorrect authToken";
        } else if(gameData == null || gameData.gameID() != command.getGameID()) {
            errorMessage = "error incorrect gameID";
        }
        return errorMessage;
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

                    if(sessionEntry.getValue().isOpen()) {
                        if (!authToken.equals(exceptThisAuthToken)) {
                            send(new Gson().toJson(message), session);
                        }
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
