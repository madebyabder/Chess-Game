package chess_application.model.pieces;

import chess_application.model.Board;
import chess_application.model.PieceType;
import chess_application.model.PlayerColor;

public abstract class Piece {
    
    protected PlayerColor color;
    protected PieceType type;
    // --- NEW FIELD FOR CASTLING & PAWN LOGIC ---
    private boolean hasMoved = false; 
    
    public Piece(PlayerColor color, PieceType type) {
        this.color = color;
        this.type = type;
    }

    public PlayerColor getColor() {
        return color;
    }

    public PieceType getType() {
        return type;
    }

    // --- NEW GETTER AND SETTER ---
    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
    
    public abstract boolean isValidMove(Board board, int fromRow, int fromCol, int toRow, int toCol);
}