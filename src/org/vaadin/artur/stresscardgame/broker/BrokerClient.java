package org.vaadin.artur.stresscardgame.broker;

import org.vaadin.artur.stresscardgame.engine.StressGameEngine;

public interface BrokerClient {

    public void gameLaunching(StressGameEngine gameEngine);

}
