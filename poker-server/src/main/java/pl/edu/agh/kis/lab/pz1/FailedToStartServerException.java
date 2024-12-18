package pl.edu.agh.kis.lab.pz1;

public class FailedToStartServerException extends RuntimeException {
    public FailedToStartServerException(String message, Exception cause) {
        super(message, cause);
    }
}
