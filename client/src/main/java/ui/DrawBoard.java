package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import java.util.Objects;

import static ui.EscapeSequences.*;

public class DrawBoard {


    private ChessPiece[][] board;

    public DrawBoard() {
        this.board = DataCache.getInstance().getGame().getBoard().getSquares();
        draw();

    }



    private void draw() {
        StringBuilder output = new StringBuilder();
        output.append("\n").append(colorLowercaseAndNumbers("  "));
        for (int j = 0; j < 8; j++) {
            output.append(colorLowercaseAndNumbers(" " + (char) (j + 'a') + " "));
        }
        output.append("\n");

        boolean shouldFlip = !Objects.equals(DataCache.getInstance().playerColor, "WHITE");

        for (int i = 0; i < 8; i++) {
            int row = shouldFlip ? 7 - i : i;
            output.append(colorLowercaseAndNumbers((8 - row) + " "));
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board[row][j];
                output.append(" ").append(getColoredPieceChar(piece, (i + j) % 2 == 0)).append(" ");
            }
            // Add an extra space after each row, to keep square size consistent
            output.append(" ");
            output.append(colorLowercaseAndNumbers(" " + (8 - row))).append("\n");
        }

        output.append(colorLowercaseAndNumbers("  "));
        for (int j = 0; j < 8; j++) {
            output.append(colorLowercaseAndNumbers(" " + (char) (j + 'a') + " "));
        }
        output.append("\n");

        System.out.println(output.toString() + SET_BG_COLOR_DARK_GREY);
    }

    private String getColoredPieceChar(ChessPiece piece, boolean isWhiteSquare) {
        if (piece == null) {
            String bgColor = isWhiteSquare ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK;
            return bgColor + " ";
        }

        String pieceName = piece.getPieceType().toString();
        String pieceColor = piece.getTeamColor().toString();

        String pieceChar = switch (pieceName) {
            case "PAWN" -> "P";
            case "ROOK" -> "R";
            case "KNIGHT" -> "N";
            case "BISHOP" -> "B";
            case "QUEEN" -> "Q";
            case "KING" -> "K";
            default -> "?";
        };

        pieceChar = pieceColor.equalsIgnoreCase("WHITE") ?
                (EscapeSequences.SET_TEXT_COLOR_RED + pieceChar) :
                (EscapeSequences.SET_TEXT_COLOR_BLUE + pieceChar);

        String bgColor = isWhiteSquare ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK;

        return bgColor + pieceChar;
    }

    private String colorLowercaseAndNumbers(String input) {
        return SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + input;
    }
}

