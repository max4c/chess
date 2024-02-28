package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator extends HelperMethodClass {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moveSet = new HashSet<>();

        moveSet.addAll(lineMoves(1,-1,myPosition, board));
        moveSet.addAll(lineMoves(-1,-1,myPosition,board));
        moveSet.addAll(lineMoves(-1,1,myPosition,board));
        moveSet.addAll(lineMoves(1,1,myPosition,board));

        return moveSet;

    }

}
