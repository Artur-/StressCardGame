package org.vaadin.artur.stresscardgame.engine;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.vaadin.artur.stresscardgame.engine.data.CardInfo;
import org.vaadin.artur.stresscardgame.engine.data.DeckCounts;
import org.vaadin.artur.stresscardgame.engine.data.StressGameClient;

public class StressGameEngine implements Serializable {
    private LockedStressGameEngine engine = new LockedStressGameEngine();
    private Lock gameLock = new ReentrantLock();

    public StressGameEngine() {
        engine = new LockedStressGameEngine();
    }

    public void registerClient(StressGameClient client, PlayerInfo playerInfo) {
        gameLock.lock();
        try {
            engine.registerClient(client, playerInfo);
        } finally {
            gameLock.unlock();
        }
    }

    public List<CardInfo> getVisibleCards(int player) {
        gameLock.lock();
        try {
            return engine.getVisibleCards(player);
        } finally {
            gameLock.unlock();
        }
    }

    public void playCard(int player, CardInfo card, int playPile)
            throws IllegalPlayException {
        gameLock.lock();
        try {
            engine.playCard(player, card, playPile);
        } finally {
            gameLock.unlock();
        }
    }

    public List<CardInfo> getPlayPileTopCards() {
        gameLock.lock();
        try {
            return engine.getPlayPileTopCards();
        } finally {
            gameLock.unlock();
        }
    }

    public void pileChosen(int playerNumber, int pile) {
        gameLock.lock();
        try {
            engine.pileChosen(playerNumber, pile);
        } finally {
            gameLock.unlock();
        }
    }

    public DeckCounts getDeckCounts() {
        gameLock.lock();
        try {
            return engine.getDeckCounts();
        } finally {
            gameLock.unlock();
        }
    }

}
