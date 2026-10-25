package Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameLogger {

    private static final Logger logger = Logger.getLogger(GameLogger.class.getName());

    public static void logInfo(String message) {
        logger.log(Level.INFO, message);
    }

    public static void logWarning(String message) {
        logger.log(Level.WARNING, message);
    }

    public static void logError(String message) {
        logger.log(Level.SEVERE, message);
    }
}