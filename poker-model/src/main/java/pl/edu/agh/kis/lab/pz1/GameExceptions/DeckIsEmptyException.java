package pl.edu.agh.kis.lab.pz1.GameExceptions;

public class DeckIsEmptyException extends RuntimeException {
    public DeckIsEmptyException(String message) {
        super(message);
    }
}
