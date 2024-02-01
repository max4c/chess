package chess;

import java.util.Collection;
import java.util.Arrays;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor turn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.turn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition.
     * A move is invalid if the chess piece cannot move there,
     * if the move leaves the team’s king in danger,
     * or if it’s not the corresponding team's turn.
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        /* check if king in check, if true,
        using the position of the piece that is causing check,
        see if any pieces on king's team can remove the piece causing check (but that could cause another check),
        if no pieces can remove check(all validMoves on king's team are null), checkmate

        create a board for each move in validMoveSet in the moveSet
        with that board see if team is in check, if true
        remove that move in validMoveSet
        */

        ChessPiece piece = board.getPiece(startPosition);
        TeamColor color = piece.getTeamColor();
        Collection<ChessMove> validMoveSet = piece.pieceMoves(board,startPosition);

        Collection<ChessMove> tempMoveSet = new HashSet<>(validMoveSet);

        for(ChessMove move: tempMoveSet){

            ChessPiece[][] tempSquares = new ChessPiece[8][8];

            for(int i = 0; i < board.getSquares().length; i++){
                tempSquares[i] = Arrays.copyOf(board.getSquares()[i], board.getSquares()[i].length);
            }

            ChessBoard tempBoard = new ChessBoard(tempSquares);

            tempBoard.addPiece(move.getEndPosition(),tempBoard.getSquares()[move.getStartPosition().getRow()-1][move.getStartPosition().getColumn()-1]);
            tempBoard.getSquares()[startPosition.getRow()-1][startPosition.getColumn()-1] = null; // remove initial piece position

            ChessBoard ogBoard = board;
            board = tempBoard;

            if(isInCheck(color)){
                validMoveSet.remove(move);
            }
            board = ogBoard;
        }

        return validMoveSet;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();

        if(!validMoves(startPosition).contains(move)){
            throw new InvalidMoveException();
        }
        board.addPiece(endPosition,board.getSquares()[startPosition.getRow()-1][startPosition.getColumn()-1]);
        board.getSquares()[startPosition.getRow()-1][startPosition.getColumn()-1] = null;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // find king
        for(int i = 0; i <8; i++){
            for(int j = 0; j<8;j++){
                ChessPiece potentialPiece = board.getSquares()[i][j];
                if(potentialPiece != null){  // find a piece on the chess board
                    Collection<ChessMove> pieceMoveSet = potentialPiece.pieceMoves(board,new ChessPosition(i+1,j+1)); // generate potential moves of piece
                        if(teamColor != potentialPiece.getTeamColor()) { // make sure the two pieces aren't same color
                            for(ChessMove move : pieceMoveSet){ // check for each move if the piece puts the King in Check
                                if(board.getSquares()[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1] != null){
                                    if(board.getSquares()[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1].getPieceType() == ChessPiece.PieceType.KING && teamColor != potentialPiece.getTeamColor()){
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
