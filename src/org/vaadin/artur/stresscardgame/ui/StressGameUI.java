package org.vaadin.artur.stresscardgame.ui;

import javax.servlet.annotation.WebServlet;

import org.vaadin.artur.geoip.GeoIP;
import org.vaadin.artur.stresscardgame.engine.PlayerInfo;
import org.vaadin.artur.stresscardgame.engine.StressGameEngine;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Theme("stresscardgametheme")
@Push
public class StressGameUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = StressGameUI.class, widgetset = "org.vaadin.artur.stresscardgame.widgetset.StresscardgameWidgetset")
    public static class Servlet extends VaadinServlet {
    }

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