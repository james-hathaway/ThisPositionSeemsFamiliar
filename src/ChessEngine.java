import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ChessEngine implements AutoCloseable {
    private Process engineProcess;
    private BufferedReader processReader;
    private PrintWriter processWriter;

    public ChessEngine(String pathToEngine) throws IOException {
        engineProcess = new ProcessBuilder(pathToEngine).start();
        processReader = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
        processWriter = new PrintWriter(new OutputStreamWriter(engineProcess.getOutputStream()), true);
    }

    public void sendCommand(String command) {
        processWriter.println(command);
        processWriter.flush();
    }

    public String getOutput() throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = processReader.readLine()) != null) {
            if("uciok".equals(line) || line.startsWith("bestmove"))
                break;
            builder.append(line).append("\n");
        }
        return builder.toString();
    }

    @Override
    public void close() throws IOException {
        sendCommand("quit");
        if (engineProcess.isAlive()) {
            engineProcess.destroy();
        }
        processReader.close();
        processWriter.close();
    }
}
