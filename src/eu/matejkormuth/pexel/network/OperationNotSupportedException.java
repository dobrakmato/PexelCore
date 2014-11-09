package eu.matejkormuth.pexel.network;

public class OperationNotSupportedException extends RuntimeException {
    private static final long serialVersionUID = -9039126915709792167L;
    
    public OperationNotSupportedException(final String message) {
        super(message);
    }
}
