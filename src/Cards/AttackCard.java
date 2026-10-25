package Cards;

import Characters.Character;

public class AttackCard extends Card {

    private int baseDamage;

    public AttackCard(String name, int manaCost, String description, int baseDamage) {
        super(name, manaCost, description, 1);
        this.baseDamage = baseDamage;
    }

    @Override
    public void play(Character user, Character target) {
        if (user.useMana(getManaCost())) {

            int finalDamage = user.getAttackPower() + baseDamage + (getLevel() * 5);

            target.takeDamage(finalDamage);

            System.out.println(getName() + " (lvl " + getLevel() + ") za " + finalDamage + " damage");

        } else {
            System.out.println("Nedostatok many!");
        }
    }
}