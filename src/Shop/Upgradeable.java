package Shop;

import Characters.Character;
import Exceptions.NotEnoughMoneyException;

public interface Upgradeable {
    void upgrade(Character character) throws NotEnoughMoneyException;
}