package chess_application.model.pieces;

import chess_application.model.Board;
import chess_application.model.PieceType;
import chess_application.model.PlayerColor;

public class Bishop extends Piece {

    public Bishop(PlayerColor color) {
        super(color, PieceType.BISHOP);
    }

    @Override
    public boolean isValidMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        
        // 1. Check if the move is not diagonal
        if (Math.abs(toRow - fromRow) != Math.abs(toCol - fromCol)) {
            return false; // Not a diagonal move
        }

        // 2. Check for pieces in the path
        int rowStep = (toRow - fromRow) > 0 ? 1 : -1;
        int colStep = (toCol - fromCol) > 0 ? 1 : -1;

        int currentRow = fromRow + rowStep;
        int currentCol = fromCol + colStep;

        // Loop along the diagonal path until the destination
        while (currentRow != toRow && currentCol != toCol) {
            if (board.getPiece(currentRow, currentCol) != null) {
                return false; // Path is blocked
            }
            currentRow += rowStep;
            currentCol += colStep;
        }

        // 3. Check the destination square
        Piece destinationPiece = board.getPiece(toRow, toCol);
        
        if (destinationPiece == null) {
            return true; // Move to an empty square is valid
        } else {
            // Can only move if the destination piece is the opposite color
            return destinationPiece.getColor() != this.color;
        }
    }
}