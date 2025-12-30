package chess_application.model.pieces;

import chess_application.model.Board;
import chess_application.model.PieceType;
import chess_application.model.PlayerColor;

public class Knight extends Piece {

    public Knight(PlayerColor color) {
        super(color, PieceType.KNIGHT);
    }

    @Override
    public boolean isValidMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        
        // 1. Calculate the absolute difference in rows and columns
        int deltaRow = Math.abs(toRow - fromRow);
        int deltaCol = Math.abs(toCol - fromCol);

        // 2. Check for the "L" shape
        // A valid L-shape is always a 2+1 or 1+2 combination
        boolean isLShape = (deltaRow == 2 && deltaCol == 1) || 
                           (deltaRow == 1 && deltaCol == 2);
                           
        if (!isLShape) {
            return false; // Not a valid L-shape
        }

        // 3. Check the destination square
        // Since the Knight jumps, we DON'T check the path.
        Piece destinationPiece = board.getPiece(toRow, toCol);
        
        if (destinationPiece == null) {
            return true; // Move to an empty square is valid
        } else {
            // Can only move if the destination piece is the opposite color
            return destinationPiece.getColor() != this.color;
        }
    }
}