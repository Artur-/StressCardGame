package org.vaadin.artur.stresscardgame.engine.data;

import java.util.List;

public class DeckCounts {

    private List<Integer> playerDecks;
    private List<Integer> playPiles;

    public List<Integer> getPlayerDecks() {
        return playerDecks;
    }

    public void setPlayerDecks(List<Integer> playerDecks) {
        this.playerDecks = playerDecks;
    }

    public List<Integer> getPlayPiles() {
        return playPiles;
    }

    public void setPlayPiles(List<Integer> playPiles) {
        this.playPiles = playPiles;
    }

}
