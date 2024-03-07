package dataAccess;

import com.google.gson.Gson;
import dataAccess.Exception.DataAccessException;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;

public class MySqlUserDAO implements UserDAO{
    public MySqlUserDAO() {
        try{
            String[] createStatements = {
                    """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            };
            DatabaseManager.configureDatabase(createStatements);
        } catch (DataAccessException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void deleteAllUsers() {
        //Database.deleteTable()
    }

    @Override
    public UserData getUser(String username) throws DataAccessException{
        var statement = "SELECT username, password, email FROM user WHERE username=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(rs.getString("username"),rs.getString("password"),rs.getString("email"));
                    }
                }
            }
        }catch (Exception e){
            throw new DataAccessException(500, String.format("unable to update database: %s, %s",statement, e.getMessage()));
        }
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try {
            DatabaseManager.executeUpdate(statement, username, hashedPassword, email);
        }catch (DataAccessException e){
            throw new DataAccessException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }

    }


}
