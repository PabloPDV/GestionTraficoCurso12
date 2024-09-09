package es.cic.curso.back.exception;

public class ViaNotFoundException extends RuntimeException {

    public ViaNotFoundException(String message) {
        super(message);
    }

    public ViaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
