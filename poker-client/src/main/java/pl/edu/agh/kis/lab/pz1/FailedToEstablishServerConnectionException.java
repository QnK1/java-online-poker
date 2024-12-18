package pl.edu.agh.kis.lab.pz1;

public class FailedToEstablishServerConnectionException extends RuntimeException {
    public FailedToEstablishServerConnectionException(String message, Exception cause) {
        super(message, cause);
    }
}
