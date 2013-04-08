package org.vaadin.artur.stresscardgame.ui;

import org.vaadin.artur.stresscardgame.broker.BrokerClient;
import org.vaadin.artur.stresscardgame.broker.GameBroker;
import org.vaadin.artur.stresscardgame.engine.StressGameEngine;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class InitialWindow extends Window {

    private static final String RULES = "<br/><br/>The simple rules:"
            + "<p>"
            + "<ul>"
            + "<li>Both players have 26 cards. You need to get rid of your cards.</li>"
            + "<li>You have 4 cards you can play. There are two piles to play cards on.</li>"
            + "</ul><ul>"
            + "<li>A card can be played if its rank is one higher or lower than the top card in a pile,<br/> "
            + "e.g. 4 can be played on a 3 or a 5. An ace can be played on a 2 or a king.</li>"
            + "<li>When you play a card, a new one will be drawn from your deck.</li>"
            + "<li>If neither player can play any card, a new card will be drawn from each persons deck to the play pile.</li>"
            + "</ul><ul>"
            + "<li>If both play piles have a top card with the same rank, you cannot play any card. Choose a pile to add to your deck (choose the smaller one).</li>"
            + "<li>If neither player can play and both players' deck is empty, you also choose a pile.</li>"
            + "</ul><ul>" + "<li>Get rid of your cards to win.</li>" + ""
            + "</p><p>" + "Good luck!" + "</p>";
    private TextField nameField = new TextField("Enter your name to begin");
    private Label waiting;

    public InitialWindow(final StressGameUI stresscardgameUI) {
        super("Welcome");
        setModal(true);
        center();
        setClosable(false);
        setResizable(false);

        setStyleName(Reindeer.WINDOW_BLACK);
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
        layout.addComponent(nameField);
        layout.setComponentAlignment(nameField, Alignment.MIDDLE_CENTER);
        waiting = new Label("Waiting for opponent...");
        waiting.setSizeUndefined();
        layout.addComponent(waiting, 1);
        layout.setComponentAlignment(waiting, Alignment.MIDDLE_CENTER);
        waiting.setVisible(false);

        layout.addComponent(new Label(RULES, ContentMode.HTML));
        nameField.setImmediate(true);
        nameField.setWidth("20em");
        nameField.focus();
        nameField.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                nameField.setEnabled(false);
                waiting.setVisible(true);

                GameBroker.register(new BrokerClient() {
                    public void gameLaunching(
                            final StressGameEngine stressGameEngine) {
                        getUI().runSafely(new Runnable() {
                            @Override
                            public void run() {
                                stresscardgameUI.opponentJoined(
                                        stressGameEngine, nameField.getValue());
                                close();
                            }
                        });
                    }
                });

            }
        });
    }
}
