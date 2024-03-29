package ui;
import server.ServerFacade;

import java.util.Scanner;
import static ui.EscapeSequences.*;


public class Repl {

    private final ServerFacade server;
    private final PreloginUI preloginUI;
    private final PostloginUI postloginUI;
    private State state = State.LOGGED_OUT;

    public Repl(String serverUrl) {
        server = new ServerFacade(serverUrl);
        preloginUI = new PreloginUI(server);
        postloginUI = new PostloginUI(server);
    }

    public void run() {
        System.out.println(SET_TEXT_COLOR_WHITE + "\uD83D\uDC51 Welcome to the 240 chess. Type help to get started. \uD83D\uDC51");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                if(state == State.LOGGED_OUT){
                    result = preloginUI.eval(line);
                    if(result.contains("logged in ")){
                        state = State.LOGGED_IN;
                    }
                }
                else{
                    result = postloginUI.eval(line);
                }
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        String state_text = "";
        if (state == State.LOGGED_OUT){
            state_text = "[LOGGED_OUT]";
        }
        else{
            state_text = "[LOGGED_IN]";
        }

        System.out.print("\n" + SET_TEXT_COLOR_WHITE + state_text + " >>> " + SET_TEXT_COLOR_GREEN );
    }

}
