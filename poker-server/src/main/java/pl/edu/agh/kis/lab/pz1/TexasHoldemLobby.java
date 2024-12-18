package pl.edu.agh.kis.lab.pz1;

import lombok.Getter;
import pl.edu.agh.kis.lab.pz1.game_logic.texas.THPlayer;
import pl.edu.agh.kis.lab.pz1.game_logic.texas.TexasHoldemGame;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TexasHoldemLobby extends Lobby {
    final List<THPlayer> players;
    TexasHoldemGame game;
    private boolean started;

    public TexasHoldemLobby() {
        players = new ArrayList<>();
        started = false;
    }

    @Override
    public void addPlayer(String name){
        THPlayer player = new THPlayer();
        player.setName(name);
        players.add(player);
    }

    @Override
    public void createGame(){
        game = new TexasHoldemGame(players, 100, 5);
        game.startGame();
        started = true;
    }
}
