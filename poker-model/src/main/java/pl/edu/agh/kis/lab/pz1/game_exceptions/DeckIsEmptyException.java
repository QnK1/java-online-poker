package pl.edu.agh.kis.lab.pz1.game_exceptions;

public class DeckIsEmptyException extends RuntimeException {
    public DeckIsEmptyException(String message) {
        super(message);
    }
}
