package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;


public class MemoryGameDAO implements GameDAO{
    private int nextId = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void deleteAllGames() {
        games.clear();
    }

    @Override
    public int createGame(String gameName) {
        int gameID = nextId++;
        GameData game = new GameData(gameID,null,null,gameName,new ChessGame());
        games.put(gameID,game);
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public void updateGame(int gameID, String fieldToUpdate, String newValue) {
        GameData originalGame = games.get(gameID);
        GameData updatedGame = switch (fieldToUpdate) {
            case "whiteUsername" ->
                    new GameData(originalGame.gameID(), newValue, originalGame.blackUsername(), originalGame.gameName(), originalGame.game());
            case "blackUsername" ->
                    new GameData(originalGame.gameID(), originalGame.whiteUsername(), newValue, originalGame.gameName(), originalGame.game());
            default -> null;
        };
        games.put(gameID, updatedGame);
    }

    @Override
    public Collection<GameData> listGames() {
        return games.values();
    }
}
