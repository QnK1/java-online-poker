package pl.edu.agh.kis.lab.pz1.game_exceptions;

public class TooFewHiddenCardsException extends RuntimeException {
    public TooFewHiddenCardsException(String message) {
        super(message);
    }
}
