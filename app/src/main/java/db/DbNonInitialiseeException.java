package db;

/**
 * Exception lancée par le gestionnaire de base de donnée si il est utilisé sans avoir été initialisé
 * Created by Maxime on 4/30/2016.
 */
public class DbNonInitialiseeException extends RuntimeException {
    /**
     * Constructs a new {@code DbNonInitialiseeException} with the current stack trace
     * and the specified detail message.
     *
     * @param detailMessage the detail message for this exception.
     */
    public DbNonInitialiseeException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Constructs a new {@code DbNonInitialiseeException} with the current stack trace,
     * the specified detail message and the specified cause.
     *
     * @param detailMessage the detail message for this exception.
     * @param throwable
     */
    public DbNonInitialiseeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * Constructs a new {@code DbNonInitialiseeException} with the current stack trace
     * and the specified cause.
     *
     * @param throwable the cause of this exception.
     */
    public DbNonInitialiseeException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Constructs a new {@code DbNonInitialiseeException} that includes the current stack
     * trace.
     */
    public DbNonInitialiseeException() {
    }
}
