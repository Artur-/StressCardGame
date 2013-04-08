package org.vaadin.artur.stresscardgame.ui;

import java.util.List;
import java.util.logging.Logger;

import org.vaadin.artur.playingcards.Card;
import org.vaadin.artur.playingcards.Card.CardTransferable;
import org.vaadin.artur.playingcards.CardPile;
import org.vaadin.artur.playingcards.Deck;
import org.vaadin.artur.playingcards.client.ui.Suite;
import org.vaadin.artur.stresscardgame.engine.data.CardInfo;
import org.vaadin.artur.stresscardgame.engine.util.CardUtil;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class StressGameLayout extends AbsoluteLayout {

    private static final int GLOW_SIZE = 20;
    private static final int ROW_HEIGHT = 263;
    private static final int COLUMN_WIDTH = 134;
    private static final int MARGIN = 20;
    private static final String PILE_CHOICE_STYLE = "choosePile";
    private static final int OWN_PILES = 4;
    private static final int GAME_PILES = 2;

    private Deck ownDeck;
    private Deck opponentDeck;
    private CardPile[] ownCards;
    private CardPile[] opponentCards;
    private CardPile ownGamePile;
    private CardPile opponentGamePile;
    private CardPlayListener cardPlayListener;

    private Label pileGlow = new Label();
    private PileChoiceListener pileChoiceListener = null;
    private Label otherPlayerInfo = new Label("", ContentMode.PREFORMATTED);
    private Label ownDeckCount;
    private Label opponentDeckCount;
    private Label ownGamePileCount;
    private Label opponentGamePileCount;

    public StressGameLayout() {
        pileGlow.setStyleName("glow");
        addGlow(pileGlow);
        otherPlayerInfo.setStyleName("playerName");

        ownDeck = new Deck();
        ownDeck.setShowTopCard(false);
        opponentDeck = new Deck();
        opponentDeck.setShowTopCard(false);

        ownCards = new CardPile[OWN_PILES];
        opponentCards = new CardPile[OWN_PILES];

        for (int i = 0; i < OWN_PILES; i++) {
            ownCards[i] = new CardPile();
            ownCards[i].setDraggable(true);

            opponentCards[i] = new CardPile();
            opponentCards[i].setDraggable(false);
        }

        addCard(opponentDeck, 0, 0);
        opponentDeckCount = createDeckCount(0, 0);
        addCard(ownDeck, 2, OWN_PILES + 1);
        ownDeckCount = createDeckCount(2, OWN_PILES + 1);

        for (int i = 0; i < OWN_PILES; i++) {
            addCard(opponentCards[i], 0, i + 1);
            addCard(ownCards[i], 2, i + 1);
        }

        ownGamePile = new CardPile();
        ownGamePile.addListener(new ClickListener() {
            @Override
            public void click(ClickEvent event) {
                pileClicked(0);
            }
        });

        opponentGamePile = new CardPile();
        opponentGamePile.addListener(new ClickListener() {
            @Override
            public void click(ClickEvent event) {
                pileClicked(1);
            }
        });

        addCard(ownGamePile, 1, 2);
        ownGamePileCount = createDeckCount(1, 2);
        addCard(opponentGamePile, 1, 3);
        opponentGamePileCount = createDeckCount(1, 3);

        addCard(otherPlayerInfo, 0, 5);
    }

    private Label createDeckCount(int row, int column) {
        Label l = new Label();
        l.setSizeUndefined();
        l.addStyleName("deckCount");
        addCard(l, row, column);
        ComponentPosition p = getPosition(l);
        p.setTopValue(p.getTopValue() + Card.HEIGHT);

        return l;
    }

    protected void pileClicked(int pile) {
        if (pileChoiceListener != null) {
            pileChoiceListener.pileChosen(pile);
        }
    }

    private void addGlow(Component glow) {
        int top = MARGIN + ROW_HEIGHT * 1 - GLOW_SIZE / 2;
        int left = MARGIN + COLUMN_WIDTH * 2 - GLOW_SIZE / 2;

        addComponent(glow, "top: " + top + "px; left: " + left + "px;");
        pileGlow.setWidth((Card.WIDTH + COLUMN_WIDTH + GLOW_SIZE) + "px");
        pileGlow.setHeight((Card.HEIGHT + GLOW_SIZE) + "px");

    }

    public void addCard(Component c, int row, int column) {
        int top = MARGIN + ROW_HEIGHT * row;
        int left = MARGIN + COLUMN_WIDTH * column;
        addComponent(c, "top: " + top + "px; left: " + left + "px;");
    }

    public class GamePileDropHandler implements DropHandler {

        public GamePileDropHandler() {
        }

        @Override
        public void drop(DragAndDropEvent event) {
            CardPile gamePile = (CardPile) event.getTargetDetails().getTarget();
            CardTransferable transferable = (CardTransferable) event
                    .getTransferable();

            CardPile sourcePile = (CardPile) transferable.getSourceComponent();

            CardInfo cardInfo = CardUtil
                    .createCardInfo(sourcePile.getTopCard());
            int pile = gamePile == ownGamePile ? 0 : 1;
            getLogger().info(
                    "Dropped " + cardInfo + " on pile " + pile
                            + " with top card "
                            + CardUtil.createCardInfo(gamePile.getTopCard()));
            cardPlayListener.playCard(cardInfo, pile);

        }

        @Override
        public AcceptCriterion getAcceptCriterion() {
            return AcceptAll.get();
        }

    }

    public void addCardPlayListener(CardPlayListener cardPlayListener) {
        this.cardPlayListener = cardPlayListener;
        ownGamePile.setDropHandler(new GamePileDropHandler());
        opponentGamePile.setDropHandler(new GamePileDropHandler());
    }

    public void refreshOwnVisibleCards(List<CardInfo> visibleCards) {
        for (int i = 0; i < visibleCards.size(); i++) {
            Card card = ownCards[i].getTopCard();
            if (card == null) {
                card = new Card(Suite.CLUBS, 1);
                ownCards[i].addCard(card);
            }

            CardInfo newCard = visibleCards.get(i);

            if (!CardUtil.equals(card, newCard)) {
                CardUtil.update(card, newCard);
            }
            ownCards[i].requestRepaint();
        }
        for (int i = visibleCards.size(); i < 4; i++) {
            ownCards[i].removeAllCards();
            ownCards[i].requestRepaint();
        }
    }

    public void refreshOpponentVisibleCards(List<CardInfo> visibleCards) {
        for (int i = 0; i < visibleCards.size(); i++) {
            Card card = opponentCards[i].getTopCard();
            if (card == null) {
                card = new Card(Suite.CLUBS, 1);
                opponentCards[i].addCard(card);
            }

            CardInfo newCard = visibleCards.get(i);

            if (!CardUtil.equals(card, newCard)) {
                CardUtil.update(card, newCard);
            }
            opponentCards[i].requestRepaint();
        }
        for (int i = visibleCards.size(); i < 4; i++) {
            opponentCards[i].removeAllCards();
            opponentCards[i].requestRepaint();
        }
    }

    private static Logger getLogger() {
        return Logger.getLogger(StressGameLayout.class.getName());
    }

    public void refreshPlayPiles(List<CardInfo> playPileTopCards) {
        if (ownGamePile.getTopCard() == null) {
            ownGamePile.addCard(new Card(Suite.CLUBS, 1));
        }
        if (opponentGamePile.getTopCard() == null) {
            opponentGamePile.addCard(new Card(Suite.CLUBS, 1));
        }

        if (!playPileTopCards.isEmpty()) {
            CardUtil.update(ownGamePile.getTopCard(), playPileTopCards.get(0));
            CardUtil.update(opponentGamePile.getTopCard(),
                    playPileTopCards.get(1));
        } else {
            ownGamePile.removeAllCards();
            opponentGamePile.removeAllCards();
        }
        ownGamePile.requestRepaint();
        opponentGamePile.requestRepaint();

    }

    /**
     * Toggles UI into a pile mode selection state where the user has to select
     * a pile. Once the pile has been selected, the UI returns to normal mode.
     * 
     * @param pileChoiceListener
     */
    public void setChoosePileMode(PileChoiceListener pileChoiceListener) {
        boolean pileChoiceMode = (pileChoiceListener != null);
        this.pileChoiceListener = pileChoiceListener;
        setCardsEnabled(!pileChoiceMode);

        if (pileChoiceMode) {
            addStyleName(PILE_CHOICE_STYLE);
        } else {
            removeStyleName(PILE_CHOICE_STYLE);
        }

    }

    private void setCardsEnabled(boolean enabled) {
        for (CardPile pile : ownCards) {
            pile.setEnabled(enabled);
            pile.setDraggable(enabled);
        }

        ownDeck.setEnabled(enabled);
        for (CardPile pile : opponentCards) {
            pile.setEnabled(enabled);
            pile.setDraggable(enabled);
        }

        opponentDeck.setEnabled(enabled);
    }

    public void setOtherPlayerText(String text) {
        otherPlayerInfo.setValue(text);
    }

    public void setDeckCounts(int cardsInOwnDeck, int cardsInOpponentDeck,
            int cardsInOwnGamePile, int cardsInOpponentGamePile) {
        ownDeckCount.setValue(cardsInOwnDeck + " cards");
        opponentDeckCount.setValue(cardsInOpponentDeck + " cards");
        ownGamePileCount.setValue(cardsInOwnGamePile + " cards");
        opponentGamePileCount.setValue(cardsInOpponentGamePile + " cards");

    }
}
