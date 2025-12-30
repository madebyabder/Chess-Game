package chess_application.view;

import chess_application.controller.GameController;
import chess_application.model.Board;
import chess_application.model.PlayerColor;
import chess_application.model.pieces.Piece;
import chess_application.model.PieceType;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;

import java.util.List;

public class Main_app extends Application {

    // --- TUNED FOR LAPTOPS WITH TOP INDICATOR ---
    public static final double TILE_SIZE = 72.0; // Reduced from 80 to fit everything
    public static final int BOARD_SIZE = 8;
    public static final double CAPTURE_BAR_WIDTH = 65.0; 
    
    private final Color LIGHT_SQUARE_COLOR = Color.web("#F0D9B5");
    private final Color DARK_SQUARE_COLOR = Color.web("#B58863");
    private final Color HIGHLIGHT_COLOR = Color.color(1.0, 1.0, 0.0, 0.35);
    private final Color GHOST_MOVE_COLOR = Color.color(0.0, 0.0, 0.0, 0.2);
    
    private Board gameBoard;
    private GameController controller;
    
    private GridPane uiBoard;
    private BorderPane root;
    private StackPane[][] squarePanes = new StackPane[BOARD_SIZE][BOARD_SIZE];

    private VBox blackCapturedBox;
    private VBox whiteCapturedBox;
    private Label turnLabel;

    @Override
    public void start(Stage primaryStage) {
        setupGame(primaryStage);
    }

    private void setupGame(Stage primaryStage) {
        gameBoard = new Board();
        controller = new GameController(gameBoard, this);
        
        root = new BorderPane();
        
        // --- 1. TURN INDICATOR (TOP) ---
        turnLabel = new Label("WHITE'S TURN");
        turnLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        turnLabel.setTextFill(LIGHT_SQUARE_COLOR);
        HBox topBar = new HBox(turnLabel);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(10, 0, 5, 0)); // Very tight padding
        root.setTop(topBar);

        // --- 2. BOARD CONTAINER ---
        uiBoard = createChessBoardUI();
        StackPane boardContainer = new StackPane(uiBoard);
        boardContainer.setPadding(new Insets(10));
        boardContainer.setStyle("-fx-background-color: #2b1d13; -fx-background-radius: 8;");
        boardContainer.setEffect(new DropShadow(15, Color.BLACK));
        boardContainer.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        root.setCenter(boardContainer);

        root.setStyle("-fx-background-image: url('/wood_table.jpg'); " +
                      "-fx-background-size: cover; " +
                      "-fx-background-position: center center;");

        blackCapturedBox = createCaptureTray();
        whiteCapturedBox = createCaptureTray();
        root.setLeft(whiteCapturedBox);   
        root.setRight(blackCapturedBox); 

        // --- 3. CONTROL BAR (BOTTOM) ---
        HBox controlBar = createControlBar(primaryStage);
        root.setBottom(controlBar);

        // Optimized Margins for vertical space
        BorderPane.setAlignment(boardContainer, Pos.CENTER);
        BorderPane.setMargin(boardContainer, new Insets(5));
        BorderPane.setMargin(whiteCapturedBox, new Insets(5, 0, 5, 20));
        BorderPane.setMargin(blackCapturedBox, new Insets(5, 20, 5, 0));
        BorderPane.setMargin(controlBar, new Insets(5, 0, 15, 0)); // Keep this visible!

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        
        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setTitle("Grandmaster Chess Pro");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint(""); 
        primaryStage.show();
    }

    public void showGhostMove(int row, int col, boolean show) {
        StackPane sq = getSquare(row, col);
        if (show) {
            Circle ghostDot = new Circle(TILE_SIZE * 0.15);
            ghostDot.setFill(GHOST_MOVE_COLOR);
            ghostDot.setId("ghostDot");
            sq.getChildren().add(ghostDot);
            StackPane.setAlignment(ghostDot, Pos.CENTER);
        } else {
            sq.getChildren().removeIf(node -> "ghostDot".equals(node.getId()));
        }
    }

    public void clearAllGhostMoves() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                showGhostMove(r, c, false);
            }
        }
    }

    public void setTurnDisplay(PlayerColor color) {
        if (color == PlayerColor.WHITE) {
            turnLabel.setText("WHITE'S TURN");
            turnLabel.setTextFill(LIGHT_SQUARE_COLOR);
        } else {
            turnLabel.setText("BLACK'S TURN");
            turnLabel.setTextFill(Color.web("#ecd4b1")); // Light but distinct
        }
    }

    public PieceType showPromotionDialog(PlayerColor color) {
        Stage popup = new Stage(StageStyle.TRANSPARENT);
        popup.initModality(Modality.APPLICATION_MODAL);
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #2b1d13; -fx-background-radius: 15; -fx-border-color: #F0D9B5; -fx-border-width: 3; -fx-border-radius: 15;");

        Label label = new Label("CHOOSE PROMOTION");
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        label.setTextFill(LIGHT_SQUARE_COLOR);

        HBox options = new HBox(12);
        options.setAlignment(Pos.CENTER);
        final PieceType[] choice = {PieceType.QUEEN};
        PieceType[] types = {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT};

        for (PieceType type : types) {
            Button btn = new Button();
            btn.setStyle("-fx-background-color: #F0D9B5; -fx-background-radius: 8;");
            Piece dummyPiece = createDummyPiece(type, color);
            ImageView iv = getPieceImageView(dummyPiece, 50);
            btn.setGraphic(iv);
            btn.setOnAction(e -> { choice[0] = type; popup.close(); });
            options.getChildren().add(btn);
        }
        layout.getChildren().addAll(label, options);
        Scene scene = new Scene(layout);
        scene.setFill(Color.TRANSPARENT);
        popup.setScene(scene);
        popup.showAndWait();
        return choice[0];
    }

    private Piece createDummyPiece(PieceType type, PlayerColor color) {
        switch(type) {
            case QUEEN: return new chess_application.model.pieces.Queen(color);
            case ROOK: return new chess_application.model.pieces.Rook(color);
            case BISHOP: return new chess_application.model.pieces.Bishop(color);
            case KNIGHT: return new chess_application.model.pieces.Knight(color);
            default: return new chess_application.model.pieces.Queen(color);
        }
    }

    private HBox createControlBar(Stage stage) {
        HBox bar = new HBox(30);
        bar.setAlignment(Pos.CENTER);
        Button restartBtn = createStyledButton("RESTART");
        restartBtn.setOnAction(e -> setupGame(stage));
        Button quitBtn = createStyledButton("QUIT");
        quitBtn.setOnAction(e -> System.exit(0));
        bar.getChildren().addAll(restartBtn, quitBtn);
        return bar;
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        String style = "-fx-background-color: #F0D9B5; -fx-text-fill: #2b1d13; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 20 8 20;";
        btn.setStyle(style);
        btn.setFont(Font.font("Segoe UI", 12));
        btn.setEffect(new DropShadow(5, Color.BLACK));
        return btn;
    }

    private VBox createCaptureTray() {
        VBox tray = new VBox(8);
        tray.setPrefWidth(CAPTURE_BAR_WIDTH);
        tray.setAlignment(Pos.TOP_CENTER);
        tray.setPadding(new Insets(10));
        tray.setStyle("-fx-background-color: rgba(0, 0, 0, 0.25); -fx-background-radius: 10;");
        tray.setEffect(new InnerShadow(8, Color.BLACK));
        return tray;
    }

    private GridPane createChessBoardUI() {
        GridPane board = new GridPane();
        board.setAlignment(Pos.CENTER);
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                StackPane square = new StackPane();
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                boolean isLight = (row + col) % 2 == 0;
                tile.setFill(isLight ? LIGHT_SQUARE_COLOR : DARK_SQUARE_COLOR);
                square.getChildren().add(tile);
                addCoordinates(square, row, col, isLight);
                Piece piece = gameBoard.getPiece(row, col);
                if (piece != null) {
                    ImageView pieceView = getPieceImageView(piece, TILE_SIZE * 0.82);
                    square.getChildren().add(pieceView);
                }
                final int r = row;
                final int c = col;
                square.setOnMouseClicked(event -> controller.handleSquareClick(r, c));
                board.add(square, col, row);
                squarePanes[row][col] = square;
            }
        }
        return board;
    }

    private void addCoordinates(StackPane square, int row, int col, boolean isLight) {
        Color textColor = isLight ? DARK_SQUARE_COLOR : LIGHT_SQUARE_COLOR;
        if (col == 0) {
            Label rank = new Label(String.valueOf(8 - row));
            rank.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
            rank.setTextFill(textColor);
            square.getChildren().add(rank);
            StackPane.setAlignment(rank, Pos.TOP_LEFT);
            StackPane.setMargin(rank, new Insets(1, 0, 0, 3));
        }
        if (row == 7) {
            Label file = new Label(String.valueOf((char)('a' + col)));
            file.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
            file.setTextFill(textColor);
            square.getChildren().add(file);
            StackPane.setAlignment(file, Pos.BOTTOM_RIGHT);
            StackPane.setMargin(file, new Insets(0, 3, 1, 0));
        }
    }
    
    public void highlightSquare(int row, int col, boolean show) {
        StackPane sq = getSquare(row, col);
        if (show) {
            Rectangle highlight = new Rectangle(TILE_SIZE, TILE_SIZE, HIGHLIGHT_COLOR);
            highlight.setId("highlight");
            sq.getChildren().add(1, highlight);
        } else {
            sq.getChildren().removeIf(node -> "highlight".equals(node.getId()));
        }
    }

    public void refreshAll() {
        refreshBoardUI();
        refreshCapturedPiecesUI();
    }

    public void refreshBoardUI() {
        uiBoard = createChessBoardUI();
        StackPane container = (StackPane) root.getCenter();
        container.getChildren().setAll(uiBoard);
    }
    
    public void refreshCapturedPiecesUI() {
        blackCapturedBox.getChildren().clear();
        whiteCapturedBox.getChildren().clear();
        List<Piece> white = gameBoard.getWhiteCaptured();
        List<Piece> black = gameBoard.getBlackCaptured();
        for (Piece p : white) whiteCapturedBox.getChildren().add(getPieceImageView(p, 38));
        for (Piece p : black) blackCapturedBox.getChildren().add(getPieceImageView(p, 38));
    }
    
    public void showGameOver(String title, String message) {
        Stage popup = new Stage(StageStyle.TRANSPARENT);
        popup.initModality(Modality.APPLICATION_MODAL);
        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(30));
        box.setStyle("-fx-background-color: #2b1d13; -fx-background-radius: 12; -fx-border-color: #F0D9B5; -fx-border-width: 3; -fx-border-radius: 12;");
        box.setEffect(new DropShadow(25, Color.BLACK));
        Label t = new Label(title);
        t.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        t.setTextFill(LIGHT_SQUARE_COLOR);
        Label m = new Label(message);
        m.setFont(Font.font("Segoe UI", 16));
        m.setTextFill(Color.WHITE);
        Button btn = new Button("CLOSE");
        btn.setStyle("-fx-background-color: #F0D9B5; -fx-text-fill: #2b1d13; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 20 8 20;");
        btn.setOnAction(e -> popup.close());
        box.getChildren().addAll(t, m, btn);
        Scene s = new Scene(box);
        s.setFill(Color.TRANSPARENT);
        popup.setScene(s);
        popup.show();
    }
    
    private StackPane getSquare(int row, int col) { return squarePanes[row][col]; }
    
    private ImageView getPieceImageView(Piece piece, double size) {
        String color = (piece.getColor() == PlayerColor.WHITE) ? "White" : "Black";
        String type = piece.getType().toString();
        type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
        Image image = new Image(getClass().getResourceAsStream("/" + color + "_" + type + ".png"));
        ImageView iv = new ImageView(image);
        iv.setFitWidth(size); iv.setFitHeight(size); iv.setSmooth(true);
        return iv;
    }

    public static void main(String[] args) { launch(args); }
}