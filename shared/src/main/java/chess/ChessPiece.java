package chess;

import chess.calculators.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (board.getPiece(myPosition).type == PieceType.BISHOP){
            BishopMovesCalculator bishopCalculator = new BishopMovesCalculator();
            return bishopCalculator.pieceMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).type == PieceType.ROOK){
            RookMovesCalculator rookCalculator = new RookMovesCalculator();
            return rookCalculator.pieceMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).type == PieceType.QUEEN){
            QueenMovesCalculator queenCalculator = new QueenMovesCalculator();
            return queenCalculator.pieceMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).type == PieceType.KING){
            KingMovesCalculator kingCalculator = new KingMovesCalculator();
            return kingCalculator.pieceMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).type == PieceType.KNIGHT){
            KnightMovesCalculator knightCalculator = new KnightMovesCalculator();
            return knightCalculator.pieceMoves(board, myPosition);
        }
        else{
            PawnMovesCalculator pawnCalculator = new PawnMovesCalculator();
            return pawnCalculator.pieceMoves(board, myPosition);
        }
    }
}
