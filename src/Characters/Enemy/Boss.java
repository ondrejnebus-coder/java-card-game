package Characters.Enemy;

import Characters.Character;

public class Boss extends Character {
    public Boss(int health, int maxHealth, int attackPower, int shiedlPower, int mana, int maxMana,int money) {
        super(health, maxHealth, attackPower, shiedlPower, mana, maxMana,money);
    }

    @Override
    public void specialAbility(Character target) {

    }
}
