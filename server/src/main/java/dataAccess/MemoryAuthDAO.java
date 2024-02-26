package dataAccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{

    final private HashMap<Integer, AuthData> auths = new HashMap<>();
    @Override
    public void deleteAllAuths() {
        auths.clear();
    }
}
