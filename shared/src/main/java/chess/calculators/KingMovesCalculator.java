package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moveSet = new HashSet<>();
        int curRow = myPosition.getRow();
        int curCol = myPosition.getColumn();

        //bottom row
        addPosition(curRow-1,curCol, myPosition, moveSet,board);
        addPosition(curRow-1,curCol+1, myPosition, moveSet,board);
        addPosition(curRow-1,curCol-1, myPosition, moveSet,board);
        //top row
        addPosition(curRow+1,curCol, myPosition, moveSet,board);
        addPosition(curRow+1,curCol+1, myPosition, moveSet,board);
        addPosition(curRow+1,curCol-1, myPosition, moveSet,board);
        // middle row
        addPosition(curRow,curCol+1, myPosition, moveSet,board);
        addPosition(curRow,curCol-1, myPosition, moveSet,board);


        return moveSet;
    }

    public void addPosition(int curRow, int curCol,ChessPosition myPosition, HashSet<ChessMove> moveSet, ChessBoard board){
        if((curRow > 0 && curRow < 9) && (curCol > 0 && curCol < 9)){ // out of range check
            if(board.getSquares()[curRow-1][curCol-1] != null){ // null check
                if (board.getPiece(myPosition).getTeamColor() != board.getSquares()[curRow-1][curCol-1].getTeamColor()) { //type check
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow, curCol), null));
                }
                return;
            }
            moveSet.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
        }
    }

}
