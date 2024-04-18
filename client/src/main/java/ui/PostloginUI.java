package ui;

import chess.ChessGame;
import model.GameData;
import server.ServerFacade;
import ui.websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class PostloginUI {
    private final ServerFacade server;
    private final WebSocketFacade websocket;

    public PostloginUI(ServerFacade server, WebSocketFacade websocket) {
        this.server = server;
        this.websocket = websocket;
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
                case "observe" -> observeGame(params);
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

    public String observeGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            try {
                DataCache.getInstance().setGameID(Integer.parseInt(params[0]));
                websocket.joinObserver();
                String blackBoard = new RenderBoard().getBlackBoard();
                String whiteBoard = new RenderBoard().getWhiteBoard();
                return "lower case is white and upper case is black\n" + whiteBoard + "\n" + blackBoard;
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "Expected: observe <ID>");
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length >= 2) {
            try {
                String authToken = DataCache.getInstance().getAuthToken();
                int gameID = Integer.parseInt(params[0]);
                DataCache.getInstance().setGameID(gameID);
                String playerColor = params[1].toUpperCase();
                server.joinGame(authToken,gameID,playerColor);
                DataCache.getInstance().setPlayerColor(playerColor);

                ChessGame.TeamColor playerColorConverted = ChessGame.TeamColor.valueOf(playerColor);
                websocket.joinPlayer(gameID, playerColorConverted);
                return "You joined the game";
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "Expected: join <ID> [WHITE|BLACK]");
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
        throw new ResponseException(400, "Expected: create <NAME>");
    }

    public String help() {
        return "create <Name> " + SET_TEXT_COLOR_WHITE +
                "- a game\n" + SET_TEXT_COLOR_BLUE +
                "list " + SET_TEXT_COLOR_WHITE +
                "- games\n" + SET_TEXT_COLOR_BLUE +
                "join <ID> [WHITE|BLACK] " + SET_TEXT_COLOR_WHITE +
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
