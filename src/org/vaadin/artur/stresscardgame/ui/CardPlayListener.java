package org.vaadin.artur.stresscardgame.ui;

import org.vaadin.artur.stresscardgame.engine.data.CardInfo;

public interface CardPlayListener {

    public void playCard(CardInfo cardInfo, int pile);

}
