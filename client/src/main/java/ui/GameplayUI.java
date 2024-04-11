package ui;

import server.ServerFacade;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class GameplayUI {

    private final ServerFacade server;

    public GameplayUI(ServerFacade server) {
        this.server = server;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
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

}
