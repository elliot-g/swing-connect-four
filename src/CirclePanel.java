package connectfour;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * The panels which the circles are drawn on (when clicked).
 *
 * @author elliot gorman
 */
class CirclePanel extends JPanel {
    /**
     * The border for a selected column of CirclePanels.
     */
    static final LineBorder SELECTED_BORDER = new LineBorder(Color.BLACK, 2);

    /**
     * The border for a unselected column of CirclePanels (default).
     */
    static final LineBorder UNSELECTED_BORDER = new LineBorder(Color.BLACK, 1);

    /**
     * The default color for the first player.
     */
    static final Color DEFAULT_PLAYER_ONE_COLOR = Color.RED;

    /**
     * The default color for the second player.
     */
    static final Color DEFAULT_PLAYER_TWO_COLOR = Color.YELLOW;

    /**
     * Flag for disabling the hovering highlights.
     */
    private static boolean blockHovers = false;

    /**
     * Flag for disabling the column highlights when a user moves their mouse across a column.
     */
    private static boolean blockColumns = true;

    /**
     * Drawing control flag for drawing greyed out hover highlights.
     */
    private boolean hover;

    /**
     * Allows control over the drawing method (when to draw circle).
     */
    private boolean draw;

    /**
     * Color for the changing player colours.
     */
    private Color color;

    /**
     * Instantiates a new Circle Panel.
     */
    CirclePanel() {
        //sets the BG and ConnectFour color to white
        setBackground(Color.WHITE);
        setOpaque(false);
        setEnabled(true);
        setPreferredSize(new Dimension(3, 3));
        setColor(Color.WHITE);
        setBorder(UNSELECTED_BORDER);
    }

    /**
     * Sets the border, only if the blockColumns flag is false.
     *
     * @param border the border to change to
     */
    void setColBorder(Border border) {
        if (blockColumns) {
            return;
        }
        super.setBorder(border);
    }

    /**
     * Setter for the column highlight flag.
     */
    static void switchColumnsHighlights() {
        blockColumns = !blockColumns;
    }

    /**
     * Getter for the column highlight flag.
     *
     * @return the column highlight flag
     */
    static boolean getColumnBlockState() {
        return blockColumns;
    }

    /**
     * Enable/Disables the hovering hints.
     */
    static void switchHovers() {
        blockHovers = !blockHovers;
    }

    /**
     * Getter for the blocking hovers flag.
     *
     * @return the blockHovers flag
     */
    static boolean getHoverState() {
        return blockHovers;
    }

    /**
     * Setter for painting control.
     *
     * @param draw either {@code true} or {@code false}
     */
    void setDraw(boolean draw) {
        this.draw = draw;
    }

    /**
     * Setter for the changer color.
     *
     * @param color the value to change to
     */
    void setColor(Color color) {
        this.color = color;
    }

    /**
     * Enables the hovering circle.
     *
     * @param hover the flag
     */
    void setHover(boolean hover) {
        //if blockHovers is set to true, the user doesn't want the highlighting on
        if (blockHovers) {
            return;
        }
        this.hover = hover;
    }

    /**
     * Getter for the hover flag.
     *
     * @return the hover flag
     */
    boolean isHover() {
        return hover;
    }

    /**
     * The painting method, responsible for painting the circles and dots
     *
     * @param graphics the graphics environment
     */
    //@Override
    public void paintComponent(Graphics graphics) {
        //refresh
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        //Antialiasing
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setBackground(null);
        graphics2D.setColor(Color.WHITE);

        if (hover) {//if the panel is being hovered, draw highlight
            Color col = GameData.getCurrentPlayerColor();
            //more transparent color
            graphics2D.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), 120));
            graphics2D.fillOval(0, 0, getWidth(), getHeight());
        }
        if (draw) { //if draw is set to true, then continue, otherwise the panel is cleared
            //draw circle on panel
            hover = false;
            graphics2D.setColor(color);
            graphics2D.fillOval(0, 0, getWidth(), getHeight());
        }
    }
}