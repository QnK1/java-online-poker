package pl.edu.agh.kis.lab.pz1.game_exceptions;

public class InvalidNumberOfPlayersException extends RuntimeException {
    public InvalidNumberOfPlayersException(String message) {
        super(message);
    }
}
