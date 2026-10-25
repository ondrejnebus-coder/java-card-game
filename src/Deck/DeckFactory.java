package Deck;

import Cards.*;

public class DeckFactory {

    public static Deck<Card> createDefaultDeck() {
        Deck<Card> deck = new Deck<>();

        // 15 attack
        for (int i = 0; i < 15; i++) {
            deck.addCard(new AttackCard("Attack " , 2, "Základný útok",1));
        }

        // 8 heal
        for (int i = 0; i < 8; i++) {
            deck.addCard(new HealCard("Heal " , 2, "Liečenie",1));
        }

        // 6 mana
        for (int i = 0; i < 6; i++) {
            deck.addCard(new ManaCard("Mana " , 0, "Obnoví manu",1));
        }

        // 3 ultimate
        for (int i = 0; i < 3; i++) {
            deck.addCard(new RandomCard("Ultimate " , 5, "Silný útok",1));
        }

        // bonus
        deck.addCard(new AttackCard("Bonus Attack", 3, "Bonusová karta",1));

        deck.shuffle();

        return deck;
    }
}