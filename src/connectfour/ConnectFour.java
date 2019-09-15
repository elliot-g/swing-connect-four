package connectfour;

/*
* Author: Elliot Gorman
* Date: June 2019
* Purpose: A connect four game which has save & load functions
* along with some other functionality.
* ICS-4U1 Final Culminating/ISU
* Notes:
   - This program was written in Java Swing, so that it may be compatible with
   very early versions of Java, such as 1.3 and 1.4. This means that the
   program can be run using the ReadyToProgram compiler.
   - Additional info about the project can be found in the README and the
   program itself includes a 'help' and 'about' section.
   - Documentation for this project was written in JavaDoc style. All classes, 
   class-fields, and methods are documented with JavaDoc. In the /documentary/
   folder is the official documentation, provided in the JavaDoc Standard Format.
   To open the documentation, navigate to the /documentation/ folder and open
   index (DOCUMENTATION IN HERE).html (it will have "DOCUMENTATION IN HERE" in the title) with any common
   browser.
   - Metadata such as @Override are commented out, due to certain compilers
   not accepting them.
   - The game serializes to files to make tampering with them harder. Regardless,
   the program will continue to fully operate if it cannot load or find the save
   files, as it will simply load the defaults.
*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;

/**
 * The Main class, builds the GUI, then dispatches the GUI in the main method.
 *
 * @author elliot gorman
 */
final class ConnectFour {
    /**
     * Main method, launches GUI.
     *
     * @param args the input arguments (unused)
     */
    public static void main(String[] args) {

        try {
            //Sets the look and feel to the default, cross-platform look and feel
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //Creates GUI on Event Dispatching Thread
        SwingUtilities.invokeLater(new Runnable() {
            //@Override
            public void run() {
                //starts GUI
                new ConnectFour().main.setVisible(true);
            }
        });
    }

    /**
     * Calls the initialize component and listener method.
     */
    private ConnectFour() {
        initListeners();
        initComponents();
    }

    /**
     * Creates each of the anonymous inner class listeners for the menu items.
     */
    private void initListeners() {

        disableColumnHighlightsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                CirclePanel.switchColumnsHighlights();
                disableColumnHighlightsItem.setText(CirclePanel.getColumnBlockState() ? "Enable Column Highlights" : "Disable Column Highlights");
            }
        });

        /*
         * The listener for disable/enable the hover highlights.
         */
        disableHoversMenuItem.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent actionEvent) {
                //switches on/off
                CirclePanel.switchHovers();
                disableHoversMenuItem.setText(CirclePanel.getHoverState() ? "Enable Hover Highlights" : "Disable Hover Highlights");
            }
        });

	/*
	 The listener for the save option
	 */
        saveMenuItem.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent actionEvent) {
                //save, then get player
                GameData.saveBoardData();
            }
        });

	/*
	 The listener for the load option
	 */
        loadMenuItem.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent actionEvent) {
                //load then get player then refresh
                GameData.loadBoard();
                GridPane.refresh();
                //reset the hovering
                GridPane.disableLastHover();
                //print board as well
                GameData.printBoard();

            }
        });

	/*
	 The listener for the reset option
	 */
        resetMenuItem.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent actionEvent) {
                //reset then refresh
                GameData.reset();
                GridPane.refresh();
                //reset the hovering
                GridPane.disableLastHover();
            }
        });

	/*
	 The listener for the aboutFrame option
	 */
        aboutMenuItem.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent actionEvent) {
                //set the frame to visible
                aboutFrame.setVisible(true);
            }
        });

	/*
	 The listener for the colorChanger option
	 */
        colorChooserMenuItem.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent actionEvent) {
                //set that frame to visible
                colorChooserFrame.setVisible(true);
            }
        });

        defaultColors.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent actionEvent) {
                GameData.setPlayerOneColor(CirclePanel.DEFAULT_PLAYER_ONE_COLOR);
                GameData.setPlayerTwoColor(CirclePanel.DEFAULT_PLAYER_TWO_COLOR);
                GridPane.refresh();
            }
        });

	/*
	The listener for the player 1 color changing button
	 */
        pieceOne.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent actionEvent) {
                GameData.setPlayerOneColor(colorChooser.getColor());
                GridPane.refresh();
            }
        });

	   /*
	    The listener for the player 2 color changing button
	    */
        pieceTwo.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent actionEvent) {
                GameData.setPlayerTwoColor(colorChooser.getColor());
                GridPane.refresh();
            }
        });

        creditsMenuItem.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent actionEvent) {
                creditsFrame.setVisible(true);
            }
        });
    }

    /**
     * Initializes the needed Swing components
     */
    private void initComponents() {
        activePlayerColor.setPreferredSize(new Dimension(100, 20));
        activePlayerColor.setOpaque(true);
        activePlayerColor.setBackground(GameData.getPlayerOneColor());

        //some metrics of the frame are set here
        main.setBackground(Color.WHITE);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.getContentPane().setLayout(new BorderLayout());
        main.setSize(new Dimension(653, 612));
        main.getContentPane().add(new GridPane(), BorderLayout.CENTER);
        main.setIconImage(icon.getImage());

        //sets some parameters of the color choosing window
        colorChooserFrame.getContentPane().setLayout(new BorderLayout());
        colorChooserFrame.setSize(650, 425);
        colorChooserFrame.setResizable(false);

        //sets some parameters of the main panel inside the color choosing panel
        colorChoosingPanel.setLayout(new BorderLayout());
        colorChoosingPanel.add(colorChooser, BorderLayout.CENTER);

        //the two buttons for setting the player's colors
        //are added to their own panel
        buttonPanel.add(pieceOne);
        buttonPanel.add(pieceTwo);
        buttonPanel.add(defaultColors);

        //line border around the buttonPanel
        buttonPanel.setBorder(new LineBorder(Color.BLACK, 4));

        //adds the buttons
        colorChoosingPanel.add(buttonPanel, BorderLayout.SOUTH);
        //adds the final pane
        colorChooserFrame.getContentPane().add(colorChoosingPanel, BorderLayout.CENTER);

        creditsPanel.setLayout(new FlowLayout());


        //tries to create a JEditorPane using the aboutFrame HTML file
        try {
            //sets some of the needed parameters of the pane
            htmlAboutText.setContentType("text/html");
            //load the html file
            htmlAboutText.setPage(new File("connectfour/help.html").toURI().toURL());
            htmlAboutText.setEditable(false);

            aboutFrame.setIconImage(icon.getImage());
            htmlAboutText.setSelectionColor(Color.ORANGE);

            //set some of the needed parameters
            htmlCreditText.setContentType("text/html");
            //load the file
            htmlCreditText.setPage(new File("connectfour/credits.html").toURI().toURL());
            htmlCreditText.setEditable(false);
            creditsFrame.setIconImage(icon.getImage());
            htmlCreditText.setSelectionColor(Color.ORANGE);

            //add the icon
            creditsIcon.setIcon(icon);
            creditsIcon.setHorizontalAlignment(JLabel.CENTER);
            //add both the icon and the text
            creditsPanel.setLayout(new BorderLayout());
            creditsPanel.add(creditsIcon, BorderLayout.CENTER);
            creditsPanel.add(htmlCreditText, BorderLayout.SOUTH);
            creditsFrame.setSize(new Dimension(400, 400));
            //add the panel to frame
            creditsFrame.getContentPane().add(creditsPanel);


            //border layout for both the panel and the frame
            aboutFrame.getContentPane().setLayout(new BorderLayout());
            aboutPanel.setLayout(new BorderLayout());

            //adds the Editor pane to the panel
            aboutPanel.add(htmlAboutText, BorderLayout.CENTER);
            aboutPanel.setBorder(new LineBorder(Color.BLACK));

            //sets the sizes
            aboutFrame.setSize(new Dimension(500, 575));
            aboutPanel.setSize(new Dimension(500, 575));

            //adds the main panel
            aboutFrame.getContentPane().add(aboutPanel, BorderLayout.CENTER);

            //add to the controlsMenu bar
            controlsMenu.add(aboutMenuItem);
        } catch (IOException e) {
            System.err.println("Error loading HTML Files.");
        }

        //The accelerators for each of the controlsMenu items
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
        resetMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        colorChooserMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));

        //adds all the items to the options
        optionsMenu.add(colorChooserMenuItem);
        optionsMenu.add(disableHoversMenuItem);
        optionsMenu.add(disableColumnHighlightsItem);

        //adds all the items to the help menu
        helpMenu.add(aboutMenuItem);
        helpMenu.add(creditsMenuItem);

        //adds all the items to the controls
        controlsMenu.add(saveMenuItem);
        controlsMenu.add(loadMenuItem);
        controlsMenu.add(resetMenuItem);

        //sets some parameters of the menu bar
        bar.setBorder(new LineBorder(Color.BLACK));
        bar.setPreferredSize(new Dimension(420, 20));
        bar.add(controlsMenu);
        bar.add(helpMenu);
        bar.add(optionsMenu);

        //create the top display
        topDisplay.setLayout(new BorderLayout());
        //add the menu bar
        topDisplay.add(bar, BorderLayout.WEST);
        activePlayer.setBorder(bar.getBorder());
        activePlayer.setHorizontalAlignment(JLabel.CENTER);
        //add the active player display
        topDisplay.add(activePlayer, BorderLayout.CENTER);
        activePlayerColor.setBorder(new LineBorder(Color.BLACK, 3));
        topDisplay.add(activePlayerColor, BorderLayout.EAST);

        //add it to the Frame
        main.getContentPane().add(topDisplay, BorderLayout.NORTH);
    }

    /*
    All the needed swing components
    - Some can be converted to local variables within the above method...
    but they are initialized here so that the documentation will document them...
     */

    /**
     * The frame for the 'about' window.
     */
    private JFrame aboutFrame = new JFrame("Connect Four - How To Play");

    /**
     * The pane which displays the html about file.
     */
    private JEditorPane htmlAboutText = new JEditorPane();

    /**
     * The pane which displays the html credits file.
     */
    private JEditorPane htmlCreditText = new JEditorPane();

    /**
     * The Panel which hosts information about the game (the 'about'.html).
     * Added to the aboutFrame.
     */
    private JPanel aboutPanel = new JPanel();

    /**
     * The main menu bar.
     */
    private JMenuBar bar = new JMenuBar();

    /**
     * The panel which houses the buttons on the color choosing window, which allow for
     * the user to change the colours.
     */
    private JPanel buttonPanel = new JPanel();

    /**
     * The button for changing the first player's pieces.
     */
    private JButton pieceOne = new JButton("Set Color for Player 1");

    /**
     * The button for changing the second player's pieces.
     */
    private JButton pieceTwo = new JButton("Set Color for Player 2");

    /**
     * The button for restoring the default player colors.
     */
    private JButton defaultColors = new JButton("Restore Default Colors");

    /**
     * The color choosing component, which allows the user to select a color.
     */
    private JColorChooser colorChooser = new JColorChooser();

    /**
     * The frame for the color changing option.
     */
    private JFrame colorChooserFrame = new JFrame("Color Chooser");

    /**
     * The frame which holds the credits.
     */
    private JFrame creditsFrame = new JFrame("Connect Four - About");

    /**
     * The panel which houses the credits.
     */
    private JPanel creditsPanel = new JPanel();

    /**
     * The panel which houses the buttons and the color chooser component.
     */
    private JPanel colorChoosingPanel = new JPanel();

    /**
     * The main controlsMenu component which houses all the game options.
     */
    private JMenu controlsMenu = new JMenu("Controls");

    /**
     * The Menu for the help options (about, creditsIcon).
     */
    private JMenu helpMenu = new JMenu("Help");

    /**
     * The Menu for the options (changing colours, disabling hovers)
     */
    private JMenu optionsMenu = new JMenu("Options");

    /**
     * The menu item for changing the colors
     */
    private JMenuItem colorChooserMenuItem = new JMenuItem("Change Colors", KeyEvent.VK_C);

    /**
     * The menu item for enabling/disabling the column highlights.
     */
    private JMenuItem disableColumnHighlightsItem = new JMenuItem("Enable Column Highlights");

    /**
     * the menu item for information on the game.
     */
    private JMenuItem creditsMenuItem = new JMenuItem("About");

    /**
     * Disables the hovering hints.
     */
    private JMenuItem disableHoversMenuItem = new JMenuItem("Disable Hovering Highlight");

    /**
     * The controlsMenu item for resetting the game to default state.
     */
    private JMenuItem resetMenuItem = new JMenuItem("Reset", KeyEvent.VK_R);

    /**
     * The controlsMenu item for saving the game (calls the serialization methods).
     */
    private JMenuItem saveMenuItem = new JMenuItem("Save", KeyEvent.VK_S);

    /**
     * The controlsMenu item for loading any saved games (calls the serialization methods).
     */
    private JMenuItem loadMenuItem = new JMenuItem("Load", KeyEvent.VK_L);

    /**
     * The menu item for the instructions window.
     */
    private JMenuItem aboutMenuItem = new JMenuItem("How To Play");

    /**
     * JLabel for holding the icon displayed in the credits window.
     */
    private JLabel creditsIcon = new JLabel();

    /**
     * The label showing the current player
     */
    static JLabel activePlayer = new JLabel("PLAYER 1's TURN:");

    /**
     * The label displaying the current player's color.
     */
    static JLabel activePlayerColor = new JLabel();

    /**
     * The panel which houses both the menu bar and the current player display.
     */
    private JPanel topDisplay = new JPanel();

    /**
     * The program's logo/icon.
     */
    private ImageIcon icon = new ImageIcon("connectfour/icon.png");

    /**
     * The main JFrame which houses the main program.
     */
    private JFrame main = new JFrame("Connect 4 - Elliot Gorman 2019");
}
