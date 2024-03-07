package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.Exception.DataAccessException;
import model.GameData;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class MySqlGameDAO implements GameDAO{

    public MySqlGameDAO() {
        try{
            String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `ChessGame` TEXT NOT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(`gameName`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            };
            DatabaseManager.configureDatabase(createStatements);
        } catch (DataAccessException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void deleteAllGames() throws DataAccessException {
        var statement = "TRUNCATE game";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public int createGame(String gameName) throws DataAccessException{
        var statement = "INSERT INTO game (gameName,ChessGame) VALUES (?, ?)";
        ChessGame game = new ChessGame();
        game.getBoard().resetBoard();
        var json = new Gson().toJson(game);
        try {
            return DatabaseManager.executeUpdate(statement, gameName,json);
        }catch (DataAccessException e){
            throw new DataAccessException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT gameID,whiteUserName,blackUserName,gameName,ChessGame FROM game WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        }catch (Exception e){
            throw new DataAccessException(500, String.format("unable to update database: %s, %s",statement, e.getMessage()));
        }
        return null;
    }

    @Override
    public void updateGame(int gameID, String fieldToUpdate, String newValue) throws DataAccessException {
        String columnToUpdate = null;

        if ("whiteUsername".equals(fieldToUpdate)) {
            columnToUpdate = "whiteUsername";
        } else if ("blackUsername".equals(fieldToUpdate)) {
            columnToUpdate = "blackUsername";
        }

        // If the input matches an acceptable value, go ahead with the update:
        if (columnToUpdate != null) {
            String statement = "UPDATE game SET " + columnToUpdate + " = ? WHERE gameID = ?";
            DatabaseManager.executeUpdate(statement, newValue, gameID);
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID,whiteUserName,blackUserName,gameName,ChessGame FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        int gameID = rs.getInt("gameID");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        Gson gson = new Gson();
        ChessGame game = gson.fromJson(rs.getString("ChessGame"),ChessGame.class);
        return new GameData(gameID,whiteUsername,blackUsername,gameName,game);
    }

}
