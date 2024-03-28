package ui;

import model.GameData;
import model.ListGamesResponse;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class PostloginUI {
    private final ServerFacade server;

    public PostloginUI(ServerFacade server) {
        this.server = server;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String listGames() throws ResponseException {
        try{
            String authToken = DataCache.getInstance().getAuthToken();
            Collection<GameData> games = server.listGames(authToken).games();
            StringBuilder sb = new StringBuilder();
            for(GameData game : games) {
                if (game.gameID() != 0) {
                    sb.append("ID: ").append(game.gameID()).append(", ");
                }
                if (game.gameName() != null) {
                    sb.append("game_name: ").append(game.gameName()).append(", ");
                }
                if (game.whiteUsername() != null) {
                    sb.append("white_username: ").append(game.whiteUsername()).append(", ");
                } else{
                    sb.append("white_username: , ");
                }
                if (game.blackUsername() != null) {
                    sb.append("black_username: ").append(game.blackUsername());
                } else{
                    sb.append("black_username: ");
                }
                sb.append("\n");
            }
            return sb.toString();
        } catch (ResponseException ex) {
            return ex.getMessage();
        }

    }

    public String logout() throws ResponseException {
        try{
            String authToken = DataCache.getInstance().getAuthToken();
            server.logout(authToken);
            return "You logged out. Come back soon \uD83D\uDE0A";
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length >= 2) {
            try {
                String authToken = DataCache.getInstance().getAuthToken();
                int gameID = Integer.parseInt(params[0]);
                String playerColor = params[1];
                var game = new GameData(gameID,null, null,null,null);
                //int gameID = server.createGame(game, authToken);
                return String.format("You created a game with the ID %s",gameID);
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "Expected:  <USERNAME> <PASSWORD>");
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            try {
                String authToken = DataCache.getInstance().getAuthToken();
                var gameName = params[0];
                var game = new GameData(0,null, null,gameName,null);
                int gameID = server.createGame(game, authToken);
                return String.format("You created a game with the name %s and the ID %s", gameName,gameID);
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
