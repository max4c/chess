package chess;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator extends HelperMethodClass{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moveSet = new HashSet<>();

        moveSet.addAll(lineMoves(1,-1,myPosition));
        moveSet.addAll(lineMoves(-1,-1,myPosition));
        moveSet.addAll(lineMoves(-1,1,myPosition));
        moveSet.addAll(lineMoves(1,1,myPosition));

        return moveSet;

    }

}
