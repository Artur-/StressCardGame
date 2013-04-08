package org.vaadin.artur.stresscardgame.engine.data;

import java.util.ArrayList;
import java.util.List;

public class StressPlayerState {
    private DeckInfo deck = new DeckInfo();
    private List<CardInfo> visibleCards = new ArrayList<CardInfo>();

    public DeckInfo getDeck() {
        return deck;
    }

    public void setDeck(DeckInfo deck) {
        this.deck = deck;
    }

    public List<CardInfo> getVisibleCards() {
        return visibleCards;
    }

    public void setVisibleCards(List<CardInfo> visibleCards) {
        this.visibleCards = visibleCards;
    }

}
