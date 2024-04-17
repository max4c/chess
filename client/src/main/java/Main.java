import ui.Repl;
import ui.ResponseException;

public class Main {
    public static void main(String[] args) throws ResponseException {
        var serverUrl = "http://localhost:8081";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new Repl(serverUrl).run();
    }
}