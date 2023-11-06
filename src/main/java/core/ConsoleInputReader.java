package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The `ConsoleInputReader` class provides methods for reading input from the console during the Mancala game.
 * It includes methods to read different types of input, such as player names and pit numbers.
 */
public class ConsoleInputReader {
    /**
     * An enumeration representing the types of input that can be read using this input reader.
     */
    public enum InputType{
        FIRST_PLAYER_NAME("first player name"),
        SECOND_PLAYER_NAME("second player name"),
        PIT_NUMBER("pit number");

        final String name;

        InputType(String name){
            this.name = name;
        }
    }
    /**
     * A `BufferedReader` instance for reading input from the console.
     */
    protected BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    /**
     * Reads a line of input from the console for a specific input type.
     *
     * @param inputType The type of input being read (e.g., player name or pit number).
     * @return The input as a string provided by the user.
     */
    public String readLine(InputType inputType){
        try{
            return reader.readLine();
        }
        catch (IOException exception){
            System.out.printf("Something went wrong while receiving %s. Please try again...", inputType.name);
            return readLine(inputType);
        }
    }
    /**
     * Closes the input reader, releasing any associated resources.
     */
    public void close(){
        try {
            reader.close();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
