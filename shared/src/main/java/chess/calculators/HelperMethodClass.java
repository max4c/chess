package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.calculators.PieceMovesCalculator;

import java.util.Collection;
import java.util.HashSet;

public abstract class HelperMethodClass implements PieceMovesCalculator {
    protected Collection<ChessMove> lineMoves(int rowDirection, int colDirection, ChessPosition myPosition, ChessBoard board){
        HashSet<ChessMove> moveSet = new HashSet<>();
        int curRow = myPosition.getRow();
        int curCol = myPosition.getColumn();

        for(int i = 1; i < 8; i++){
            curRow = curRow + rowDirection;
            curCol = curCol + colDirection;
            if((curRow > 0 && curRow < 9) && (curCol > 0 && curCol < 9)){ // out of range check
                if(board.getSquares()[curRow-1][curCol-1] != null){ // null check
                    if (board.getPiece(myPosition).getTeamColor() != board.getSquares()[curRow-1][curCol-1].getTeamColor()) { //type check
                        moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow, curCol), null));
                    }
                    break;
                }
                moveSet.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            }
        }

        return moveSet;
    }
}
