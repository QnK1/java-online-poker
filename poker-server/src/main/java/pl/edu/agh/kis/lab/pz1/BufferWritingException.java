package pl.edu.agh.kis.lab.pz1;

public class BufferWritingException extends RuntimeException {
    public BufferWritingException(String message, Exception e) {
        super(message, e);
    }
}
