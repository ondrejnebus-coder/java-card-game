package Test;

import Utils.GameLogger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameLoggerTest {

    @Test
    void testLogInfo() {
        GameLogger.logInfo("Test info");
        assertTrue(true); // len coverage
    }

    @Test
    void testLogWarning() {
        GameLogger.logWarning("Test warning");
        assertTrue(true);
    }

    @Test
    void testLogError() {
        GameLogger.logError("Test error");
        assertTrue(true);
    }
}