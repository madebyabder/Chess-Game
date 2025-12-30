package chess_application.controller;

import chess_application.model.Board;
import chess_application.model.PlayerColor;
import chess_application.model.pieces.*;
import chess_application.view.Main_app;
import chess_application.model.PieceType;

public class GameController {

    private Board gameBoard;
    private Main_app view;

    private Piece selectedPiece = null;
    private int fromRow, fromCol;
    private PlayerColor currentPlayer = PlayerColor.WHITE;

    public GameController(Board board, Main_app view) {
        this.gameBoard = board;
        this.view = view;
    }

    public void handleSquareClick(int row, int col) {
        if (selectedPiece == null) {
            Piece pieceOnSquare = gameBoard.getPiece(row, col);
            if (pieceOnSquare != null && pieceOnSquare.getColor() == currentPlayer) {
                selectedPiece = pieceOnSquare;
                fromRow = row;
                fromCol = col;
                view.highlightSquare(row, col, true);
                
                // Optimized calculation
                calculateAndShowGhostMoves();
            }
        } else {
            view.clearAllGhostMoves();

            if (selectedPiece.isValidMove(gameBoard, fromRow, fromCol, row, col)) {
                
                // 1. Castling Detection
                boolean isCastling = false;
                int rookFromCol = -1, rookToCol = -1;
                if (selectedPiece.getType() == PieceType.KING && Math.abs(col - fromCol) == 2) {
                    isCastling = true;
                    rookFromCol = (col > fromCol) ? 7 : 0;
                    rookToCol = (col > fromCol) ? col - 1 : col + 1;
                }

                Piece capturedPiece = gameBoard.getPiece(row, col); 
                gameBoard.movePiece(fromRow, fromCol, row, col); 

                if (isCastling) gameBoard.forceMove(row, rookFromCol, row, rookToCol);
                
                // 2. King Safety Check
                if (gameBoard.isKingInCheck(currentPlayer)) {
                    // Undo move
                    gameBoard.movePiece(row, col, fromRow, fromCol); 
                    gameBoard.setPiece(row, col, capturedPiece); 
                    if (capturedPiece != null) gameBoard.removeLastCaptured(capturedPiece.getColor());
                    if (isCastling) gameBoard.forceMove(row, rookToCol, row, rookFromCol);
                } else {
                    // --- LEGAL MOVE FINALIZED ---
                    
                    // IMPORTANT: Update hasMoved state ONLY NOW
                    selectedPiece.setHasMoved(true);
                    if (isCastling) {
                        Piece rook = gameBoard.getPiece(row, rookToCol);
                        if (rook != null) rook.setHasMoved(true);
                    }

                    handlePawnPromotion(row, col);

                    currentPlayer = (currentPlayer == PlayerColor.WHITE) ? PlayerColor.BLACK : PlayerColor.WHITE;
                    view.setTurnDisplay(currentPlayer);
                    checkGameOver();
                }
            }
            
            view.highlightSquare(fromRow, fromCol, false); 
            selectedPiece = null;
            view.refreshAll();
        }
    }

    private void calculateAndShowGhostMoves() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (selectedPiece.isValidMove(gameBoard, fromRow, fromCol, r, c)) {
                    Piece captured = gameBoard.getPiece(r, c);
                    
                    // Simulate
                    gameBoard.movePiece(fromRow, fromCol, r, c);
                    boolean kingSafe = !gameBoard.isKingInCheck(currentPlayer);
                    
                    // Undo Simulation (Board.movePiece is now safe for simulations)
                    gameBoard.movePiece(r, c, fromRow, fromCol);
                    gameBoard.setPiece(r, c, captured);
                    if (captured != null) gameBoard.removeLastCaptured(captured.getColor());

                    if (kingSafe) {
                        view.showGhostMove(r, c, true);
                    }
                }
            }
        }
    }

    private void handlePawnPromotion(int row, int col) {
        Piece piece = gameBoard.getPiece(row, col);
        if (piece != null && piece.getType() == PieceType.PAWN) {
            if ((piece.getColor() == PlayerColor.WHITE && row == 0) || 
                (piece.getColor() == PlayerColor.BLACK && row == 7)) {
                
                PieceType choice = view.showPromotionDialog(piece.getColor());
                Piece newPiece;
                switch (choice) {
                    case ROOK:   newPiece = new Rook(piece.getColor()); break;
                    case BISHOP: newPiece = new Bishop(piece.getColor()); break;
                    case KNIGHT: newPiece = new Knight(piece.getColor()); break;
                    default:     newPiece = new Queen(piece.getColor()); break;
                }
                newPiece.setHasMoved(true); // Promoted pieces are considered moved
                gameBoard.promotePawn(row, col, newPiece);
            }
        }
    }

    private void checkGameOver() {
        if (!gameBoard.hasLegalMoves(currentPlayer)) {
            if (gameBoard.isKingInCheck(currentPlayer)) {
                String winner = (currentPlayer == PlayerColor.WHITE) ? "Black" : "White";
                view.showGameOver("CHECKMATE!", winner + " Wins!");
            } else {
                view.showGameOver("STALEMATE!", "The game is a draw.");
            }
        }
    }
}