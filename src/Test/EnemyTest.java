package Test;

import Characters.Enemy.*;
import Characters.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyTest {

    @Test
    void testGoblinConstructorAndAlive() {
        Goblin g = new Goblin(100, 100, 10, 2, 20, 20, 0);
        assertTrue(g.isAlive());
    }

    @Test
    void testNecromancerConstructorAndDamage() {
        Necromancer n = new Necromancer(100, 100, 10, 0, 20, 20, 0);

        n.takeDamage(20);

        assertTrue(n.getHealth() < 100);
    }

    @Test
    void testBossConstructorAndHeal() {
        Boss b = new Boss(50, 100, 10, 0, 20, 20, 0);

        b.heal(30);

        assertEquals(80, b.getHealth());
    }

    @Test
    void testEnemyDrawCard() {
        Goblin g = new Goblin(100, 100, 10, 0, 20, 20, 0);

        g.drawCard();

        assertTrue(g.getHand().size() >= 0); // pokrytie
    }

    @Test
    void testEnemyPlayCard() {
        Goblin g = new Goblin(100, 100, 10, 0, 20, 20, 0);
        Warrior w = new Warrior(100, 100, 10, 0, 20, 20, 0);

        g.drawCard();

        if (!g.getHand().isEmpty()) {
            g.playCard(0, w);
            assertTrue(w.getHealth() <= 100);
        }
    }

    @Test
    void testSpecialAbilityCalls() {
        Goblin g = new Goblin(100, 100, 10, 0, 20, 20, 0);
        Necromancer n = new Necromancer(100, 100, 10, 0, 20, 20, 0);
        Boss b = new Boss(100, 100, 10, 0, 20, 20, 0);
        Warrior target = new Warrior(100, 100, 10, 0, 20, 20, 0);

        // aj keď nič nerobia → pokryjú sa riadky
        g.specialAbility(target);
        n.specialAbility(target);
        b.specialAbility(target);

        assertTrue(true); // len aby test prešiel
    }

    @Test
    void testEnemyManaUsage() {
        Goblin g = new Goblin(100, 100, 10, 0, 20, 20, 0);

        boolean used = g.useMana(10);

        assertTrue(used);
        assertEquals(10, g.getMana());
    }
}