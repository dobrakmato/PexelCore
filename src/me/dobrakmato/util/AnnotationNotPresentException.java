package me.dobrakmato.util;

public class AnnotationNotPresentException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public AnnotationNotPresentException() {
        super();
    }
    
    public AnnotationNotPresentException(final String message) {
        super(message);
    }
    
    public AnnotationNotPresentException(final Throwable throwable) {
        super(throwable);
    }
    
    public AnnotationNotPresentException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
