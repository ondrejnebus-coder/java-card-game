package GameControl;

import java.io.*;

/**
 * Serializable snapshot of the current game state.
 * Used to save/load progress between fights.
 */
public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String SAVE_FILE = "savegame.dat";

    private String playerType;
    private int playerHealth;
    private int playerMaxHealth;
    private int playerMana;
    private int playerMaxMana;
    private int playerMoney;
    private int enemyLevel;
    private int specialCharges;
    private boolean inShop;

    public GameState(String playerType, int playerHealth, int playerMaxHealth,
                     int playerMana, int playerMaxMana, int playerMoney,
                     int enemyLevel, int specialCharges, boolean inShop) {
        this.playerType = playerType;
        this.playerHealth = playerHealth;
        this.playerMaxHealth = playerMaxHealth;
        this.playerMana = playerMana;
        this.playerMaxMana = playerMaxMana;
        this.playerMoney = playerMoney;
        this.enemyLevel = enemyLevel;
        this.specialCharges = specialCharges;
        this.inShop = inShop;
    }


    public String getPlayerType()   { return playerType; }
    public int getPlayerHealth()    { return playerHealth; }
    public int getPlayerMaxHealth() { return playerMaxHealth; }
    public int getPlayerMana()      { return playerMana; }
    public int getPlayerMaxMana()   { return playerMaxMana; }
    public int getPlayerMoney()     { return playerMoney; }
    public int getEnemyLevel()      { return enemyLevel; }
    public int getSpecialCharges()  { return specialCharges; }
    public boolean isInShop()       { return inShop; }

    /**
     * Saves this GameState to disk.
     */
    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.err.println("[GameState] Save failed: " + e.getMessage());
        }
    }

    /**
     * Loads a GameState from disk. Returns null if no save exists.
     */
    public static GameState load() {
        File f = new File(SAVE_FILE);
        if (!f.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (GameState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[GameState] Load failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes the save file (called on game over / new game).
     */
    public static void deleteSave() {
        new File(SAVE_FILE).delete();
    }
}