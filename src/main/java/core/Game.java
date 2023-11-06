package core;

import exception.EmptyPitSelectedException;
import exception.InvalidPitNumberException;
import model.LargePit;
import model.Pit;
import model.Player;
import model.RegularPit;

/**
 * The `Game` class manages the core logic of the Mancala game. It facilitates player turns,
 * seed sowing, and win conditions. This class interacts with the game board (`Board`) and
 * handles user interactions to drive the gameplay.
 */
public class Game {
    private final Board board; // Represents the game board where the Mancala game is played.
    private Player activePlayer; // Represents the currently active player taking their turn.
    private final ConsoleInputReader inputReader; // Reads user input to facilitate player interactions.

    /**
     * Private constructor to restrict external instantiation. Instances of the `Game` class are created
     * using the `newGame()` method.
     *
     * @param board        The game board where the Mancala game is played.
     */
    private Game(Board board, ConsoleInputReader inputReader){
        this.board = board;
        this.activePlayer = board.getFirstPlayer();
        this.inputReader = inputReader;
    }
    /**
     * Creates a new Mancala game by taking player names as input from the user and initializing
     * the game board. It then starts the game and manages the gameplay.
     */
    public static Game create(ConsoleInputReader inputReader){
        System.out.println("Please enter the first player's name...");
        String firstPlayerName = inputReader.readLine(ConsoleInputReader.InputType.FIRST_PLAYER_NAME);

        System.out.println("Please enter the second player's name...");
        String secondPlayerName;
        while ((secondPlayerName = inputReader.readLine(ConsoleInputReader.InputType.SECOND_PLAYER_NAME))
                .equals(firstPlayerName)){
            System.out.println("Please enter a different name than the first player...");
        }

        // Prepare board
        Board board = Board.create(firstPlayerName, secondPlayerName);
        return new Game(board, inputReader);
    }

    /**
     * Starts the Mancala game by initiating the first player's turn and prompting them to play.
     */
    public GameResult start(){
        board.prettyPrint();
        return move();
    }

    /**
     * Initiates a player's turn, allowing them to select a pit and sowing seeds.
     */
    private GameResult move(){
        // Check if game is over
        if(board.getFirstPlayer().noSeedsLeft() || board.getSecondPlayer().noSeedsLeft()){
            return finalizeGame();
        }

        // Allow active player to take turn
        Pit endPit = activePlayer.takeTurn(askActivePlayerToPlay());
        board.prettyPrint();

        // If the last seed lands in an empty pit owned by the player, and the opposite pit contains seeds,
        // both the last seed and the opposite seeds are captured and placed into the player’s large pit.
        if(endPit instanceof RegularPit endRegularPit){
            if(activePlayer.canCapture(endRegularPit)){
                activePlayer.capture(endRegularPit);
            }
        }

        activePlayer = nextPlayer(endPit);
        return move();
    }
    /**
     * Asks the active player to select a pit for their turn and validates the input.
     *
     * @return The selected regular pit for the active player's turn.
     */
    public RegularPit askActivePlayerToPlay(){
        RegularPit selectedPit;
        System.out.printf("%s, it is your turn. Please enter a pit number to start your move...\n",
                activePlayer.getName());

        while (true){
            String selectedPitStr = inputReader.readLine(ConsoleInputReader.InputType.PIT_NUMBER);
            try {
                selectedPit = validateMove(selectedPitStr);
                break;
            }
            catch (InvalidPitNumberException |
                   EmptyPitSelectedException exception){
                System.out.println(exception.getMessage());
            }
        }

        return selectedPit;
    }

    /**
     * Retrieves the game board associated with this Mancala game.
     *
     * @return The game board, which represents the current state of the Mancala game.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Retrieves the currently active player who is taking their turn.
     *
     * @return The active player, representing the player who is currently making moves in the game.
     */
    public Player getActivePlayer() {
        return activePlayer;
    }

    /**
     * Validates the pit number selected by the player to ensure it's a valid move.
     *
     * @param pitNumberStr  The pit number selected by the player as a string.
     * @return The selected regular pit for the player's turn.
     * @throws InvalidPitNumberException     If the selected pit number is invalid.
     * @throws EmptyPitSelectedException     If the selected pit is empty.
     */
    protected RegularPit validateMove(String pitNumberStr) throws
            InvalidPitNumberException,
            EmptyPitSelectedException {
        // Validate that the user entered a valid number
        if(!pitNumberStr.matches("^\\d+$")){
            throw new InvalidPitNumberException();
        }

        // Validate that the selected pit number is within the player's pits
        int pitNumber = Integer.parseInt(pitNumberStr);
        if (pitNumber < 1 || pitNumber > activePlayer.getRegularPits().size()) {
            throw new InvalidPitNumberException();
        }

        // Validate that the selected pit is not empty
        RegularPit selectedPit = activePlayer.getRegularPits().get(pitNumber - 1);
        if (selectedPit.isEmpty()) {
            throw new EmptyPitSelectedException();
        }

        return selectedPit;
    }
    /**
     * Ends the game by determining the winner or declaring a draw.
     */
    private GameResult finalizeGame(){
        int firstPlayerScore = board.getFirstPlayer().getLargePit().getSeeds();
        int secondPlayerScore = board.getSecondPlayer().getLargePit().getSeeds();
        GameResult gameResult;

        if(firstPlayerScore == secondPlayerScore){
            System.out.println("Game over, it is a draw!");
            gameResult = GameResult.DRAW;
        }
        else if(firstPlayerScore > secondPlayerScore){
            System.out.printf("Game over, %s won!\n", board.getFirstPlayer().getName());
            gameResult = GameResult.FIRST_PLAYER_WON;
        }
        else{
            System.out.printf("Game over, %s won!\n", board.getSecondPlayer().getName());
            gameResult = GameResult.SECOND_PLAYER_WON;
        }

        inputReader.close();
        return gameResult;
    }
    /**
     * Determines the next active player based on the pit where the last seed lands.
     *
     * @param endPit    The pit where the last seed lands.
     * @return The next active player.
     */
    protected Player nextPlayer(Pit endPit){
        // If the last seed lands in the player’s large pit, the player gets an additional move.
        if(endPit instanceof LargePit && endPit.getOwner().equals(activePlayer)){
            return activePlayer;
        }

        return getOpponent(activePlayer);
    }
    /**
     * Retrieves the opponent player of the given player.
     *
     * @param player    The player for whom the opponent needs to be determined.
     * @return The opponent player.
     */
    protected Player getOpponent(Player player){
        if(player.equals(board.getFirstPlayer())){
            return board.getSecondPlayer();
        }

        return board.getFirstPlayer();
    }
    /**
     * Main method to start a new Mancala game.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args){
        Game.create(new ConsoleInputReader()).start();
    }
}
