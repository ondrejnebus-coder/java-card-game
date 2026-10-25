package Cards;

import Characters.Character;
import Exceptions.NotEnoughMoneyException;
import Shop.Upgradeable;

public abstract class Card implements Upgradeable {
    /**
     * Abstract class representing a card.
     * Cards can be played and upgraded.
     */
    private String name;
    private int manaCost;
    private String description;
    private int level;

    public Card(String name, int manaCost, String description, int level) {
        this.name = name;
        this.manaCost = manaCost;
        this.description = description;
        this.level=level;
    }

    public String getName() {
        return name;
    }

    public int getManaCost() {
        return manaCost;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    /**
     * Executes the card effect.
     * @param user character using the card
     * @param target target character
     */
    public abstract void play(Character user, Character target);

    public int getUpgradeCost() {
        return 10 + (level * 5);
    }
    /**
     * Upgrades the card if player has enough money.
     */
    @Override
    public void upgrade(Character owner) throws NotEnoughMoneyException {
        int cost = getUpgradeCost();

        if (owner.spendMoney(cost)) {
            level++;
            System.out.println(name + " upgraded na level " + level);
        } else {
            throw new NotEnoughMoneyException("Nedostatok money!");
        }
    }
    public void levelUp() {
        level++;
    }
}