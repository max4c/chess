package ui;
import java.util.Scanner;
import static ui.EscapeSequences.*;


public class Repl {
    private final PreloginClient preloginClient;

    public Repl(String serverUrl) {
        preloginClient = new PreloginClient(serverUrl);
    }

    public void run() {
        System.out.println("\uD83D\uDC51 Welcome to the 240 chess. Type help to get started. \uD83D\uDC51");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = preloginClient.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_WHITE + ">>> " + SET_TEXT_COLOR_GREEN );
    }

}
