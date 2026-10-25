package Test;

import Cards.*;
import Characters.*;
import Characters.Enemy.Necromancer;
import Characters.Enemy.Goblin;
import Characters.Enemy.Boss;



import Deck.*;
import Exceptions.NotEnoughMoneyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardAndDeckTest {

    @Test
    void testAttackCardDamage() {
        Warrior w = new Warrior(100,100,10,0,50,50,0);
        Goblin g = new Goblin(100,100,10,0,30,30,0);

        AttackCard c = new AttackCard("Attack", 2, "dmg", 5);

        c.play(w, g);

        assertTrue(g.getHealth() < 100);
    }

    @Test
    void testHealCard() {
        Mage m = new Mage(50,100,10,0,50,50,0);

        HealCard c = new HealCard("Heal",2,"heal",10);

        c.play(m, null);

        assertTrue(m.getHealth() > 50);
    }

    @Test
    void testManaCard() {
        Mage m = new Mage(100,100,10,0,10,50,0);

        ManaCard c = new ManaCard("Mana",0,"mana",10);

        c.play(m,null);

        assertTrue(m.getMana() > 10);
    }

    @Test
    void testRandomCardDamage() {
        Warrior w = new Warrior(100,100,10,0,50,50,0);
        Goblin g = new Goblin(100,100,10,0,30,30,0);

        RandomCard c = new RandomCard("Ult",5,"ult",5);

        c.play(w,g);

        assertTrue(g.getHealth() < 100);
    }

    @Test
    void testCardUpgrade() throws Exception {
        Warrior w = new Warrior(100,100,10,0,50,50,200);

        AttackCard c = new AttackCard("Attack",2,"dmg",5);

        int lvl = c.getLevel();

        c.upgrade(w);

        assertEquals(lvl+1, c.getLevel());
    }

    @Test
    void testCardUpgradeFail() {
        Warrior w = new Warrior(100,100,10,0,50,50,0);

        AttackCard c = new AttackCard("Attack",2,"dmg",5);

        assertThrows(NotEnoughMoneyException.class, () -> {
            c.upgrade(w);
        });
    }

    @Test
    void testDeckAddAndDraw() {
        Deck<Card> deck = new Deck<>();

        AttackCard c = new AttackCard("A",2,"",5);

        deck.addCard(c);

        assertEquals(1, deck.getSize());

        Card drawn = deck.drawCard();

        assertNotNull(drawn);
        assertEquals(0, deck.getSize());
    }

    @Test
    void testDeckEmptyDraw() {
        Deck<Card> deck = new Deck<>();

        Card c = deck.drawCard();

        assertNull(c);
    }

    @Test
    void testDeckClear() {
        Deck<Card> deck = new Deck<>();

        deck.addCard(new AttackCard("A",2,"",5));
        deck.clear();

        assertTrue(deck.isEmpty());
    }

    @Test
    void testDeckFactory() {
        Deck<Card> deck = DeckFactory.createDefaultDeck();

        assertTrue(deck.getSize() > 0);
    }

    @Test
    void testDeckShuffle() {
        Deck<Card> deck = DeckFactory.createDefaultDeck();

        deck.shuffle();

        assertTrue(deck.getSize() > 0);
    }
}