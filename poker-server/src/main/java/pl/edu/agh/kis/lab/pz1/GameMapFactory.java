package pl.edu.agh.kis.lab.pz1;

import org.reflections.Reflections;
import pl.edu.agh.kis.lab.pz1.game_logic.Game;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameMapFactory {
    public static Set<Class<? extends Game>> getGameSet(){
        Reflections reflections = new Reflections("pl.edu.agh.kis.lab.pz1.game_logic.texas");
        return reflections.getSubTypesOf(Game.class);
    }

    public static Map<String, Game> getGameMap(){
        Set<Class<? extends Game>> gameSet = getGameSet();
        Map<String, Game> gameMap = new HashMap<>();

        for(Class<? extends Game> gameClass : gameSet){
            try{
                Constructor<? extends Game> constructor = gameClass.getDeclaredConstructor();
                Game instance = constructor.newInstance();

                gameMap.put(instance.getName(), instance);
            } catch (Exception e){
                throw new FailedToLoadChildClassesException("Loading child classes of Game failed", e);
            }
        }

        return gameMap;
    }
}
