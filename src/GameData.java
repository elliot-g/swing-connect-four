package connectfour;

import java.awt.Color;
import java.awt.Point;

/**
 * The gameData class which stores all game data such as the board and the current player.
 *
 * @author elliot gorman
 */
final class GameData {
    /**
     * The number of rows in the board.
     */
    static final int ROWS = 6;

    /**
     * The number of columns in the board.
     */
    static final int COLUMNS = 7;

    /**
     * The location of the serialize file for the game.
     */
    private static final String GAME_SAVE_PATH = "connectfour/save.connect4";

    /**
     * The location of the serialize file for the second player's color.
     */
    private static final String COLOR_TWO_SAVE_PATH = "connectfour/p2.color";

    /**
     * The location of the serialize file for the first player's color.
     */
    private static final String COLOR_ONE_SAVE_PATH = "connectfour/p1.color";

    /**
     * The game board, stores each move.
     */
    private static int[][] board = new int[ROWS][COLUMNS];

    /**
     * Current player.
     */
    private static int player = 1;

    /**
     * The color for player one.
     */
    private static Color playerOneColor = (Color) SerializeSystem.load(COLOR_ONE_SAVE_PATH, 1);

    /**
     * The color for player two.
     */
    private static Color playerTwoColor = (Color) SerializeSystem.load(COLOR_TWO_SAVE_PATH, 2);

    /**
     * The winner.
     */
    private static int winner = 0;

    /**
     * The winning pieces.
     */
    private static Point[] winningPieces = new Point[4];

    /**
     * Hides the default public constructor.
     */
    private GameData() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Setter for a specific position on the board.
     *
     * @param y the row
     * @param x the column
     */
    static void setBoard(int y, int x) {
        //set the board at pos x, y to the current player
        board[y][x] = player;
        //changes player
        setPlayer();
    }

    /**
     * Setter for the board (currently unused).
     *
     * @param b the board array
     */
    private static void setBoard(int[][] b) {
        board = b;
    }

    /**
     * Returns a winning message based on the winner of the game.
     *
     * @return the message
     */
    static String winnerMessage() {
        if (getWinner() == 0) return null;
        if (getWinner() == -1) return "DRAW - NO ONE WINS";
        return getWinner() == 1 ? "PLAYER ONE WINS" : "PLAYER TWO WINS";
    }

    /**
     * Getter for player one's color.
     *
     * @return the first player's color
     */
    static Color getPlayerOneColor() {
        return playerOneColor;
    }

    /**
     * Getter for player two's color.
     *
     * @return the second player's color
     */
    static Color getPlayerTwoColor() {
        return playerTwoColor;
    }

    /**
     * Setter for the first player's colour.
     * Saves once finished.
     *
     * @param color the color to change to
     */
    static void setPlayerOneColor(Color color) {
        //set the color
        playerOneColor = color;
        //save
        SerializeSystem.serialize(playerOneColor, COLOR_ONE_SAVE_PATH);
        //change the display
        ConnectFour.activePlayerColor.setBackground(getCurrentPlayerColor());
    }

    /**
     * Setter for the second player's colour.
     * Saves once finished.
     *
     * @param color the color to change to
     */
    static void setPlayerTwoColor(Color color) {
        //set the color
        playerTwoColor = color;
        //save
        SerializeSystem.serialize(playerTwoColor, COLOR_TWO_SAVE_PATH);
        ConnectFour.activePlayerColor.setBackground(getCurrentPlayerColor());
    }

    /**
     * Gets the color of the current player.
     *
     * @return the color of the current player
     */
    static Color getCurrentPlayerColor() {
        return player == 1 ? playerOneColor : playerTwoColor;
    }

    /**
     * Prints board to the console.
     */
    static void printBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Switches player after turns.
     */
    private static void setPlayer() {
        player = player == 1 ? 2 : 1;
        ConnectFour.activePlayer.setText("PLAYER " + player + "'s TURN:");
        ConnectFour.activePlayerColor.setBackground(getCurrentPlayerColor());
    }

    /**
     * Updates the player (used when saving and loading).
     *
     * @param val the player (either 1 or 2)
     */
    static void setPlayer(int val) {
        player = val;
    }

    /**
     * Getter for the current player.
     *
     * @return the current player
     */
    static int getPlayer() {
        return player;
    }

    /**
     * Getter for the game board.
     *
     * @return the game board
     */
    static int[][] getBoard() {
        return board;
    }

    /**
     * Saves the game.
     */
    static void saveBoardData() {
        System.out.println("SAVED\n");
        SerializeSystem.serialize(board, GAME_SAVE_PATH);
    }

    /**
     * Loads the game from memory.
     */
    static void loadBoard() {
        Object s;
        if ((s = (SerializeSystem.load(GAME_SAVE_PATH, 3))) != null) {
            board = (int[][]) s;
            //get the current player
            player = nextPlayer();
            //update the display
            ConnectFour.activePlayer.setText("PLAYER " + player + "'s TURN:");
            ConnectFour.activePlayerColor.setBackground(getCurrentPlayerColor());
            System.out.println("LOADED\n");
        } else {
            System.err.println("Game load failed...");
        }
    }


    /**
     * Gets the next player after loading.
     *
     * @return the next player
     */
    private static int nextPlayer() {
        int one = 0;
        int two = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (board[i][j] == 1) {
                    one++;
                } else if (board[i][j] == 2) {
                    two++;
                }
            }
        }
        //return 2 if more 1 pieces are present, else return 1;
        return one > two ? 2 : 1;
    }

    /**
     * Resets the game to its original state.
     */
    static void reset() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j] = 0;
            }
        }
        //resets the player
        player = 1;
        //change the current player display
        ConnectFour.activePlayerColor.setBackground(GameData.getPlayerOneColor());
        ConnectFour.activePlayer.setText("PLAYER 1's TURN");
        System.out.println("NEW GAME\n");
    }

    /**
     * Finds the player on the board at a given x and y.
     *
     * @param y the y coordinate
     * @param x the x coordinate
     * @return the player (if any) in that position
     */
    private static int pieceOnBoard(int y, int x) {
        return (y < 0 || x < 0 || y >= 6 || x >= COLUMNS) ? 0 : board[y][x];
    }

    /**
     * Scans the board for a winner.
     *
     * @return the winner (if any) otherwise 0 for none, -1 for tie
     */
    static int isWinner() {
        //Horizontal check
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                if (pieceOnBoard(y, x) != 0 && pieceOnBoard(y, x) == pieceOnBoard(y, x + 1)
                        && pieceOnBoard(y, x) == pieceOnBoard(y, x + 2)
                        && pieceOnBoard(y, x) == pieceOnBoard(y, x + 3)) {
                    //Sets the winning pieces
                    winningPieces[0] = new Point(x, y);
                    winningPieces[1] = new Point(x + 1, y);
                    winningPieces[2] = new Point(x + 2, y);
                    winningPieces[3] = new Point(x + 3, y);
                    return pieceOnBoard(y, x);
                }
            }
        }

        //Vertical check
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                if (pieceOnBoard(y, x) != 0 && pieceOnBoard(y, x) == pieceOnBoard(y + 1, x)
                        && pieceOnBoard(y, x) == pieceOnBoard(y + 2, x)
                        && pieceOnBoard(y, x) == pieceOnBoard(y + 3, x)) {
                    //sets the winning pieces
                    winningPieces[0] = new Point(x, y);
                    winningPieces[1] = new Point(x, y + 1);
                    winningPieces[2] = new Point(x, y + 2);
                    winningPieces[3] = new Point(x, y + 3);
                    return pieceOnBoard(y, x);
                }
            }
        }

        //Diagonal check
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                //diagonal search
                for (int d = -1; d <= 1; d += 2)
                    if (pieceOnBoard(y, x) != 0 && pieceOnBoard(y, x) == pieceOnBoard(y + d, x + 1)
                            && pieceOnBoard(y, x) == pieceOnBoard(y + 2 * d, x + 2)
                            && pieceOnBoard(y, x) == pieceOnBoard(y + 3 * d, x + 3)) {
                        //sets the winning pieces
                        winningPieces[0] = new Point(x, y);
                        winningPieces[1] = new Point(x + 1, y + d);
                        winningPieces[2] = new Point(x + 2, y + 2 * d);
                        winningPieces[3] = new Point(x + 3, y + 3 * d);
                        return pieceOnBoard(y, x);
                    }
            }
        }

        // if No one wins
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                if (pieceOnBoard(y, x) == 0) return 0;
            }
        }
        return -1; //tie
    }

    /**
     * Gets the next available space in a column.
     *
     * @param col the column to search
     * @return the next available space (if any)
     */
    static int nextAvailableSpace(int col) {
        for (int i = 5; i >= 0; i--) {
            if (board[i][col] == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Getter for the winning pieces array.
     *
     * @return the Point array
     */
    static Point[] getWinningPieces() {
        return winningPieces;
    }

    /**
     * Sets the winner.
     *
     * @param winner the player
     */
    static void setWinner(int winner) {
        GameData.winner = winner;
    }

    /**
     * Getter for the winning player.
     *
     * @return the winner
     */
    static int getWinner() {
        return winner;
    }
}
