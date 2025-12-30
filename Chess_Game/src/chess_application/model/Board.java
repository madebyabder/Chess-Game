package chess_application.model;

import chess_application.model.pieces.*; 
import java.util.ArrayList; 
import java.util.List;    

public class Board {

    private Piece[][] grid;
    private List<Piece> whiteCaptured;
    private List<Piece> blackCaptured;

    public Board() {
        this.grid = new Piece[8][8]; 
        this.whiteCaptured = new ArrayList<>();
        this.blackCaptured = new ArrayList<>();
        setupPieces();
    }
    
    // --- GETTER METHODS ---
    public List<Piece> getWhiteCaptured() { return whiteCaptured; }
    public List<Piece> getBlackCaptured() { return blackCaptured; }

    public Piece getPiece(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) return null;
        return grid[row][col];
    }
    
    // --- PIECE MOVEMENT METHODS ---

    /**
     * Standard move method. 
     * NOTE: setHasMoved(true) is now handled by GameController 
     * to avoid breaking logic during simulations.
     */
    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Piece pieceToMove = getPiece(fromRow, fromCol);
        Piece capturedPiece = getPiece(toRow, toCol); 
        
        if (capturedPiece != null) {
            if (capturedPiece.getColor() == PlayerColor.WHITE) {
                whiteCaptured.add(capturedPiece);
            } else {
                blackCaptured.add(capturedPiece);
            }
        }
        
        grid[fromRow][fromCol] = null; 
        grid[toRow][toCol] = pieceToMove;
    }

    /**
     * Special method for Castling or Simulations.
     * Simply teleports the piece without any extra logic.
     */
    public void forceMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece p = grid[fromRow][fromCol];
        grid[toRow][toCol] = p;
        grid[fromRow][fromCol] = null;
    }

    public void setPiece(int row, int col, Piece piece) {
        grid[row][col] = piece;
    }

    public void promotePawn(int row, int col, Piece newPiece) {
        grid[row][col] = newPiece;
    }

    public void removeLastCaptured(PlayerColor capturedColor) {
        if (capturedColor == PlayerColor.WHITE) {
            if (!whiteCaptured.isEmpty()) whiteCaptured.remove(whiteCaptured.size() - 1);
        } else {
            if (!blackCaptured.isEmpty()) blackCaptured.remove(blackCaptured.size() - 1);
        }
    }

    // --- CHECK LOGIC METHODS ---

    public boolean isKingInCheck(PlayerColor kingColor) {
        int[] kingPos = findKing(kingColor);
        if (kingPos == null) return false;
        
        int kingRow = kingPos[0];
        int kingCol = kingPos[1];

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = getPiece(row, col);
                if (piece != null && piece.getColor() != kingColor) {
                    if (piece.isValidMove(this, row, col, kingRow, kingCol)) {
                        return true; 
                    }
                }
            }
        }
        return false; 
    }

    private int[] findKing(PlayerColor kingColor) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = getPiece(row, col);
                if (piece != null && piece.getType() == PieceType.KING && piece.getColor() == kingColor) {
                    return new int[] { row, col };
                }
            }
        }
        return null;
    }

    private void setupPieces() {
        // Black Pieces
        grid[0][0] = new Rook(PlayerColor.BLACK);
        grid[0][1] = new Knight(PlayerColor.BLACK);
        grid[0][2] = new Bishop(PlayerColor.BLACK);
        grid[0][3] = new Queen(PlayerColor.BLACK);
        grid[0][4] = new King(PlayerColor.BLACK);
        grid[0][5] = new Bishop(PlayerColor.BLACK);
        grid[0][6] = new Knight(PlayerColor.BLACK);
        grid[0][7] = new Rook(PlayerColor.BLACK);
        for (int col = 0; col < 8; col++) grid[1][col] = new Pawn(PlayerColor.BLACK);

        // White Pieces
        for (int col = 0; col < 8; col++) grid[6][col] = new Pawn(PlayerColor.WHITE);
        grid[7][0] = new Rook(PlayerColor.WHITE);
        grid[7][1] = new Knight(PlayerColor.WHITE);
        grid[7][2] = new Bishop(PlayerColor.WHITE);
        grid[7][3] = new Queen(PlayerColor.WHITE);
        grid[7][4] = new King(PlayerColor.WHITE);
        grid[7][5] = new Bishop(PlayerColor.WHITE);
        grid[7][6] = new Knight(PlayerColor.WHITE);
        grid[7][7] = new Rook(PlayerColor.WHITE);
    }

    public boolean hasLegalMoves(PlayerColor playerColor) {
        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Piece piece = getPiece(fromRow, fromCol);
                if (piece != null && piece.getColor() == playerColor) {
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            if (piece.isValidMove(this, fromRow, fromCol, toRow, toCol)) {
                                Piece capturedPiece = getPiece(toRow, toCol);
                                movePiece(fromRow, fromCol, toRow, toCol);
                                boolean kingSafe = !isKingInCheck(playerColor);
                                
                                movePiece(toRow, toCol, fromRow, fromCol);
                                setPiece(toRow, toCol, capturedPiece);
                                if (capturedPiece != null) removeLastCaptured(capturedPiece.getColor());
                                
                                if (kingSafe) return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}