package ui;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import server.ServerFacade;
import ui.websocket.WebSocketFacade;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class GameplayUI {

    private final WebSocketFacade websocket;

    public GameplayUI(WebSocketFacade websocket) {
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
                case "makemove" -> makeMove(params);
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
                "makeMove <startRow> <startCol> <endRow> <endCol>\n" +
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

    public String makeMove(String... params) throws ResponseException{
        if (params.length >= 2) {
            try {

                int startRow = Integer.parseInt(params[0]);
                int startCol = switchLettersToNumbers(params[1]);
                int endRow = Integer.parseInt(params[2]);
                int endCol = switchLettersToNumbers(params[3]);

                ChessPosition startPosition = new ChessPosition(startRow,startCol);
                ChessPosition endPosition = new ChessPosition(endRow,endCol);
                ChessMove move = new ChessMove(startPosition,endPosition,ChessPiece.PieceType.QUEEN);
                websocket.makeMove(move);
                return "You moved a chessPiece";
            }catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "Expected: observe <ID>");
    }

    public static int switchLettersToNumbers(String param) {

        return switch (param) {
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default -> throw new IllegalArgumentException("Invalid letter");
        };
    }

}
