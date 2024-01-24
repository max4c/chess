package chess;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator  extends HelperMethodClass{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moveSet = new HashSet<>();

        moveSet.addAll(lineMoves(0,1,myPosition,board));
        moveSet.addAll(lineMoves(0,-1,myPosition,board));
        moveSet.addAll(lineMoves(-1,0,myPosition,board));
        moveSet.addAll(lineMoves(1,0,myPosition,board));

        return moveSet;
    }
}
