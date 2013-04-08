package org.vaadin.artur.stresscardgame.engine.util;

import org.vaadin.artur.playingcards.Card;
import org.vaadin.artur.playingcards.Deck;
import org.vaadin.artur.stresscardgame.engine.data.CardInfo;


public class CardUtil {

    public static void clear(Deck deck) {
        while (!deck.isEmpty()) {
            deck.removeTopCard();
        }
    }

    public static void update(Card target, CardInfo source) {
        target.setRank(source.getRank());
        target.setSuite(source.getSuite());
    }

    public static void update(CardInfo target, CardInfo source) {
        target.setRank(source.getRank());
        target.setSuite(source.getSuite());
    }

    public static boolean equals(Card card1, CardInfo card2) {
        if (card1 == null) {
            return card2 == null;
        }

        return card1.getRank() == card2.getRank()
                && card1.getSuite() == card2.getSuite();
    }

    public static boolean equals(CardInfo card1, CardInfo card2) {
        if (card1 == null) {
            return card2 == null;
        }

        return card1.getRank() == card2.getRank()
                && card1.getSuite() == card2.getSuite();
    }

    public static CardInfo createCardInfo(Card card) {
        return new CardInfo(card.getSuite(), card.getRank());
    }

    public static CardInfo clone(CardInfo topCard) {
        return new CardInfo(topCard.getSuite(), topCard.getRank());
    }

}
