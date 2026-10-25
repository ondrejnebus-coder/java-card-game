package GameControl;

import Characters.Character;
import Characters.Mage;
import Characters.Warrior;
import Characters.Thief;
import Characters.Enemy.Goblin;
import Characters.Enemy.Necromancer;
import Characters.Enemy.Boss;
import Cards.Card;
import Utils.GameLogger;

public class GameController {
    /**
     * Controls game logic including turns, enemies and player actions.
     */
    private Character player;
    private Character enemy;
    private int enemyLevel = 1;
    private int specialCharges = 0; // starts at 0 - must be purchased in shop

    public void selectCharacter(String type) {
        switch (type) {
            case "Mage":
                player = new Mage(100, 100, 20, 5, 50, 50, 100);
                break;
            case "Warrior":
                player = new Warrior(120, 120, 25, 8, 40, 40, 100);
                break;
            case "Thief":
                player = new Thief(90, 90, 18, 3, 50, 50, 100);
                break;
            default:
                player = new Mage(100, 100, 20, 5, 50, 50, 100);
                break;
        }
        createEnemy();
    }

    /**
     * Creates enemy based on current level.
     */
    public void createEnemy() {
        switch (enemyLevel) {
            case 1: enemy = new Goblin(100, 100, 15, 2, 30, 30, 0); break;
            case 2: enemy = new Necromancer(120, 120, 18, 4, 40, 40, 0); break;
            case 3: enemy = new Boss(200, 200, 25, 6, 50, 50, 0); break;
            default: enemy = null; break;
        }
    }

    /**
     * Executes player's card action only (no enemy turn).
     * The enemy turn is triggered separately (with delay) from GameView.
     *
     * @param index selected card index
     */
    public void playPlayerCard(int index) {
        player.playCard(index, enemy);
        GameLogger.logInfo("Player plays card at index: " + index);
        if (enemy.isAlive()) {
            player.drawCard();
        }
    }

    /**
     * Legacy method kept for compatibility.
     *
     * @param index selected card index
     * @return enemy card used
     */
    public String playTurn(int index) {
        player.playCard(index, enemy);
        GameLogger.logInfo("Player plays card at index: " + index);
        if (!enemy.isAlive()) return null;
        player.drawCard();
        return enemyTurn();
    }

    /**
     * Executes enemy turn.
     *
     * @return name of card used by enemy
     */
    public String enemyTurn() {
        GameLogger.logInfo("Enemy turn started");
        if (enemy == null || !enemy.isAlive()) {
            GameLogger.logWarning("Enemy has no cards to play");
            return null;
        }

        enemy.drawCard();

        if (!enemy.getHand().isEmpty()) {
            Card card = enemy.getHand().get(0);
            String cardName = card.getName();
            card.play(enemy, player);
            enemy.getHand().clear();
            return cardName;
        }

        return null;
    }

    public boolean useSpecial() {
        if (specialCharges > 0) {
            specialCharges--;
            player.specialAbility(enemy);
            return true;
        }
        return false;
    }

    /**
     * Buys one special charge for 70 gold.
     */
    public boolean buySpecialCharge() {
        if (player.spendMoney(70)) {
            specialCharges++;
            return true;
        }
        return false;
    }

    public int getSpecialCharges() { return specialCharges; }

    public void resetSpecialForFight() { specialCharges = 1; }

    /**
     * Checks if enemy is dead.
     */
    public boolean isEnemyDead() { return enemy != null && !enemy.isAlive(); }

    public void rewardPlayer() {
        int reward = 50 + enemyLevel * 20;
        player.addMoney(reward);
    }

    public boolean isGameFinished() { return enemy == null; }

    /** Returns true if the current enemy is the last one (boss). */
    public boolean isFinalEnemy() { return enemyLevel == 3; }

    public void nextEnemy() {
        enemyLevel++;
        createEnemy();
        // special charges carry over between fights - NOT reset here
    }


    /**
     * Saves the current game state to disk.
     */
    /**
     * Restores game state from a saved GameState object.
     */
    public void loadFromState(GameState state) {
        enemyLevel = state.getEnemyLevel();
        specialCharges = state.getSpecialCharges();

        switch (state.getPlayerType()) {
            case "mag":
                player = new Mage(state.getPlayerHealth(), state.getPlayerMaxHealth(),
                        20, 5, state.getPlayerMana(), state.getPlayerMaxMana(),
                        state.getPlayerMoney());
                break;
            case "warrior":
                player = new Warrior(state.getPlayerHealth(), state.getPlayerMaxHealth(),
                        25, 8, state.getPlayerMana(), state.getPlayerMaxMana(),
                        state.getPlayerMoney());
                break;
            case "thief":
                player = new Thief(state.getPlayerHealth(), state.getPlayerMaxHealth(),
                        18, 3, state.getPlayerMana(), state.getPlayerMaxMana(),
                        state.getPlayerMoney());
                break;
            default:
                player = new Mage(state.getPlayerHealth(), state.getPlayerMaxHealth(),
                        20, 5, state.getPlayerMana(), state.getPlayerMaxMana(),
                        state.getPlayerMoney());
                break;
        }

        createEnemy();
        GameLogger.logInfo("Game state loaded (level=" + enemyLevel + ")");
    }

    public void saveState() {
        GameState state = new GameState(
                getPlayerType(),
                player.getHealth(),
                player.getMaxHealth(),
                player.getMana(),
                player.getMaxMana(),
                player.getMoney(),
                enemyLevel,
                specialCharges,
                false
        );
        state.save();
        GameLogger.logInfo("Game state saved (level=" + enemyLevel + ")");
    }

    /**
     * Saves the current game state while in the shop.
     */
    public void saveStateInShop() {
        GameState state = new GameState(
                getPlayerType(),
                player.getHealth(),
                player.getMaxHealth(),
                player.getMana(),
                player.getMaxMana(),
                player.getMoney(),
                enemyLevel,
                specialCharges,
                true
        );
        state.save();
        GameLogger.logInfo("Game state saved in shop (level=" + enemyLevel + ")");
    }


    public Character getPlayer() { return player; }
    public Character getEnemy()  { return enemy; }
    public int getEnemyLevel()   { return enemyLevel; }

    public String getPlayerType() {
        if (player instanceof Mage)    return "mag";
        if (player instanceof Warrior) return "warrior";
        if (player instanceof Thief)   return "thief";
        return "mag";
    }

    public String getEnemyType() {
        if (enemy instanceof Goblin)      return "goblin";
        if (enemy instanceof Necromancer) return "necromancer";
        if (enemy instanceof Boss)        return "boss";
        return "goblin";
    }

    public String peekEnemyCard() {
        if (enemy != null && !enemy.getHand().isEmpty()) {
            return enemy.getHand().get(0).getName();
        }
        return null;
    }

    public boolean isThief() {
        return player instanceof Thief;
    }
}