package Deck;

import Cards.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck<T extends Card> {

    private List<T> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public void addCard(T card) {
        cards.add(card);
    }

    public T drawCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int getSize() {
        return cards.size();
    }

    public List<T> getCards() {
        return cards;
    }

    public void clear() {
        cards.clear();
    }
}