package dataAccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;


public class MemoryGameDAO implements GameDAO{

    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void deletaAllGames() {
        games.clear();
    }
}
