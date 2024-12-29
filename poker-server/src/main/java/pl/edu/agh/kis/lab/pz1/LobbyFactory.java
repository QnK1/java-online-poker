package pl.edu.agh.kis.lab.pz1;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyFactory {

    private final Map<String, Constructor<? extends Lobby>> lobbies;

    public LobbyFactory() {
        lobbies = new HashMap<>();

        Reflections reflections = new Reflections("pl.edu.agh.kis.lab.pz1");
        var lobbySet = reflections.getSubTypesOf(Lobby.class);

        for(var lobby : lobbySet) {
            try{
                Constructor<? extends Lobby> constructor = lobby.getDeclaredConstructor();
                Lobby instance = constructor.newInstance();

                lobbies.put(instance.getGameName(), constructor);
            } catch (Exception e) {
                throw new FailedToLoadChildClassesException("Loading child classes of Lobby failed", e);
            }

        }
    }

    public Lobby getLobby(String gameName) {
        var lobby = lobbies.get(gameName);

        if(lobby == null) {
            return null;
        }

        try {
            return lobby.newInstance();
        } catch (Exception e) {
            throw new FailedToLoadChildClassesException("Loading child classes of Lobby failed", e);
        }
    }

    public List<String> getGameNames(){
        return lobbies.keySet().stream().toList();
    }

}
