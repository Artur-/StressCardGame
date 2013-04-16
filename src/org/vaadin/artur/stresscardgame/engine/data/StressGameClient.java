package org.vaadin.artur.stresscardgame.engine.data;

import org.vaadin.artur.stresscardgame.engine.event.GameStartedEvent;

public interface StressGameClient {

    public void gameStarted(GameStartedEvent event);

    public void movePerformed();

    public void redeal();

    public void pileChoice();

    public void pileChoiceResolved();

    public void announceWinner(int playerNumber);

}
