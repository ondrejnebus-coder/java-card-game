package Characters;
import Deck.Deck;
import Deck.DeckFactory;
import Cards.Card;
import Shop.Upgradeable;
import Exceptions.NotEnoughMoneyException;
import java.util.List;
import java.util.ArrayList;

public abstract class Character implements Upgradeable {
    /**
     * Abstract base class representing a character in the game.
     * Contains common attributes such as health, mana and combat logic.
     */
    private int health;
    private int maxHealth;
    private int attackPower;
    private int shieldPower;
    private int mana;
    private int maxMana;
    private int money;
    private int dodgeCharges = 0;
    private int level;

    private Deck<Card> deck;
    private List<Card> hand;



    public Character(int health, int maxHealth, int attackPower, int shieldPower, int mana, int maxMana, int money) {
        this.health = health;
        this.maxHealth = maxHealth;
        this.attackPower = attackPower;
        this.shieldPower = shieldPower;
        this.mana = mana;
        this.maxMana = maxMana;
        this.money=money;
        this.level=1;
        this.deck = DeckFactory.createDefaultDeck();
        this.hand = new ArrayList<>();
    }

    public List<Card> getHand() {
        return hand;
    }

    public Deck<Card> getDeck() {
        return deck;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getShieldPower() {
        return shieldPower;
    }

    public int getMana() {
        return mana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getMoney() {
        return money;
    }
    public int getDodgeCharges(){return dodgeCharges;}
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public void setDodgeCharges(int amount) {
        this.dodgeCharges = amount;
    }
    /**
     * Applies damage to the character considering shield and dodge.
     * @param damage incoming damage
     */
    public void takeDamage(int damage) {
        if (dodgeCharges > 0) {
            dodgeCharges--;
            System.out.println("Útok bol vyhnutý!");
            return;
        }

        int finalDamage = Math.max(0, damage - shieldPower);
        health -= finalDamage;

        if (health < 0) {
            health = 0;
        }
    }
    /**
     * Heals the character up to max health.
     * @param amount amount of healing
     */
    public void heal(int amount) {
        health += amount;

        if (health > maxHealth) {
            health = maxHealth;
        }
    }
    /**
     * Uses mana if enough is available.
     * @param amount mana cost
     * @return true if mana was used
     */
    public boolean useMana(int amount) {
        if (mana >= amount) {
            mana -= amount;
            return true;
        }
        return false;
    }

    public void restoreMana(int amount) {
        mana += amount;

        if (mana > maxMana) {
            mana = maxMana;
        }
    }

    public void attack(Character target) {
        target.takeDamage(this.attackPower);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public abstract void specialAbility(Character target);
    /**
     * Draws a card from the deck into hand.
     */
    public void drawCard() {
        if (hand.size() >= 5) return;

        Card drawnCard = deck.drawCard();
        if (drawnCard != null) {
            hand.add(drawnCard);
        }
    }

    public void drawCard(int amount) {
        for (int i = 0; i < amount; i++) {
            drawCard();
        }
    }
    //adding money to equation
    public void addMoney(int amount) {
        money += amount;
    }
    public boolean spendMoney(int amount) {
        if (money >= amount) {
            money -= amount;
            return true;
        }
        return false;
    }
    /**
     * Plays a card from hand on a target.
     * @param index index of card in hand
     * @param target target character
     */
    public void playCard(int index, Character target) {
        if (index < hand.size()) {
            Card card = hand.remove(index);
            card.play(this, target);
        }
    }
    @Override
    public void upgrade(Character owner) throws NotEnoughMoneyException {
        int cost = 100; // napr

        if (spendMoney(cost)) {
            level++;
            System.out.println("Character upgraded!");
        } else {
            throw new NotEnoughMoneyException("Nedostatok money!");
        }
    }
}