package chess_application.model.pieces;

import chess_application.model.Board;
import chess_application.model.PieceType;
import chess_application.model.PlayerColor;

public class Rook extends Piece {

    public Rook(PlayerColor color) {
        super(color, PieceType.ROOK);
    }

    @Override
    public boolean isValidMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        
        // 1. Must move in a straight line (horizontal or vertical)
        if (fromRow != toRow && fromCol != toCol) {
            return false; 
        }

        // 2. Check for path obstructions
        if (fromRow == toRow) {
            // Horizontal movement
            int startCol = Math.min(fromCol, toCol) + 1;
            int endCol = Math.max(fromCol, toCol);
            
            for (int col = startCol; col < endCol; col++) {
                if (board.getPiece(fromRow, col) != null) {
                    return false; // Path blocked
                }
            }
        } else {
            // Vertical movement
            int startRow = Math.min(fromRow, toRow) + 1;
            int endRow = Math.max(fromRow, toRow);
            
            for (int row = startRow; row < endRow; row++) {
                if (board.getPiece(row, fromCol) != null) {
                    return false; // Path blocked
                }
            }
        }

        // 3. Check destination square (Empty or Enemy)
        Piece destinationPiece = board.getPiece(toRow, toCol);
        
        if (destinationPiece == null) {
            return true; 
        } else {
            return destinationPiece.getColor() != this.color;
        }
    }
}