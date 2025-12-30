package chess_application.model.pieces;

import chess_application.model.Board;
import chess_application.model.PieceType;
import chess_application.model.PlayerColor;

public class Queen extends Piece {

    public Queen(PlayerColor color) {
        super(color, PieceType.QUEEN);
    }

    @Override
    public boolean isValidMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        
        // --- This is a clever way to re-use our code ---
        
        // 1. Create a "phantom" Rook and Bishop of the same color
        // We're not adding them to the board, just using their move logic
        Rook rookLogic = new Rook(this.color);
        Bishop bishopLogic = new Bishop(this.color);

        // 2. Check if the move is valid for EITHER a Rook OR a Bishop
        // If it is, then it's a valid Queen move.
        if (rookLogic.isValidMove(board, fromRow, fromCol, toRow, toCol) || 
            bishopLogic.isValidMove(board, fromRow, fromCol, toRow, toCol)) {
            
            return true;
        }

        // If it's not a valid Rook or Bishop move, it's invalid.
        return false;
    }
}