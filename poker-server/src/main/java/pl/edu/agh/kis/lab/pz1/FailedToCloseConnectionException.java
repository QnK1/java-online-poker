package pl.edu.agh.kis.lab.pz1;

public class FailedToCloseConnectionException extends RuntimeException {
    public FailedToCloseConnectionException(String message, Exception e) {
        super(message, e);
    }
}
