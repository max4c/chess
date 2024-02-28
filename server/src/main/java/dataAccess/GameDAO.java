package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void deleteAllGames();
    int createGame(String gameName);
    GameData getGame(int gameID);

    void updateGame(int gameID, String fieldToUpdate, String newValue);

    Collection<GameData> listGames();

}
