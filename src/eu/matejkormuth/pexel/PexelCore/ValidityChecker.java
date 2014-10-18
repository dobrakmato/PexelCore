package eu.matejkormuth.pexel.PexelCore;

import eu.matejkormuth.pexel.PexelCore.minigame.Minigame;

/**
 * Class that is used for checking validity of parts registered to pexel.
 */
public final class ValidityChecker {
    public static void checkMinigame(final Minigame minigame) {
        if (!minigame.getName().matches("^[a-z0-9]*$")) { throw new ValidationException(
                "Minigame name does not match pattern '[a-zA-Z0-9]'!"); }
        if (minigame.getCategory() == null) { throw new ValidationException(
                "Minigame must return category!"); }
    }
    
    public static class ValidationException extends RuntimeException {
        private static final long serialVersionUID = 8219849002256286968L;
        
        public ValidationException(final String s) {
            super(s);
        }
    }
}
