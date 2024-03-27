package ui;

import server.ServerFacade;

import java.util.Arrays;
import static ui.EscapeSequences.*;

public class PreloginClient{
    private final String serverUrl;
    private final ServerFacade server;

    public PreloginClient(String serverUrl) {
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
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws Exception{
        if(params.length >= 3){
            return "time to create the server";
        }
        throw new Exception();
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
