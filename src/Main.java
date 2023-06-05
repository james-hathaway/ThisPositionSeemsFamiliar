import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Get the user's username
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your chess.com username: ");
        String user = scanner.nextLine();
        scanner.close();

        // Load in the user's games
        LoadInUser loadInUser = new LoadInUser(user);
        loadInUser.loadUserGames();

        // Get the list of user games in PGN format
        List<String> userGames = loadInUser.getLoadedGames();

        // Start the chess engine
        try (ChessEngine engine = new ChessEngine("/path/to/stockfish")) { // provide the path to your stockfish binary
            engine.sendCommand("uci");

            // Iterate over each game
            for(String game : userGames) {
                // Send the game to the engine
                engine.sendCommand("position " + game);
                engine.sendCommand("go depth 20");
                
                // Get the engine output
                String output = engine.getOutput();

                // Logic for checking if there was a significant swing in evaluation
                // If there was, create a chess puzzle from the game.
                // TODO: implement this logic
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
