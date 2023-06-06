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
                GameAnalysis analysis = new GameAnalysis(game, engine);
                analysis.analyzeGame();

                // Retrieve the analysis data
                List<String> moveEvaluations = analysis.getMoveEvaluations();
                List<String> puzzlePositions = analysis.getPuzzlePositions();
                List<String> puzzlePGNs = analysis.getPuzzlePGNs();

                // Logic for creating puzzles based on these positions.
                // TODO: implement this logic
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
