package Test;

import Characters.Mage;
import Characters.Warrior;
import Characters.Thief;
import Characters.Enemy.Goblin;
import Characters.Enemy.Necromancer;
import Characters.Enemy.Boss;
import Characters.Character;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CharacterTest {

    // Helper na vytvorenie jednoduche postavy
    private Goblin makeEnemy() {
        return new Goblin(100, 100, 15, 2, 30, 30, 0);
    }

    // ==================== TAKE DAMAGE ====================

    @Test
    public void testTakeDamageReducesHealth() {
        Mage mage = new Mage(100, 100, 20, 0, 50, 50, 0);
        mage.takeDamage(30);
        assertEquals(70, mage.getHealth());
    }

    @Test
    public void testTakeDamageWithShield() {
        Mage mage = new Mage(100, 100, 20, 10, 50, 50, 0);
        mage.takeDamage(30);
        assertEquals(80, mage.getHealth());
    }

    @Test
    public void testTakeDamageCannotGoBelowZero() {
        Mage mage = new Mage(100, 100, 20, 0, 50, 50, 0);
        mage.takeDamage(999);
        assertEquals(0, mage.getHealth());
    }

    @Test
    public void testTakeDamageLessThanShieldDealsZero() {
        Mage mage = new Mage(100, 100, 20, 50, 50, 50, 0);
        mage.takeDamage(10);
        assertEquals(100, mage.getHealth());
    }

    // ==================== DODGE ====================

    @Test
    public void testDodgeChargeBlocksDamage() {
        Thief thief = new Thief(100, 100, 18, 3, 50, 50, 0);
        // Thief starts with 1 dodge charge
        thief.takeDamage(50);
        assertEquals(100, thief.getHealth());
        assertEquals(0, thief.getDodgeCharges());
    }

    @Test
    public void testDodgeChargeDecrementsAfterUse() {
        Thief thief = new Thief(100, 100, 18, 3, 50, 50, 0);
        thief.setDodgeCharges(2);
        thief.takeDamage(50);
        assertEquals(1, thief.getDodgeCharges());
    }

    @Test
    public void testNoDodgeChargesTakesDamage() {
        Thief thief = new Thief(100, 100, 18, 0, 50, 50, 0);
        thief.setDodgeCharges(0);
        thief.takeDamage(50);
        assertEquals(50, thief.getHealth());
    }

    // ==================== HEAL ====================

    @Test
    public void testHealIncreasesHealth() {
        Mage mage = new Mage(50, 100, 20, 0, 50, 50, 0);
        mage.heal(30);
        assertEquals(80, mage.getHealth());
    }

    @Test
    public void testHealCannotExceedMaxHealth() {
        Mage mage = new Mage(90, 100, 20, 0, 50, 50, 0);
        mage.heal(50);
        assertEquals(100, mage.getHealth());
    }

    // ==================== MANA ====================

    @Test
    public void testUseManaReducesMana() {
        Mage mage = new Mage(100, 100, 20, 0, 50, 50, 0);
        assertTrue(mage.useMana(20));
        assertEquals(30, mage.getMana());
    }

    @Test
    public void testUseManaReturnsFalseWhenNotEnough() {
        Mage mage = new Mage(100, 100, 20, 0, 10, 50, 0);
        assertFalse(mage.useMana(20));
        assertEquals(10, mage.getMana());
    }

    @Test
    public void testRestoreManaIncreasesMana() {
        Mage mage = new Mage(100, 100, 20, 0, 20, 50, 0);
        mage.restoreMana(20);
        assertEquals(40, mage.getMana());
    }

    @Test
    public void testRestoreManaCannotExceedMaxMana() {
        Mage mage = new Mage(100, 100, 20, 0, 40, 50, 0);
        mage.restoreMana(50);
        assertEquals(50, mage.getMana());
    }

    // ==================== MONEY ====================

    @Test
    public void testAddMoney() {
        Mage mage = new Mage(100, 100, 20, 0, 50, 50, 100);
        mage.addMoney(50);
        assertEquals(150, mage.getMoney());
    }

    @Test
    public void testSpendMoneySuccess() {
        Mage mage = new Mage(100, 100, 20, 0, 50, 50, 100);
        assertTrue(mage.spendMoney(50));
        assertEquals(50, mage.getMoney());
    }

    @Test
    public void testSpendMoneyFailWhenNotEnough() {
        Mage mage = new Mage(100, 100, 20, 0, 50, 50, 30);
        assertFalse(mage.spendMoney(50));
        assertEquals(30, mage.getMoney());
    }

    // ==================== IS ALIVE ====================

    @Test
    public void testIsAliveWhenHealthAboveZero() {
        Mage mage = new Mage(100, 100, 20, 0, 50, 50, 0);
        assertTrue(mage.isAlive());
    }

    @Test
    public void testIsDeadWhenHealthZero() {
        Mage mage = new Mage(100, 100, 20, 0, 50, 50, 0);
        mage.takeDamage(999);
        assertFalse(mage.isAlive());
    }

    // ==================== ATTACK ====================

    @Test
    public void testAttackDealsDamageToTarget() {
        Warrior warrior = new Warrior(120, 120, 25, 0, 40, 40, 0);
        Goblin goblin = makeEnemy();
        warrior.attack(goblin);
        assertEquals(77, goblin.getHealth()); // 100 - (25 - 2 shield)
    }

    // ==================== DRAW CARD ====================

    @Test
    public void testDrawCardAddsToHand() {
        Mage mage = new Mage(100, 100, 20, 0, 50, 50, 0);
        mage.drawCard();
        assertEquals(1, mage.getHand().size());
    }

    @Test
    public void testDrawCardMaxFive() {
        Mage mage = new Mage(100, 100, 20, 0, 50, 50, 0);
        mage.drawCard(10);
        assertEquals(5, mage.getHand().size());
    }

    @Test
    public void testDrawCardMultiple() {
        Mage mage = new Mage(100, 100, 20, 0, 50, 50, 0);
        mage.drawCard(3);
        assertEquals(3, mage.getHand().size());
    }

    // ==================== PLAY CARD ====================

    @Test
    public void testPlayCardRemovesFromHand() {
        Mage mage = new Mage(100, 100, 20, 0, 50, 50, 0);
        Goblin goblin = makeEnemy();
        mage.drawCard(1);
        assertEquals(1, mage.getHand().size());
        mage.playCard(0, goblin);
        assertEquals(0, mage.getHand().size());
    }

    @Test
    public void testPlayCardInvalidIndexDoesNothing() {
        Mage mage = new Mage(100, 100, 20, 0, 50, 50, 0);
        Goblin goblin = makeEnemy();
        mage.playCard(5, goblin); // nema kartu na indexe 5
        assertEquals(0, mage.getHand().size());
    }

    // ==================== LEVEL ====================

    @Test
    public void testDefaultLevelIsOne() {
        Mage mage = new Mage(100, 100, 20, 0, 50, 50, 0);
        assertEquals(1, mage.getLevel());
    }

    @Test
    public void testSetLevel() {
        Mage mage = new Mage(100, 100, 20, 0, 50, 50, 0);
        mage.setLevel(3);
        assertEquals(3, mage.getLevel());
    }

    @Test
    public void testUpgradeIncreasesLevel() throws Exception {
        Mage mage = new Mage(100, 100, 20, 0, 50, 50, 200);
        mage.upgrade(mage);
        assertEquals(2, mage.getLevel());
    }

    @Test
    public void testUpgradeThrowsWhenNotEnoughMoney() {
        Mage mage = new Mage(100, 100, 20, 0, 50, 50, 0);
        assertThrows(Exception.class, () -> mage.upgrade(mage));
    }

    // ==================== SPECIAL ABILITIES ====================

    @Test
    public void testMageSpecialHealsHalfMaxHP() {
        Mage mage = new Mage(50, 100, 20, 0, 50, 50, 0);
        mage.specialAbility(makeEnemy());
        assertEquals(100, mage.getHealth()); // 50 + 50 = 100
    }

    @Test
    public void testMageSpecialCannotExceedMaxHP() {
        Mage mage = new Mage(90, 100, 20, 0, 50, 50, 0);
        mage.specialAbility(makeEnemy());
        assertEquals(100, mage.getHealth());
    }

    @Test
    public void testWarriorSpecialExecutesLowHPEnemy() {
        Warrior warrior = new Warrior(120, 120, 25, 0, 40, 40, 0);
        Goblin goblin = new Goblin(20, 100, 15, 0, 30, 30, 0); // 20% HP
        warrior.specialAbility(goblin);
        assertFalse(goblin.isAlive());
    }

    @Test
    public void testWarriorSpecialDamagesHighHPEnemy() {
        Warrior warrior = new Warrior(120, 120, 25, 0, 40, 40, 0);
        Goblin goblin = new Goblin(100, 100, 15, 0, 30, 30, 0); // plne HP
        warrior.specialAbility(goblin);
        assertEquals(80, goblin.getHealth()); // 100 - 20% of 100
    }

    @Test
    public void testThiefSpecialAddsDodgeCharge() {
        Thief thief = new Thief(90, 90, 18, 0, 50, 50, 0);
        int before = thief.getDodgeCharges();
        thief.specialAbility(makeEnemy());
        assertEquals(before + 1, thief.getDodgeCharges());
    }

    @Test
    public void testThiefStartsWithOneDodgeCharge() {
        Thief thief = new Thief(90, 90, 18, 0, 50, 50, 0);
        assertEquals(1, thief.getDodgeCharges());
    }

    // ==================== ENEMIES ====================

    @Test
    public void testGoblinCanBeCreated() {
        Goblin goblin = new Goblin(100, 100, 15, 2, 30, 30, 0);
        assertEquals(100, goblin.getHealth());
        assertTrue(goblin.isAlive());
    }

    @Test
    public void testNecromancerCanBeCreated() {
        Necromancer necro = new Necromancer(120, 120, 18, 4, 40, 40, 0);
        assertEquals(120, necro.getHealth());
        assertTrue(necro.isAlive());
    }

    @Test
    public void testBossCanBeCreated() {
        Boss boss = new Boss(200, 200, 25, 6, 50, 50, 0);
        assertEquals(200, boss.getHealth());
        assertTrue(boss.isAlive());
    }
}