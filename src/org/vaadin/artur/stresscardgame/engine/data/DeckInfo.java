package org.vaadin.artur.stresscardgame.engine.data;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.artur.playingcards.client.ui.Suite;
import org.vaadin.artur.playingcards.collection.ShufflableArrayList;

/**
 * Representation of a set of cards. The last card is the "top" card.
 * 
 */
public class DeckInfo {

    private ShufflableArrayList<CardInfo> cards = new ShufflableArrayList<CardInfo>();

    public void reset() {
        removeAllCards();

        // Initialize deck with 52 cards, no jokers
        for (Suite suite : Suite.values()) {
            for (int i = 1; i <= 13; i++) {
                CardInfo c = new CardInfo(suite, i);
                cards.add(c);
            }
        }
    }

    public void shuffle() {
        cards.shuffle();
    }

    private void removeAllCards() {
        cards.clear();
    }

    public int getNumberOfCards() {
        return cards.size();
    }

    public void addCardsAtBottom(List<CardInfo> cards) {
        this.cards.addAll(0, cards);
    }

    public void addCardsAtTop(List<CardInfo> cards) {
        this.cards.addAll(cards);
    }

    public void addCardAtBottom(CardInfo card) {
        cards.add(0, card);
    }

    public void addCardAtTop(CardInfo card) {
        cards.add(card);
    }

    public List<CardInfo> drawCards(int cardsToDraw) {
        int cardsInDeck = getNumberOfCards();
        if (cardsToDraw > cardsInDeck) {
            throw new IllegalArgumentException("Deck only contains "
                    + cardsInDeck + " cards");
        }
        List<CardInfo> removed = new ArrayList<CardInfo>(cardsToDraw);
        for (int i = 0; i < cardsToDraw; i++) {
            removed.add(drawCard());
        }
        return removed;
    }

    public CardInfo drawCard() {
        if (getNumberOfCards() < 1) {
            throw new IllegalArgumentException(
                    "Deck does not contain any cards");
        }
        return cards.remove(getNumberOfCards() - 1);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public CardInfo getTopCard() {
        if (isEmpty()) {
            return null;
        }

        return getCard(getNumberOfCards() - 1);
    }

    private CardInfo getCard(int i) {
        if (i >= getNumberOfCards()) {
            throw new IllegalArgumentException("Deck contains "
                    + getNumberOfCards() + " cards. Requested " + i);
        }
        return cards.get(i);
    }

    public List<CardInfo> drawAllCards() {
        return drawCards(getNumberOfCards());
    }
}
