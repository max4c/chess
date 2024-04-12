package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(session);
            case JOIN_OBSERVER -> joinObserver();
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

    private void joinPlayer(Session session){
        // add connection to connection manager
        String game = "You joined the game";
        var notification = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME,game);

        // send another notification to all other clients saying user joined game
    }
}
