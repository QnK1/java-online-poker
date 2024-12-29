package pl.edu.agh.kis.lab.pz1;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class LobbyFactoryTest {

    @Test
    public void lobbyNamesTest() {
        LobbyFactory factory = new LobbyFactory();
        assertFalse(factory.getGameNames().isEmpty());
    }
}
