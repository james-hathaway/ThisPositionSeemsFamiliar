import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

public class LoadInUser extends Main {
        private String user;
    
        public LoadInUser(String user) {
            this.user = user;
        }
    
        public void loadUserGames() {
            String url = "https://api.chess.com/pub/player/" + user + "/games/archives";
    
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    
                // optional default is GET
                con.setRequestMethod("GET");
    
                int responseCode = con.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
    
                while((inputLine=in.readLine())!=null) {
                    response.append(inputLine);
                }
                in.close();
    
                // print result
                System.out.println(response.toString());
    
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
