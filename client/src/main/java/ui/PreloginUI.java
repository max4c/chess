package ui;

import server.ServerFacade;

import java.util.Arrays;
import static ui.EscapeSequences.*;
import model.UserData;
import ui.websocket.WebSocketFacade;

public class PreloginUI {
    private final ServerFacade server;


    public PreloginUI(ServerFacade server) {
        this.server = server;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            try {
                var username = params[0];
                var password = params[1];
                var user = new UserData(username, password, null);
                var response = server.login(user);
                DataCache.getInstance().setAuthToken(response.authToken());
                return String.format("You logged in as %s", user.username());
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "Expected:  <USERNAME> <PASSWORD>");
    }


    public String register(String... params) throws ResponseException{
        if(params.length >= 3) {
            try {
                var username = params[0];
                var password = params[1];
                var email = params[2];
                var user = new UserData(username,password,email);
                var response = server.register(user);
                DataCache.getInstance().setAuthToken(response.authToken());
                return String.format("You logged in as %s \n", user.username());
            } catch (NumberFormatException ignored) {}
        }
        throw new ResponseException(400, "Expected:  <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String help() {
        return "register <USERNAME> <PASSWORD> <EMAIL>\n" +
                "login <USERNAME> <PASSWORD>\n" +
                "quit" + SET_TEXT_COLOR_WHITE +
                " - playing chess\n" + SET_TEXT_COLOR_BLUE +
                "help" + SET_TEXT_COLOR_WHITE +
                "- with possible commands\n";
    }


}
