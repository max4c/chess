package ui;

import server.ServerFacade;

import java.util.Arrays;
import static ui.EscapeSequences.*;
import model.UserData;

public class PreloginUI {
    private final String serverUrl;
    private final ServerFacade server;

    public PreloginUI(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException{
        if(params.length >= 3) {
            try {
                var username = params[0];
                var password = params[1];
                var email = params[2];
                var user = new UserData(username,password,email);
                server.register(user);
                return String.format("You registered the user %s", user.username());
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
