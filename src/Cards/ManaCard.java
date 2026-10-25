package Cards;

import Characters.Character;

public class ManaCard extends Card {

    private int baseMana;

    public ManaCard(String name, int manaCost, String description, int baseMana) {
        super(name, manaCost, description, 1);
        this.baseMana = baseMana;
    }

    @Override
    public void play(Character user, Character target) {

        int finalMana = baseMana + (getLevel() * 10);

        user.restoreMana(finalMana);

        System.out.println(getName() + " (lvl " + getLevel() + ") mana +" + finalMana);
    }
}