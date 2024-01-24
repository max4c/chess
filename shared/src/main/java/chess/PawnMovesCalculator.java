package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moveSet = new HashSet<>();
        int curRow = myPosition.getRow();
        int curCol = myPosition.getColumn();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        if(color == ChessGame.TeamColor.WHITE){
            if(board.getSquares()[curRow][curCol-1] == null) {
                if (curRow == 7) {
                    addPromotion(curRow + 1, curCol, myPosition, moveSet, board);
                }
                if (curRow == 2) {
                    if(board.getSquares()[curRow+1][curCol-1] == null) {
                        moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 2, curCol), null));
                    }
                }
                if (curRow != 7) {
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol), null));
                }
            }
            addDiagonalPromotion(curRow + 1, curCol - 1, myPosition, moveSet, board,color);
            addDiagonalPromotion(curRow + 1, curCol + 1, myPosition, moveSet, board,color);
        }
        if(color == ChessGame.TeamColor.BLACK) {
            if(board.getSquares()[curRow-2][curCol-1] == null) {
                if (curRow == 2) {
                    addPromotion(curRow - 1, curCol, myPosition, moveSet, board);
                }
                if (curRow == 7) {
                    if(board.getSquares()[curRow-3][curCol-1] == null) {
                        moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 2, curCol), null));
                    }
                }
                if (curRow != 2) {
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol), null));
                }
            }
            addDiagonalPromotion(curRow - 1, curCol - 1, myPosition, moveSet, board,color);
            addDiagonalPromotion(curRow - 1, curCol + 1, myPosition, moveSet, board,color);
        }
        return moveSet;
    }

    public void addPromotion(int curRow, int curCol, ChessPosition myPosition, HashSet<ChessMove> moveSet, ChessBoard board){

        moveSet.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol), ChessPiece.PieceType.ROOK));
        moveSet.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol), ChessPiece.PieceType.QUEEN));
        moveSet.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol), ChessPiece.PieceType.KNIGHT));
        moveSet.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol), ChessPiece.PieceType.BISHOP));

    }

    public void addDiagonalPromotion(int curRow, int curCol, ChessPosition myPosition, HashSet<ChessMove> moveSet, ChessBoard board,ChessGame.TeamColor color){

        if(curCol > 0 && curCol < 9){ // out of range check
            if(board.getSquares()[curRow-1][curCol-1] != null){ // null check
                if (board.getPiece(myPosition).getTeamColor() != board.getSquares()[curRow-1][curCol-1].getTeamColor()) { //type check
                    if((curRow == 8 && color == ChessGame.TeamColor.WHITE)|| (curRow == 1 && color == ChessGame.TeamColor.BLACK)){
                        moveSet.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol), ChessPiece.PieceType.ROOK));
                        moveSet.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol), ChessPiece.PieceType.QUEEN));
                        moveSet.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol), ChessPiece.PieceType.KNIGHT));
                        moveSet.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol), ChessPiece.PieceType.BISHOP));
                    }
                    else{
                        moveSet.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol), null));
                    }
                }
            }
        }
    }
}
