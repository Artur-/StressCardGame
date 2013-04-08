package org.vaadin.artur.stresscardgame.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.artur.stresscardgame.engine.data.CardInfo;
import org.vaadin.artur.stresscardgame.engine.data.DeckCounts;
import org.vaadin.artur.stresscardgame.engine.data.DeckInfo;
import org.vaadin.artur.stresscardgame.engine.data.StressGameClient;
import org.vaadin.artur.stresscardgame.engine.data.StressGameState;
import org.vaadin.artur.stresscardgame.engine.data.StressPlayerState;
import org.vaadin.artur.stresscardgame.engine.util.CardUtil;

public class LockedStressGameEngine implements Serializable {
    private enum EngineState {
        WAITING_FOR_PLAYERS, RUNNING, ENDED;
    }

    private static final int NUMBER_OF_PLAYERS = 2;
    private StressGameState state;
    private EngineState engineState = EngineState.WAITING_FOR_PLAYERS;
    private List<StressGameClient> players = new ArrayList<StressGameClient>();
    private Map<Integer, PlayerInfo> playerInfo = new HashMap<Integer, PlayerInfo>();
    private boolean pileChoicePending = false;

    public LockedStressGameEngine() {
        state = new StressGameState(NUMBER_OF_PLAYERS);
        setupDecks();
    }

    public void registerClient(StressGameClient player, PlayerInfo info) {
        if (engineState != EngineState.WAITING_FOR_PLAYERS) {
            throw new IllegalStateException("Not waiting for players");
        }

        players.add(player);
        playerInfo.put(players.size() - 1, info);
        if (players.size() == NUMBER_OF_PLAYERS) {
            startGame();
        }
    }

    private void startGame() {
        if (players.size() != NUMBER_OF_PLAYERS) {
            throw new IllegalStateException(
                    "Can't start game with wrong number of players");
        }

        engineState = EngineState.RUNNING;

        deckToEmptySlots();
        drawPileCards();

        for (int playerNumber = 0; playerNumber < players.size(); playerNumber++) {
            players.get(playerNumber).gameStarted(playerNumber, playerInfo);
        }

        stressCheck();
    }

    private void setupDecks() {
        // Shuffle a deck and deal to both players
        DeckInfo deck = new DeckInfo();
        deck.reset();
        deck.shuffle();

        int cardsPerPlayer = 52 / NUMBER_OF_PLAYERS;

        for (int player = 0; player < NUMBER_OF_PLAYERS; player++) {
            List<CardInfo> playerCards = deck.drawCards(cardsPerPlayer);
            state.getPlayerState(player).getDeck().addCardsAtTop(playerCards);
        }
    }

    private void drawPlayPileCard(int player) {
        StressPlayerState playerState = state.getPlayerState(player);

        state.getPlayPiles().get(player)
                .addCardAtTop(playerState.getDeck().drawCard());

    }

    private void deckToEmptySlots(int player) {
        StressPlayerState playerState = state.getPlayerState(player);
        while (playerState.getVisibleCards().size() < 4
                && !playerState.getDeck().isEmpty()) {
            playerState.getVisibleCards().add(playerState.getDeck().drawCard());
        }
    }

    public List<CardInfo> getVisibleCards(int player) {
        ArrayList<CardInfo> cards = new ArrayList<CardInfo>();
        for (CardInfo c : state.getPlayerState(player).getVisibleCards()) {
            cards.add(new CardInfo(c.getSuite(), c.getRank()));
        }

        return cards;
    }

    public void playCard(int player, CardInfo card, int playPileId)
            throws IllegalPlayException {
        if (engineState != EngineState.RUNNING) {
            return;
        }

        if (pileChoicePending) {
            // can't play a card when supposed to select a pile
            throw new IllegalPlayException();
        }
        StressPlayerState playerState = state.getPlayerState(player);
        int cardIndex = playerState.getVisibleCards().indexOf(card);
        if (cardIndex == -1) {
            throw new IllegalArgumentException("Card " + card
                    + " is not playable");
        }

        DeckInfo playPile = state.getPlayPiles().get(playPileId);
        if (!canBePlayed(card, playPile.getTopCard())) {
            // No longer a valid move (or was it ever?)
            throw new IllegalPlayException();
        }

        playPile.addCardAtTop(card);

        // Draw a new card from the deck
        if (!playerState.getDeck().isEmpty()) {
            CardUtil.update(playerState.getVisibleCards().get(cardIndex),
                    playerState.getDeck().drawCard());
        } else {
            playerState.getVisibleCards().remove(cardIndex);
        }

        postPlayCard();
    }

    private void postPlayCard() {
        // Notify other player about move
        for (StressGameClient p : players) {
            p.movePerformed();
        }

        if (winnerCheck()) {
        } else if (stressCheck()) {
        } else if (drawPileCardsIfNeeded()) {
        }

    }

    private boolean winnerCheck() {
        for (int player = 0; player < NUMBER_OF_PLAYERS; player++) {
            if (state.getPlayerState(player).getDeck().isEmpty()
                    && state.getPlayerState(player).getVisibleCards().isEmpty()) {

                // Notify players about winner
                for (StressGameClient p : players) {
                    p.announceWinner(player);
                }
                engineState = EngineState.ENDED;
                return true;
            }
        }
        return false;
    }

    private boolean playPilesEqualRank() {
        CardInfo ref = null;
        for (DeckInfo playPile : state.getPlayPiles()) {
            if (ref == null) {
                ref = playPile.getTopCard();
            } else {
                if (ref.getRank() != playPile.getTopCard().getRank()) {
                    return false;
                }
            }
        }

        return true;

    }

    private boolean stressCheck() {
        if (playPilesEqualRank()) {
            doPileChoice();
            return true;
        } else {
            return false;
        }
    }

    private boolean drawPileCardsIfNeeded() {
        if (!noMoreMoves()) {
            return false;
        }

        if (drawPileCards()) {
            // Card(s) redrawn and game continues
            for (StressGameClient p : players) {
                p.redeal();
            }
        }

        return true;
    }

    private boolean drawPileCards() {
        do {
            boolean atleastOneCardDrawn = false;

            for (int player = 0; player < NUMBER_OF_PLAYERS; player++) {
                if (!state.getPlayerState(player).getDeck().isEmpty()) {
                    atleastOneCardDrawn = true;
                    drawPlayPileCard(player);
                }
            }

            if (!atleastOneCardDrawn) {
                doPileChoice();
                return false;
            }
        } while (noMoreMoves() || playPilesEqualRank());

        return true;

    }

    private void doPileChoice() {
        // Both decks are empty
        for (StressGameClient p : players) {
            p.pileChoice();
        }

        pileChoicePending = true;
    }

    private boolean noMoreMoves() {
        for (int player = 0; player < NUMBER_OF_PLAYERS; player++) {
            for (CardInfo c : state.getPlayerState(player).getVisibleCards()) {
                for (DeckInfo playPile : state.getPlayPiles()) {
                    if (canBePlayed(c, playPile.getTopCard())) {
                        System.out.println("Can still play " + c + " on "
                                + playPile.getTopCard());
                        return false;
                    }
                }
            }

        }

        System.out.println("No more plays");
        return true;
    }

    private boolean canBePlayed(CardInfo cardToPlay, CardInfo targetCard) {
        int smallerRank = targetCard.getRank() - 1;
        if (smallerRank == 0) {
            smallerRank = 13;
        }

        int higherRank = targetCard.getRank() + 1;
        if (higherRank == 14) {
            higherRank = 1;
        }

        if (cardToPlay.getRank() == smallerRank
                || cardToPlay.getRank() == higherRank) {
            return true;
        }

        return false;
    }

    public List<CardInfo> getPlayPileTopCards() {
        List<CardInfo> playPileCards = new ArrayList<CardInfo>();
        for (DeckInfo playPile : state.getPlayPiles()) {
            if (!playPile.isEmpty()) {
                playPileCards.add(CardUtil.clone(playPile.getTopCard()));
            }
        }
        return playPileCards;
    }

    public void pileChosen(int playerNumber, int pile) {
        // First come first served
        addPileToDeck(pile, playerNumber);
        addPileToDeck(1 - pile, 1 - playerNumber);
        drawPileCards();

        // If there are empty piles, fill them
        deckToEmptySlots();

        for (StressGameClient p : players) {
            p.pileChoiceResolved();
        }
        pileChoicePending = false;
    }

    public void deckToEmptySlots() {
        for (int player = 0; player < NUMBER_OF_PLAYERS; player++) {
            deckToEmptySlots(player);
        }
    }

    private void addPileToDeck(int player, int pileId) {
        DeckInfo deck = state.getPlayerState(player).getDeck();
        DeckInfo pile = state.getPlayPiles().get(pileId);

        deck.addCardsAtBottom(pile.drawAllCards());
        deck.shuffle();
    }

    public DeckCounts getDeckCounts() {
        DeckCounts dc = new DeckCounts();
        List<Integer> playerDecks = new ArrayList<Integer>();
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            playerDecks.add(state.getPlayerState(i).getDeck()
                    .getNumberOfCards());
        }
        dc.setPlayerDecks(playerDecks);

        List<Integer> playPiles = new ArrayList<Integer>();
        for (DeckInfo pile : state.getPlayPiles()) {
            playPiles.add(pile.getNumberOfCards());
        }
        dc.setPlayPiles(playPiles);
        return dc;
    }
}
