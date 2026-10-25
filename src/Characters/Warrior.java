package Characters;

public class Warrior extends Character{
    public Warrior(int health, int maxHealth, int attackPower, int shiedlPower, int mana, int maxMana,int money) {
        super(health, maxHealth, attackPower, shiedlPower, mana, maxMana, money);
    }

    @Override
    public void specialAbility(Character target) {


        double hpPercent = (double) target.getHealth() / target.getMaxHealth();

        if (hpPercent <= 0.22) {
            target.takeDamage(target.getHealth() + target.getShieldPower());
        } else {
            int damage = (int)(target.getMaxHealth() * 0.2);
            target.takeDamage(damage);
        }
    }
}
