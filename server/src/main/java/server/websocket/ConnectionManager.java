package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Map<String, Session>> connections = new ConcurrentHashMap<>();

    public void add(String authToken, Session session, int gameID) {
        Map<String, Session> sessionMap = connections.computeIfAbsent(gameID, k -> new HashMap<>());
        sessionMap.put(authToken,session);
    }

    public void remove(int gameID,String authToken) {
        Map<String, Session> map = connections.get(gameID);
        map.remove(authToken);
    }

    public Set<Map.Entry<Integer, Map<String, Session>>> getEntrySet() {
        return connections.entrySet();
    }
}
