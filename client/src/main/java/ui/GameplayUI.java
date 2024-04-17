package ui;

import server.ServerFacade;
import ui.websocket.WebSocketFacade;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class GameplayUI {

    private final ServerFacade server;
    private final WebSocketFacade websocket;

    public GameplayUI(ServerFacade server, WebSocketFacade websocket) {
        this.server = server;
        this.websocket = websocket;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "leave" -> leave();
                case "resign" -> resign();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() throws ResponseException{
        return "redraw " + SET_TEXT_COLOR_WHITE +
                "- chess board\n" + SET_TEXT_COLOR_BLUE +
                "leave " + SET_TEXT_COLOR_WHITE +
                "- the game\n" + SET_TEXT_COLOR_BLUE +
                "make move <start_position> <end_position>\n" +
                "resign " + SET_TEXT_COLOR_WHITE +
                "- give up game\n" + SET_TEXT_COLOR_BLUE +
                "highlight <position of piece> " + SET_TEXT_COLOR_WHITE +
                "- legal moves\n" + SET_TEXT_COLOR_BLUE +
                "help " + SET_TEXT_COLOR_WHITE +
                "- with possible commands\n";
    }

    public String leave() throws ResponseException{
        websocket.leave();
        return "You have left the game";
    }

    public String resign() throws ResponseException{
        websocket.resign();
        return "You have given up the game";
    }

}
