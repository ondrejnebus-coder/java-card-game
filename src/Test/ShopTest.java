package Test;

import Shop.Shop;
import Characters.*;
import Cards.*;
import Exceptions.NotEnoughMoneyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShopTest {

    @Test
    void testBuyUpgradeSuccess() throws Exception {
        Shop shop = new Shop();
        Warrior w = new Warrior(100,100,10,0,50,50,200);

        int levelBefore = w.getLevel();

        shop.buyUpgrade(w, w);

        assertEquals(levelBefore + 1, w.getLevel());
    }

    @Test
    void testBuyUpgradeFail() {
        Shop shop = new Shop();
        Warrior w = new Warrior(100,100,10,0,50,50,0);

        assertThrows(NotEnoughMoneyException.class, () -> {
            shop.buyUpgrade(w, w);
        });
    }

    @Test
    void testUpgradeCardTypeSuccess() throws Exception {
        Shop shop = new Shop();
        Warrior w = new Warrior(100,100,10,0,50,50,200);

        // pridaj kartu do decku
        AttackCard c = new AttackCard("Attack",2,"",5);
        w.getDeck().addCard(c);

        int levelBefore = c.getLevel();

        shop.upgradeCardType("Attack", w);

        assertTrue(c.getLevel() > levelBefore);
    }

    @Test
    void testUpgradeCardTypeFail() {
        Shop shop = new Shop();
        Warrior w = new Warrior(100,100,10,0,50,50,0);

        assertThrows(NotEnoughMoneyException.class, () -> {
            shop.upgradeCardType("Attack", w);
        });
    }

    @Test
    void testUpgradeCardInHand() throws Exception {
        Shop shop = new Shop();
        Warrior w = new Warrior(100,100,10,0,50,50,200);

        AttackCard c = new AttackCard("Attack",2,"",5);
        w.getHand().add(c);

        int before = c.getLevel();

        shop.upgradeCardType("Attack", w);

        assertTrue(c.getLevel() > before);
    }

    @Test
    void testUpgradeWrongType() throws Exception {
        Shop shop = new Shop();
        Warrior w = new Warrior(100,100,10,0,50,50,200);

        AttackCard c = new AttackCard("Attack",2,"",5);
        w.getDeck().addCard(c);

        int before = c.getLevel();

        shop.upgradeCardType("Heal", w);

        assertEquals(before, c.getLevel());
    }
}