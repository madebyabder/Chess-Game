# Chess-Game

A fully-featured chess game application built with Java and JavaFX, implementing complete chess rules with an elegant graphical user interface.

![Chess Game](https://img.shields.io/badge/Java-Chess%20Game-blue)
![JavaFX](https://img.shields.io/badge/JavaFX-UI%20Framework-orange)
![License](https://img.shields.io/badge/License-MIT-green)

## 🎮 Features

### Core Gameplay
- **Complete Chess Rules**: All 6 piece types (Pawn, Rook, Knight, Bishop, Queen, King) with proper movement validation
- **Castling**: Kingside and Queenside castling with proper validation
- **Pawn Promotion**: Automatic promotion dialog when pawns reach the 8th rank
- **Check Detection**: Real-time check detection prevents illegal moves
- **Checkmate & Stalemate**: Automatic game end detection with victory/draw announcements
- **Move Validation**: Prevents moves that would leave your king in check

### User Interface
- **Visual Feedback**: 
  - Highlighted selected pieces
  - Ghost move indicators showing valid moves
  - Coordinate labels (a-h files, 1-8 ranks)
- **Captured Pieces Display**: Side panels showing all captured pieces for both players
- **Turn Indicator**: Clear display of whose turn it is
- **Game Over Dialogs**: Elegant popup notifications for checkmate and stalemate
- **Full-Screen Mode**: Immersive gaming experience with wood texture background
- **Modern UI**: Polished interface with shadows, rounded corners, and professional styling

### Technical Features
- **MVC Architecture**: Clean separation of Model, View, and Controller
- **Java 9+ Modules**: Modern Java module system implementation
- **Object-Oriented Design**: Extensible piece hierarchy with abstract base class
- **Move Simulation**: Safe move validation through simulation without state corruption

## 📋 Requirements

- **Java Development Kit (JDK)**: Version 11 or higher
- **JavaFX**: Included with JDK 11+ (or separate installation for older versions)
- **IDE**: Eclipse, IntelliJ IDEA, or any Java-compatible IDE (optional)

## 🚀 Getting Started

### Prerequisites

1. **Install JDK 11+**
   - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
   - Verify installation:
     ```bash
     java -version
     javac -version
     ```

2. **Verify JavaFX** (usually included with JDK 11+)
   - If using JDK 8 or earlier, download JavaFX SDK separately

### Running the Application

#### Option 1: Using Eclipse
1. Open Eclipse IDE
2. File → Import → Existing Projects into Workspace
3. Select the `Chess_Game` directory
4. Right-click on `Main_app.java` → Run As → Java Application

#### Option 2: Command Line
```bash
# Navigate to the project directory
cd Chess_Game

# Compile the project
javac --module-path <path-to-javafx-lib> --add-modules javafx.controls,javafx.graphics -d bin src/chess_application/**/*.java src/module-info.java

# Run the application
java --module-path <path-to-javafx-lib> --add-modules javafx.controls,javafx.graphics -cp bin chess_application.view.Main_app
```

#### Option 3: Using Maven/Gradle (if configured)
```bash
mvn clean javafx:run
# or
./gradlew run
```

## 🎯 How to Play

1. **Starting the Game**: Launch the application - the board will initialize with pieces in standard starting positions
2. **Selecting a Piece**: Click on any piece of your color (indicated by the turn indicator at the top)
3. **Viewing Valid Moves**: After selecting a piece, ghost dots will appear on all valid destination squares
4. **Making a Move**: Click on a highlighted destination square to move your piece
5. **Capturing**: Click on an enemy piece to capture it (captured pieces appear in side panels)
6. **Castling**: Move your king two squares horizontally toward a rook (if conditions are met)
7. **Pawn Promotion**: When a pawn reaches the opposite end, a dialog will appear to choose promotion piece
8. **Game End**: The game automatically detects checkmate or stalemate and displays a victory/draw message

### Controls
- **Mouse Click**: Select pieces and make moves
- **Restart Button**: Start a new game
- **Quit Button**: Exit the application

## 📁 Project Structure

```
Chess_Game/
├── src/
│   ├── chess_application/
│   │   ├── controller/
│   │   │   └── GameController.java      # Game logic and move handling
│   │   ├── model/
│   │   │   ├── Board.java               # Board state and game rules
│   │   │   ├── PlayerColor.java         # Enum for WHITE/BLACK
│   │   │   ├── PieceType.java           # Enum for piece types
│   │   │   └── pieces/
│   │   │       ├── Piece.java           # Abstract base class
│   │   │       ├── Pawn.java            # Pawn movement logic
│   │   │       ├── Rook.java            # Rook movement logic
│   │   │       ├── Knight.java          # Knight movement logic
│   │   │       ├── Bishop.java          # Bishop movement logic
│   │   │       ├── Queen.java           # Queen movement logic
│   │   │       └── King.java            # King movement + castling
│   │   └── view/
│   │       └── Main_app.java            # JavaFX UI and rendering
│   ├── module-info.java                 # Java module configuration
│   └── resources/                       # Image assets
│       ├── White_*.png                  # White piece images
│       ├── Black_*.png                  # Black piece images
│       └── wood_table.jpg               # Background texture
└── README.md
```

## 🏗️ Architecture

### Model-View-Controller (MVC) Pattern

- **Model** (`chess_application.model`): 
  - `Board`: Manages game state, piece positions, captured pieces, and game rules
  - `Piece` classes: Implement movement validation for each piece type
  
- **View** (`chess_application.view`):
  - `Main_app`: Handles all UI rendering, user interactions, and visual feedback
  
- **Controller** (`chess_application.controller`):
  - `GameController`: Orchestrates game flow, validates moves, handles special rules (castling, promotion)

### Key Design Patterns

- **Strategy Pattern**: Each piece type implements its own movement strategy
- **Template Method**: Abstract `Piece` class defines the interface, subclasses implement specifics
- **Observer Pattern**: View updates based on model state changes

## 🎨 Customization

### Changing Colors
Edit color constants in `Main_app.java`:
```java
private final Color LIGHT_SQUARE_COLOR = Color.web("#F0D9B5");
private final Color DARK_SQUARE_COLOR = Color.web("#B58863");
```

### Adjusting Board Size
Modify constants in `Main_app.java`:
```java
public static final double TILE_SIZE = 72.0;
public static final int BOARD_SIZE = 8;
```

### Adding New Features
The modular architecture makes it easy to extend:
- Add new piece types by extending the `Piece` class
- Implement new game modes in `GameController`
- Customize UI elements in `Main_app`

## 🐛 Known Limitations

- **En Passant**: Not yet implemented (marked in code comments)
- **Threefold Repetition**: Draw detection for repeated positions not implemented
- **Fifty-Move Rule**: Draw detection for 50 moves without capture/pawn move not implemented
- **Time Controls**: No timer/clock functionality

## 🔮 Future Enhancements

Potential features for future versions:
- [ ] En passant capture
- [ ] Move history and undo/redo
- [ ] Save/load game functionality
- [ ] AI opponent
- [ ] Online multiplayer
- [ ] Move notation (PGN) export
- [ ] Game replay
- [ ] Sound effects
- [ ] Different board themes

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. Areas where help is appreciated:
- Bug fixes
- Feature implementations (especially en passant)
- UI/UX improvements
- Code optimization
- Documentation

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

## 👨‍💻 Author

Developed as a comprehensive chess game implementation demonstrating:
- Object-oriented programming principles
- JavaFX GUI development
- Game logic implementation
- Software architecture patterns

## 🙏 Acknowledgments

- Chess piece images and assets
- JavaFX community for excellent documentation
- Standard chess rules as defined by FIDE

---

**Enjoy playing chess!** ♟️

For issues, questions, or suggestions, please open an issue on the GitHub repository.

