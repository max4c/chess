package dataAccess;

import dataAccess.Exception.DataAccessException;
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
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) {
        var statement = "INSERT INTO user (name, type, json) VALUES (?, ?, ?)";
    }


}
