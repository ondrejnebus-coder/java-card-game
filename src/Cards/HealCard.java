package Cards;

import Characters.Character;

public class HealCard extends Card {

    private int baseHeal;

    public HealCard(String name, int manaCost, String description, int baseHeal) {
        super(name, manaCost, description, 1);
        this.baseHeal = baseHeal;
    }

    @Override
    public void play(Character user, Character target) {
        if (user.useMana(getManaCost())) {

            int finalHeal = baseHeal + (getLevel() * 10);

            user.heal(finalHeal);

            System.out.println(getName() + " (lvl " + getLevel() + ") heal za " + finalHeal);

        } else {
            System.out.println("Nedostatok many!");
        }
    }
}