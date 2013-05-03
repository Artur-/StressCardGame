package org.vaadin.artur.stresscardgame.engine.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StressGameState implements Serializable {

    private int numberOfPlayers = 2;

    private List<StressPlayerState> playerState = new ArrayList<StressPlayerState>();
    private List<DeckInfo> playPiles = new ArrayList<DeckInfo>();

    public StressGameState(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        for (int i = 0; i < numberOfPlayers; i++) {
            playerState.add(new StressPlayerState());
        }
        for (int i = 0; i < 2; i++) {
            playPiles.add(new DeckInfo());
        }
    }

    public StressPlayerState getPlayerState(int player) {
        return playerState.get(player);
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public List<DeckInfo> getPlayPiles() {
        return playPiles;
    }

}
