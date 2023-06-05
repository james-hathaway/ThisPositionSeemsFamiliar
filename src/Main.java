import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your chess.com username: ");
        String user = scanner.nextLine();
        LoadInUser loadInUser = new LoadInUser(user);
        loadInUser.loadUserGames();
        scanner.close();
    }
}





