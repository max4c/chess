package chess;

import java.util.Collection;
import java.util.HashSet;

public abstract class HelperMethodClass implements PieceMovesCalculator{
    protected Collection<ChessMove> lineMoves(int rowDirection, int colDirection, ChessPosition myPosition){
        HashSet<ChessMove> moveSet = new HashSet<>();
        int curRow = myPosition.getRow();
        int curCol = myPosition.getColumn();

        for(int i = 1; i < 8; i++){
            curRow = curRow + rowDirection;
            curCol = curCol + colDirection;
            if((curRow > 0 && curRow < 9) && (curCol > 0 && curCol < 9)){
                moveSet.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            }
        }

        return moveSet;
    }
}
