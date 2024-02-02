package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moveSet = new HashSet<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
            if(row == 2){
                if((board.getPiece(new ChessPosition(row+2,col)) == null)&& (board.getPiece(new ChessPosition(row+1,col)) == null)) {
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(row + 2, col), null));
                }
            }
            if(row == 7){
                moveSet.addAll(PromotionPieces(board,myPosition,row+1,col));
            }
            if(row!= 7) {
                if(board.getPiece(new ChessPosition(row+1,col)) == null) {
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), null));
                }
            }
            moveSet.addAll(Diagonal(board,myPosition,row+1,col-1));
            moveSet.addAll(Diagonal(board,myPosition,row+1,col+1));
        }
        else{
            if(row == 7){
                if((board.getPiece(new ChessPosition(row-2,col)) == null)&& (board.getPiece(new ChessPosition(row-1,col)) == null)) {
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(row - 2, col), null));
                }
            }
            if(row == 2){
                moveSet.addAll(PromotionPieces(board,myPosition,row-1,col));
            }
            if(row!=2) {
                if(board.getPiece(new ChessPosition(row-1,col)) == null){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), null));
                }
            }
            moveSet.addAll(Diagonal(board,myPosition,row-1,col-1));
            moveSet.addAll(Diagonal(board,myPosition,row-1,col+1));
        }

        return moveSet;
    }

    public Collection<ChessMove> PromotionPieces(ChessBoard board, ChessPosition myPosition, int row, int col){
        HashSet<ChessMove> moveSet = new HashSet<>();
        if(board.getPiece(new ChessPosition(row,col)) == null) {
            moveSet.add(new ChessMove(myPosition, new ChessPosition(row, col), ChessPiece.PieceType.ROOK));
            moveSet.add(new ChessMove(myPosition, new ChessPosition(row, col), ChessPiece.PieceType.QUEEN));
            moveSet.add(new ChessMove(myPosition, new ChessPosition(row, col), ChessPiece.PieceType.BISHOP));
            moveSet.add(new ChessMove(myPosition, new ChessPosition(row, col), ChessPiece.PieceType.KNIGHT));
        }

        return moveSet;
    }

    public Collection<ChessMove> Diagonal(ChessBoard board, ChessPosition myPosition, int row, int col){
        HashSet<ChessMove> moveSet = new HashSet<>();
        ChessPosition newPosition = new ChessPosition(row,col);
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        if((row > 0 && row < 9)&&(col > 0 && col < 9)){
            if(board.getPiece(newPosition) != null){
                if(board.getPiece(myPosition).getTeamColor() != board.getPiece(newPosition).getTeamColor()){
                    if((color == ChessGame.TeamColor.WHITE && row ==6)|| ((color == ChessGame.TeamColor.BLACK && row ==1))){
                        moveSet.add(new ChessMove(myPosition, new ChessPosition(row, col), ChessPiece.PieceType.ROOK));
                        moveSet.add(new ChessMove(myPosition, new ChessPosition(row, col), ChessPiece.PieceType.QUEEN));
                        moveSet.add(new ChessMove(myPosition, new ChessPosition(row, col), ChessPiece.PieceType.BISHOP));
                        moveSet.add(new ChessMove(myPosition, new ChessPosition(row, col), ChessPiece.PieceType.KNIGHT));
                    }
                    else{
                        moveSet.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                    }
                }
            }
        }

        return moveSet;
    }
}
