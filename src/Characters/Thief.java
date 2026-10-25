package Characters;

public class Thief extends Character {
    public Thief(int health, int maxHealth, int attackPower, int shieldPower, int mana, int maxMana, int money) {
        super(health, maxHealth, attackPower, shieldPower, mana, maxMana, money);
        setDodgeCharges(1);
    }

    @Override
    public void specialAbility(Character target) {
        setDodgeCharges(getDodgeCharges() + 1);
        System.out.println("Thief získal dodge charge! Celkom: " + getDodgeCharges());
    }
}