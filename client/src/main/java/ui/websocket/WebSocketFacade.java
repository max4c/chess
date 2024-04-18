package ui.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import ui.DataCache;
import ui.DrawBoard;
import ui.ResponseException;
import webSocketMessages.serverMessages.Error;

import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
                    switch (msg.getServerMessageType()){
                        case LOAD_GAME:
                            DataCache.getInstance().setGame(new Gson().fromJson(message, LoadGame.class).getGame());
                            new DrawBoard();
                            break;
                        case ERROR:
                            Error error = new Gson().fromJson(message, Error.class);
                            notificationHandler.notify((error).getErrorMessage());
                            break;
                        case NOTIFICATION:
                            Notification notification = new Gson().fromJson(message, Notification.class);
                            notificationHandler.notify((notification).getMessage());
                            break;
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}


    public void sendCommand(UserGameCommand command) throws ResponseException {
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinPlayer(int gameID, ChessGame.TeamColor playerColor) throws ResponseException {
        var command = new JoinPlayer(DataCache.getInstance().getAuthToken(),gameID, playerColor);
        sendCommand(command);
    }

    public void joinObserver() throws ResponseException {
        var command = new JoinObserver(DataCache.getInstance().getAuthToken(), DataCache.getInstance().getGameID());
        sendCommand(command);
    }

    public void leave() throws ResponseException {
        var command = new Leave(DataCache.getInstance().getAuthToken(), DataCache.getInstance().getGameID());
        sendCommand(command);
    }

    public void resign() throws ResponseException {
        var command = new Resign(DataCache.getInstance().getAuthToken(), DataCache.getInstance().getGameID());
        sendCommand(command);
    }

    public void makeMove(ChessMove move) throws ResponseException {
        var command = new MakeMove(DataCache.getInstance().getAuthToken(),DataCache.getInstance().getGameID(), move);
        sendCommand(command);
    }




}
