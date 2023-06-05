import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import org.json.*;

public class LoadInUser extends Main {
    private String user;
    private List<String> loadedGames = new ArrayList<>();

    public LoadInUser(String user) {
        this.user = user;
    }

    public void loadUserGames() {
        String url = "https://api.chess.com/pub/player/" + user + "/games/archives";

        try {
            String response = getHttpResponse(url);

            // Get the last month's games URL using regular expression
            Pattern pattern = Pattern.compile("https://api\\.chess\\.com/pub/player/" + user + "/games/(\\d{4}/\\d{2})");
            Matcher matcher = pattern.matcher(response);
            String lastMonthUrl = null;
            while (matcher.find()) {
                lastMonthUrl = matcher.group();
            }

            // Fetch the games from the last month
            if (lastMonthUrl != null) {
                String gameResponse = getHttpResponse(lastMonthUrl);

                // parse the JSON response
                JSONObject jsonObject = new JSONObject(gameResponse);
                JSONArray games = jsonObject.getJSONArray("games");

                // convert each game to PGN format and store it in the list
                for (int i = 0; i < games.length(); i++) {
                    JSONObject game = games.getJSONObject(i);
                    loadedGames.add(game.getString("pgn"));
                }
            } else {
                System.out.println("No games found for the last month.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getLoadedGames() {
        return loadedGames;
    }

    private String getHttpResponse(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) { 
            String inputLine;

            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response.toString();
    }
}
