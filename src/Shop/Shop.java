package Shop;

import Characters.Character;
import Cards.Card;
import Exceptions.NotEnoughMoneyException;
import Utils.GameLogger;
public class Shop {

    public void buyUpgrade(Upgradeable target, Character player) throws NotEnoughMoneyException {
        target.upgrade(player);
    }

    public void upgradeCardType(String type, Character player) throws NotEnoughMoneyException {

        int cost = 50;

        if (!player.spendMoney(cost)) {
            GameLogger.logError("Not enough gold for upgrade");
            throw new NotEnoughMoneyException("Not enough gold!");

        }

        player.getDeck().getCards()
                .stream()
                .filter(c -> c.getName().trim().equals(type))
                .forEach(Card::levelUp);

        player.getHand()
                .stream()
                .filter(c -> c.getName().trim().equals(type))
                .forEach(Card::levelUp);
    }
}