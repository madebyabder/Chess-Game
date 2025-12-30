package chess_application.model.pieces;

// --- These are the 3 imports you need ---
import chess_application.model.Board; // <-- This one was missing
import chess_application.model.PieceType;
import chess_application.model.PlayerColor;
// We don't need to import Piece, since Pawn is in the same package

public class Pawn extends Piece {

    public Pawn(PlayerColor color) {
        super(color, PieceType.PAWN);
    }

    @Override
    public boolean isValidMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        
        // 1. Determine direction of movement (White moves "up", Black moves "down")
        // Note: Row 0 is the top, Row 7 is the bottom
        int direction = (this.color == PlayerColor.WHITE) ? -1 : 1;
        
        // 2. Check for standard 1-square move
        if (fromCol == toCol && toRow == fromRow + direction) {
            // Check if the destination square is empty
            return board.getPiece(toRow, toCol) == null;
        }

        // 3. Check for 2-square "first move"
        boolean isFirstMove = (this.color == PlayerColor.WHITE && fromRow == 6) || 
                              (this.color == PlayerColor.BLACK && fromRow == 1);
        
        if (isFirstMove && fromCol == toCol && toRow == fromRow + (2 * direction)) {
            // Check if both the destination square AND the square in between are empty
            return board.getPiece(toRow, toCol) == null && 
                   board.getPiece(fromRow + direction, fromCol) == null;
        }

        // 4. Check for diagonal capture
        if (Math.abs(fromCol - toCol) == 1 && toRow == fromRow + direction) {
            // Check if the destination square has an ENEMY piece
            Piece targetPiece = board.getPiece(toRow, toCol);
            return targetPiece != null && targetPiece.getColor() != this.color;
        }
        
        // --- En Passant and Promotion will be added later ---

        // If none of the above, it's an invalid move
        return false;
    }
}