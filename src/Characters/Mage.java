package Characters;

public class Mage extends Character{
    public Mage(int health, int maxHealth, int attackPower, int shiedlPower, int mana, int maxMana,int money) {
        super(health, maxHealth, attackPower, shiedlPower, mana, maxMana, money);
    }

    @Override
    public void specialAbility(Character target) {

            int healAmount = (int)(getMaxHealth() * 0.5);
            heal(healAmount);
            System.out.println("Mage sa healol o " + healAmount);

    }
}
