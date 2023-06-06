import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;

public class GameAnalysis {
    private String game;
    private ChessEngine engine;
    private List<String> moveEvaluations;
    private List<String> puzzlePositions;
    private List<String> puzzlePGNs;
    private double prevEval;

    public GameAnalysis(String game, ChessEngine engine) {
        this.game = game;
        this.engine = engine;
        this.moveEvaluations = new ArrayList<>();
        this.puzzlePositions = new ArrayList<>();
        this.puzzlePGNs = new ArrayList<>();
        this.prevEval = 0.0;
    }

    public void analyzeGame() throws IOException {
        // Split the game into individual moves
        String[] moves = game.split("\\s+");

        StringBuilder position = new StringBuilder();
        for (int i = 0; i < moves.length; i++) {
            // Update the position
            position.append(moves[i]).append(" ");

            // Send the position to the engine
            engine.sendCommand("position startpos moves " + position);
            engine.sendCommand("go depth 20");

            // Get the engine output
            String output = engine.getOutput();

            // Extract the evaluation from the engine output
            String evaluation = extractEvaluation(output);

            // Store the evaluation
            moveEvaluations.add(evaluation);

            // Check if the change in evaluation is larger than 2.5
            double currEval = Double.parseDouble(evaluation);
            if (Math.abs(currEval - prevEval) > 2.5) {
                // Store the current position as a potential puzzle
                puzzlePositions.add(position.toString());

                // Create a PGN of all the moves up to this point and add it to the list
                puzzlePGNs.add(createPGNFromMoves(position.toString()));
            }

            prevEval = currEval;
        }

    }

    private String createPGNFromMoves(String moves) {
        // Split the moves into an array
        String[] movesArray = moves.trim().split("\\s+");
        
        StringBuilder pgn = new StringBuilder();
    
        // Iterate over the moves
        for (int i = 0; i < movesArray.length; i++) {
            // Every two moves, add the move number
            if (i % 2 == 0) {
                pgn.append((i/2 + 1)).append(".");
            }
            
            // Append the move
            pgn.append(movesArray[i]).append(" ");
        }
        
        return pgn.toString();
    }
    

    private String extractEvaluation(String output) {
        Pattern pattern = Pattern.compile("info score cp (-?\\d+)");
        Matcher matcher = pattern.matcher(output);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "N/A"; // or you could return some other value indicating the absence of a score
    }

    public List<String> getMoveEvaluations() {
        return moveEvaluations;
    }

    public List<String> getPuzzlePositions() {
        return puzzlePositions;
    }

    public List<String> getPuzzlePGNs() {
        return puzzlePGNs;
    }

}
