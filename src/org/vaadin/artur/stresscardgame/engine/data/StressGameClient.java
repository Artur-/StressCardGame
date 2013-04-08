package org.vaadin.artur.stresscardgame.engine.data;

import java.util.Map;

import org.vaadin.artur.stresscardgame.engine.PlayerInfo;


public interface StressGameClient {

    public void gameStarted(int playerNumber,
            Map<Integer, PlayerInfo> playerInfo);

    public void movePerformed();

    public void redeal();

    public void pileChoice();

    public void pileChoiceResolved();

    public void announceWinner(int playerNumber);
}
