package Cards;

import Characters.Character;
import Characters.Mage;
import Characters.Thief;
import Characters.Warrior;

public class RandomCard extends Card {

    private int baseDamage;

    public RandomCard(String name, int manaCost, String description, int baseDamage) {
        super(name, manaCost, description, 1);
        this.baseDamage = baseDamage;
    }

    @Override
    public void play(Character user, Character target) {
        if (user.useMana(getManaCost())) {

            int finalDamage = (user.getAttackPower() * 2) + baseDamage + (getLevel() * 4);

            if (user instanceof Mage) {
                System.out.println("MAGE ULTIMATE");
            } else if (user instanceof Warrior) {
                System.out.println("WARRIOR ULTIMATE!");
            } else if (user instanceof Thief) {
                System.out.println("THIEF ULTIMATE");
            } else {
                System.out.println("ULTIMATE " + getName() + " aktivovaná!");
            }

            target.takeDamage(finalDamage);
            System.out.println(getName() + " (lvl " + getLevel() + ") spôsobil celkovo " + finalDamage + " poškodenia.");

        } else {
            System.out.println("Nedostatok many pre kartu " + getName() + "!");
        }
    }
}