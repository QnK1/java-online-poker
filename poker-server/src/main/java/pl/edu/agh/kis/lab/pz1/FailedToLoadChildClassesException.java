package pl.edu.agh.kis.lab.pz1;

public class FailedToLoadChildClassesException extends RuntimeException {
    public FailedToLoadChildClassesException(String message, Exception e) {
        super(message, e);
    }
}
