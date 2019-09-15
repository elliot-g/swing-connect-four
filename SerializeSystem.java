package connectfour;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The "save" system of the game
 * serializes and un-serializes values.
 *
 * @author elliot
 */
class SerializeSystem {
    /**
     * Hides the default public constructor.
     */
    private SerializeSystem() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Serializes the given object to the given path.
     *
     * @param toSave the object to save
     * @param path   the filepath to save to
     */
    static synchronized void serialize(Object toSave, String path) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            //creates necessary output streams
            fileOutputStream = new FileOutputStream(path); //to file
            objectOutputStream = new ObjectOutputStream(fileOutputStream); //object

            //writes object
            objectOutputStream.writeObject(toSave);
        } catch (FileNotFoundException nfe) {
            System.err.println("Necessary file not found.");
            java.awt.Toolkit.getDefaultToolkit().beep();
        } catch (IOException ioe) {
            System.err.println("Unexpected I/O Error. Keeping previous save.");
        } finally {
            //closes the streams in the finally
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                System.err.println("Unexpected I/O Error. Keeping previous save.");
            }
        }
    }

    /**
     * Loads the game from the given path.
     *
     * @param state signifies the purpose of calling this method
     *              (0 for colors, 1 for the game)
     * @param path  the filepath
     * @return the loaded object
     */
    static synchronized Object load(String path, int state) {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            //creates necessary input streams
            fileInputStream = new FileInputStream(path); //from file
            objectInputStream = new ObjectInputStream(fileInputStream); //object stream

            //reads object
            return objectInputStream.readObject();
        } catch (FileNotFoundException nfe) {
            System.err.println("Necessary file not found. Keeping defaults...");
            java.awt.Toolkit.getDefaultToolkit().beep();
        } catch (EOFException eof) {
            //silent
        } catch (IOException ioe) {
            System.err.println("Unexpected I/O Error. Keeping defaults...");
            java.awt.Toolkit.getDefaultToolkit().beep();
        } catch (ClassNotFoundException e) {
            System.err.println("Couldn't properly parse necessary file. Keeping defaults...");
        } finally {
            //closes the streams in the finally
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                System.err.println("Unexpected I/O Error. Keeping defaults.");
            }
        }

        //if loading failed, return defaults...
        return state == 1 ? CirclePanel.DEFAULT_PLAYER_ONE_COLOR :
                state == 2 ? CirclePanel.DEFAULT_PLAYER_TWO_COLOR : null;
    }
}