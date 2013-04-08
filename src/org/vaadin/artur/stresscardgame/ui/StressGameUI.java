package org.vaadin.artur.stresscardgame.ui;

import org.vaadin.artur.geoip.GeoIP;
import org.vaadin.artur.stresscardgame.engine.PlayerInfo;
import org.vaadin.artur.stresscardgame.engine.StressGameEngine;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@Theme("stresscardgametheme")
public class StressGameUI extends UI {

    private StressGameLayout gameLayout;

    @Override
    protected void init(VaadinRequest request) {
        gameLayout = new StressGameLayout();
        setContent(gameLayout);
        waitForOpponent();
    }

    private void waitForOpponent() {
        addWindow(new InitialWindow(this));
    }

    public void opponentJoined(StressGameEngine stressGameEngine, String name) {
        StressGameClientImpl stressGamePlayer = new StressGameClientImpl(
                gameLayout, stressGameEngine);
        stressGameEngine.registerClient(stressGamePlayer, new PlayerInfo(name,
                GeoIP.getLocation(getPage().getWebBrowser().getAddress())));
    }

}