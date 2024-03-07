package dataAccess;

import dataAccess.Exception.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void deleteAllGames() throws DataAccessException;
    int createGame(String gameName)throws DataAccessException;
    GameData getGame(int gameID);

    void updateGame(int gameID, String fieldToUpdate, String newValue);

    Collection<GameData> listGames();

}
