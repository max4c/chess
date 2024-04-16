package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.MySqlAuthDAO;
import dataAccess.MySqlGameDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.HttpException;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameDAO gameAccess;
    private final AuthDAO authAccess;
    private final GameService gameService;

    public WebSocketHandler() {
        this.gameAccess = new MySqlGameDAO();
        this.authAccess = new MySqlAuthDAO();
        this.gameService = new GameService(gameAccess,authAccess);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws HttpException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(new Gson().fromJson(message, JoinPlayer.class), session);
            case JOIN_OBSERVER -> joinObserver(); // call the new GSON with the class of the specific command
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave();
            case RESIGN -> resign();
            default -> command_error();
        }
    }

    private void command_error() {

    }

    private void resign() {

    }

    private void leave() {
    }

    private void makeMove() {

    }

    private void joinObserver() {
    }

    private void joinPlayer(JoinPlayer command, Session session) throws HttpException{
        connections.add(command.getAuthString(), session, command.getGameID());
        String playerName = gameService.joinPlayer(command.getAuthString());
        String game = String.format("%s joined the game",playerName);
        var message = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME,game);
        // Do I need to get the game from the game database with the joinPlayer function?
        try {
            broadcast(command.getAuthString(), message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String message,Session session) throws IOException {
        session.getRemote().sendString(message);
    }

    public void broadcast(String exceptThisAuthToken, ServerMessage message) throws IOException {
        for (Map.Entry<Integer, Map<String, Session>> entry : connections.getEntrySet()) {
            Integer gameID = entry.getKey();
            Map<String, Session> sessionMap = entry.getValue();

            for (Map.Entry<String, Session> sessionEntry : sessionMap.entrySet()) {
                String authToken = sessionEntry.getKey();
                Session session = sessionEntry.getValue();

                if (!authToken.equals(exceptThisAuthToken)) {
                    send(message.toString(),session);
                }
            }
        }

    }
}
