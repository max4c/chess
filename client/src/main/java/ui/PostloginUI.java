package ui;

import model.GameData;
import model.UserData;
import server.ServerFacade;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class PostloginUI {
    private final ServerFacade server;

    public PostloginUI(ServerFacade server) {
        this.server = server;
    }

    public String eval(String input){
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "create" -> createGame(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            try {
                String authToken = DataCache.getInstance().getAuthToken();
                var gameName = params[0];
                var game = new GameData(0,null, null,gameName,null);
                var response = server.createGame(game);
                int gameID = 0;
                return String.format("You created the game %s with the ID %s", gameName,gameID);
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "Expected:  <USERNAME> <PASSWORD>");
    }

    public String help() {
        return "create <Name> " + SET_TEXT_COLOR_WHITE +
                "- a game\n" + SET_TEXT_COLOR_BLUE +
                "list " + SET_TEXT_COLOR_WHITE +
                "- games\n" + SET_TEXT_COLOR_BLUE +
                "join <ID> [WHITE | BLACK|<empty>] " + SET_TEXT_COLOR_WHITE +
                "- a game\n" + SET_TEXT_COLOR_BLUE +
                "observe <ID> " + SET_TEXT_COLOR_WHITE +
                "- a game\n" + SET_TEXT_COLOR_BLUE +
                "logout " + SET_TEXT_COLOR_WHITE +
                "- when you are done \n" + SET_TEXT_COLOR_BLUE +
                "quit" + SET_TEXT_COLOR_WHITE +
                " - playing chess \n" + SET_TEXT_COLOR_BLUE +
                "help " + SET_TEXT_COLOR_WHITE +
                "- with possible commands\n";
    }
}
