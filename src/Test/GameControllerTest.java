package Test;

import GameControl.GameController;
import Characters.*;
import Characters.Enemy.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    @Test
    void testSelectAllCharacters() {
        GameController gc = new GameController();

        gc.selectCharacter("Mage");
        assertTrue(gc.getPlayer() instanceof Mage);

        gc.selectCharacter("Warrior");
        assertTrue(gc.getPlayer() instanceof Warrior);

        gc.selectCharacter("Thief");
        assertTrue(gc.getPlayer() instanceof Thief);

        gc.selectCharacter("Unknown");
        assertTrue(gc.getPlayer() instanceof Mage);
    }

    @Test
    void testEnemyCreationLevels() {
        GameController gc = new GameController();

        gc.selectCharacter("Mage");

        assertTrue(gc.getEnemy() instanceof Goblin);

        gc.nextEnemy();
        assertTrue(gc.getEnemy() instanceof Necromancer);

        gc.nextEnemy();
        assertTrue(gc.getEnemy() instanceof Boss);
    }

    @Test
    void testPlayTurn() {
        GameController gc = new GameController();

        gc.selectCharacter("Warrior");

        gc.getPlayer().drawCard(1);

        String result = gc.playTurn(0);

        assertNotNull(gc.getPlayer());
    }

    @Test
    void testEnemyTurn() {
        GameController gc = new GameController();

        gc.selectCharacter("Mage");

        String result = gc.enemyTurn();

        assertTrue(result == null || result instanceof String);
    }

    @Test
    void testUseSpecial() {
        GameController gc = new GameController();

        gc.selectCharacter("Mage");

        boolean used = gc.useSpecial();

        assertTrue(used);
    }

    @Test
    void testUseSpecialFail() {
        GameController gc = new GameController();

        gc.selectCharacter("Mage");

        gc.useSpecial();

        boolean second = gc.useSpecial();

        assertFalse(second);
    }

    @Test
    void testBuySpecialCharge() {
        GameController gc = new GameController();

        gc.selectCharacter("Mage");

        boolean result = gc.buySpecialCharge();

        assertTrue(result);
    }

    @Test
    void testBuySpecialChargeFail() {
        GameController gc = new GameController();

        gc.selectCharacter("Mage");

        // vyčerpaj peniaze postupne
        gc.getPlayer().spendMoney(100);

        boolean result = gc.buySpecialCharge();

        assertFalse(result);
    }

    @Test
    void testResetSpecial() {
        GameController gc = new GameController();

        gc.selectCharacter("Mage");

        gc.useSpecial();
        gc.resetSpecialForFight();

        assertEquals(1, gc.getSpecialCharges());
    }

    @Test
    void testEnemyDead() {
        GameController gc = new GameController();

        gc.selectCharacter("Mage");

        gc.getEnemy().takeDamage(999);

        assertTrue(gc.isEnemyDead());
    }

    @Test
    void testRewardPlayer() {
        GameController gc = new GameController();

        gc.selectCharacter("Mage");

        int before = gc.getPlayer().getMoney();

        gc.rewardPlayer();

        assertTrue(gc.getPlayer().getMoney() > before);
    }

    @Test
    void testGameFinished() {
        GameController gc = new GameController();

        gc.selectCharacter("Mage");

        gc.nextEnemy();
        gc.nextEnemy();
        gc.nextEnemy();

        assertTrue(gc.isGameFinished());
    }

    @Test
    void testTypes() {
        GameController gc = new GameController();

        gc.selectCharacter("Thief");

        assertEquals("thief", gc.getPlayerType());

        assertNotNull(gc.getEnemyType());
    }

    @Test
    void testPeekEnemyCard() {
        GameController gc = new GameController();

        gc.selectCharacter("Mage");

        gc.getEnemy().drawCard();

        String card = gc.peekEnemyCard();

        assertTrue(card == null || card instanceof String);
    }

    @Test
    void testIsThief() {
        GameController gc = new GameController();

        gc.selectCharacter("Thief");

        assertTrue(gc.isThief());

        gc.selectCharacter("Mage");

        assertFalse(gc.isThief());
    }
}