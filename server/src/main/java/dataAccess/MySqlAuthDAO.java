package dataAccess;

import com.google.gson.Gson;
import dataAccess.Exception.DataAccessException;
import model.AuthData;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlAuthDAO implements AuthDAO{

    public MySqlAuthDAO() {
        try{
            String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            };
            DatabaseManager.configureDatabase(createStatements);
        } catch (DataAccessException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void deleteAllAuths(){

    }

    @Override
    public String createAuth(String username) throws DataAccessException{
        var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        String authToken =  UUID.randomUUID().toString();
        try {
            DatabaseManager.executeUpdate(statement, username, authToken);
        }catch (DataAccessException e){
            throw new DataAccessException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
        return authToken;
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException{
        var statement = "SELECT authToken, json FROM auth WHERE authToken=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString("authToken"),rs.getString("username"));
                    }
                }
            }
        }catch (Exception e){
            throw new DataAccessException(500, String.format("unable to update database: %s, %s",statement, e.getMessage()));
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken){
        // delete from table with no where condition
    }



}
