package chess_application.model.pieces;

import chess_application.model.Board;
import chess_application.model.PieceType;
import chess_application.model.PlayerColor;

public class King extends Piece {

    public King(PlayerColor color) {
        super(color, PieceType.KING);
    }

    @Override
    public boolean isValidMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        
        int deltaRow = Math.abs(toRow - fromRow);
        int deltaCol = Math.abs(toCol - fromCol);

        // --- 1. STANDARD KING MOVE (1 square in any direction) ---
        if (deltaRow <= 1 && deltaCol <= 1) {
            if (deltaRow == 0 && deltaCol == 0) return false; // Same square

            Piece destinationPiece = board.getPiece(toRow, toCol);
            if (destinationPiece == null) {
                return true; 
            } else {
                return destinationPiece.getColor() != this.color;
            }
        }

        // --- 2. CASTLING MOVE (2 squares horizontally) ---
        if (!this.hasMoved() && deltaRow == 0 && deltaCol == 2) {
            
            // Rule: Cannot castle out of check
            if (board.isKingInCheck(this.getColor())) {
                return false;
            }

            // Identify which Rook we are trying to castle with
            int rookCol = (toCol > fromCol) ? 7 : 0; // Kingside (7) or Queenside (0)
            Piece rook = board.getPiece(fromRow, rookCol);

            // Rule: Rook must exist, must be a Rook, and must NOT have moved
            if (rook != null && rook.getType() == PieceType.ROOK && !rook.hasMoved()) {
                
                // Rule: Path between King and Rook must be empty
                int step = (toCol > fromCol) ? 1 : -1;
                for (int c = fromCol + step; c != rookCol; c += step) {
                    if (board.getPiece(fromRow, c) != null) {
                        return false; // Path is blocked
                    }
                }
                
                // Note: The controller/board should handle "passing through check" 
                // but for basic validity, the path is now clear.
                return true;
            }
        }

        return false; 
    }
}