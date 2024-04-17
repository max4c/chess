package ui.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.DataCache;
import ui.ResponseException;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.Leave;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.lang.reflect.Type;
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
                    System.out.println(message);
                    ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
                    switch (msg.getServerMessageType()){
                        case LOAD_GAME:
                            System.out.println("Call redraw board");
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


    public void joinPlayer(int gameID, ChessGame.TeamColor playerColor) throws ResponseException {
        try {
            var command = new JoinPlayer(DataCache.getInstance().getAuthToken(),gameID, playerColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave() throws ResponseException {
        try {
            var command = new Leave(DataCache.getInstance().getAuthToken(),DataCache.getInstance().getGameID());
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }



}
