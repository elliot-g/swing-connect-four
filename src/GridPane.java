package connectfour;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The grid component which houses all the pieces.
 *
 * @author elliot gorman
 */
class GridPane extends JPanel {
    /**
     * Flag for when to draw the black stroke through the winning pieces.
     */
    private boolean won = false;

    /**
     * The individual panels that make up the grid.
     */
    private static CirclePanel[][] cList = new CirclePanel[GameData.COLUMNS][GameData.COLUMNS];

    /**
     * Initializes each of the panels that make up the grid.
     */
    private void initPanels() {
        int count = 0; // used to give names to boxes

        //loops through all the panels
        for (int i = 0; i < GameData.ROWS; i++) {
            for (int j = 0; j < GameData.COLUMNS; j++) {
                //initializes
                CirclePanel circlePanel = new CirclePanel();
                //adds the event listener
                circlePanel.addMouseListener(new BoxListener());
                //set the size, border and name
                circlePanel.setName("" + (count % GameData.COLUMNS) + "," + (count / GameData.COLUMNS)); //gives each panel a name according to their x and y

                //add this panel to this list
                cList[i][j] = circlePanel;
                ++count;
                //add the panel to the grid
                add(circlePanel);
            }
        }
    }

    /**
     * Instantiates a new GridPane component.
     */
    GridPane() {
        //sets basic parameters of the pane
        setOpaque(true);
        setBackground(Color.WHITE);
        //grid layout for the grid constraints
        setLayout(new GridLayout(GameData.ROWS, GameData.COLUMNS));
        //sets the border
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        //initialize panels
        initPanels();
    }

    /**
     * Removes all the "hovering" pieces present, and places the first
     * "hovering", greyed out piece at the bottom of the board where
     * the user's mouse is.
     */
    static void disableLastHover() {
        for (int i = 0; i < GameData.ROWS; i++) {
            for (int j = 0; j < GameData.COLUMNS; j++) {
                if (cList[i][j].isHover()) {
                    int n = GameData.nextAvailableSpace(j);
                    cList[i][j].setHover(false);
                    cList[i][j].repaint();
                    cList[n][j].setHover(true);
                    cList[n][j].repaint();
                    return;
                }
            }
        }
    }

    /**
     * Refreshes each of the panels in the grid.
     */
    static void refresh() {
        //loops through all the panels
        for (int i = 0; i < GameData.ROWS; i++) {
            for (int j = 0; j < GameData.COLUMNS; j++) {
                //if move was performed on the panel
                if (GameData.getBoard()[i][j] != 0) {
                    //draw is true
                    cList[i][j].setDraw(true);
                    //changes color depending on player
                    if (GameData.getBoard()[i][j] == 2) {
                        cList[i][j].setColor(GameData.getPlayerTwoColor());
                    } else {
                        cList[i][j].setColor(GameData.getPlayerOneColor());
                    }
                    //repaint that panel
                    cList[i][j].repaint();
                } else {
                    //don't draw, just repaint
                    cList[i][j].setDraw(false);
                    cList[i][j].repaint();
                }
            }
        }
    }

    /**
     * Paints the stroke through the winning pieces.
     *
     * @param g the graphics context
     */
    //@Override
    //overrides paint and not paintComponent because paint() doesn't call paintChildren(), which would hide the line.
    public void paint(Graphics g) {
        super.paint(g);
        //if there is a winner
        if (won) {
            //get the first winning piece
            CirclePanel winStart = cList[(int) GameData.getWinningPieces()[0].getY()][(int) GameData.getWinningPieces()[0].getX()];
            //get the last winning piece
            CirclePanel winEnd = cList[(int) GameData.getWinningPieces()[3].getY()][(int) GameData.getWinningPieces()[3].getX()];

            Graphics2D graphics2D = (Graphics2D) g;
            //antialiasing
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //black line
            graphics2D.setColor(Color.BLACK);
            //thicker brush
            graphics2D.setStroke(new BasicStroke(12));

            //draw line from middle of first piece to middle of last piece
            graphics2D.drawLine(winStart.getX() + winStart.getWidth() / 2, winStart.getY() + winStart.getWidth() / 2, winEnd.getX() + winEnd.getWidth() / 2, winEnd.getY() + winEnd.getWidth() / 2);
            //reset won flag
            won = false;
        }
    }

    /**
     * The mouseListener applied to each panel in the grid.
     *
     * @author elliot gorman
     */
    class BoxListener extends MouseAdapter {

        /**
         * When the mouse enters a CirclePanel, takes appropriate action,
         * by highlighting the column and displaying the greyed out piece
         * (if the user hasn't chosen to block the highlights).
         *
         * @param e the mouseEvent
         */
        //@Override
        public void mouseEntered(MouseEvent e) {
            //gets the source and it's name
            CirclePanel source = (CirclePanel) e.getSource();
            String name = source.getName();

            //gets the x and y position of the panel
            int y = Integer.parseInt(name.substring(0, name.indexOf(',')));
            int x = GameData.nextAvailableSpace(y);

            //enable the hovering
            if (x != -1) {
                cList[x][y].setHover(true);
                cList[x][y].repaint();
            }

            //darken the column border.
            for (int i = 0; i < GameData.ROWS; i++) {
                cList[i][y].setColBorder(CirclePanel.SELECTED_BORDER);
            }
        }

        /**
         * When the mouse enters a CirclePanel, takes appropriate action,
         * by removing the highlighting on the column and the greyed out piece
         * (if the user hasn't chosen to block the highlights).
         *
         * @param e the mouseEvent
         */
        //@Override
        public void mouseExited(MouseEvent e) {
            //gets the source and it's name
            CirclePanel source = (CirclePanel) e.getSource();
            String name = source.getName();

            //gets the x and y position of the panel
            int y = Integer.parseInt(name.substring(0, name.indexOf(',')));
            int x = GameData.nextAvailableSpace(y);

            //remove the hovering piece
            if (x != -1) {
                cList[x][y].setHover(false);
                cList[x][y].repaint();
            }

            //set the border back to default for that column
            for (int i = 0; i < GameData.ROWS; i++) {
                cList[i][y].setColBorder(CirclePanel.UNSELECTED_BORDER);
            }
        }


        /**
         * The 'on clicked method', processes the user's clicks on the panels.
         *
         * @param e the mouseEvent
         */
        //@Override
        public void mouseClicked(MouseEvent e) {
            //gets the invoking panel
            CirclePanel clickedBox = (CirclePanel) e.getSource();
            //gets the name (which is it's pos)
            String name = clickedBox.getName();

            //gets the column from it's name
            int x = Integer.parseInt(name.substring(0, name.indexOf(',')));
            //calculates the y position (the row)
            int y = GameData.nextAvailableSpace(x);

            //if there is a position in that column to move to
            if (y >= 0) {
                cList[y][x].setHover(false);
                cList[y][x].repaint();

                if (y != 0) {
                    cList[y - 1][x].setHover(true);
                    cList[y - 1][x].repaint();
                }

                //set the game board at position y,x to the current player
                GameData.setBoard(y, x);

                //print the board to the terminal
                GameData.printBoard();

                //refresh the painting
                GridPane.refresh();

                //check for winner
                GameData.setWinner(GameData.isWinner());

                //if someone won or draw
                if (GameData.getWinner() != 0) {
                    if (y != 0) {
                        cList[y - 1][x].setHover(false);
                        cList[y - 1][x].repaint();
                    }
                    //beep
                    Toolkit.getDefaultToolkit().beep();
                    //if someone actually won and it wasn't a tie
                    if (GameData.getWinner() != -1) {
                        //won flag is true, repaint for line through pieces.
                        won = true;
                        repaint();
                    }

                    //popup message box
                    int input = JOptionPane.showOptionDialog(GridPane.this, GameData.winnerMessage(),
                            "GAME OVER", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE,
                            null, null, null);
                    //reset the game when user presses Ok or closes the tab
                    if (input == JOptionPane.OK_OPTION || input == -1) {
                        //reset
                        GameData.reset();
                        refresh();
                        //reset the active player display
                        ConnectFour.activePlayerColor.setBackground(GameData.getPlayerOneColor());
                        ConnectFour.activePlayer.setText("PLAYER 1's TURN");
                    }
                }
            }
        }
    }
}
