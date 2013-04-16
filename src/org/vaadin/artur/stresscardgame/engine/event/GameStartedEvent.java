/*
 * Copyright 2000-2013 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.vaadin.artur.stresscardgame.engine.event;

import java.util.Map;

import org.vaadin.artur.stresscardgame.engine.PlayerInfo;
import org.vaadin.artur.stresscardgame.engine.data.StressGameClient;

public class GameStartedEvent extends GameEvent {

    private int playerNumber;
    private Map<Integer, PlayerInfo> playerInfo;

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public void setPlayerInfo(Map<Integer, PlayerInfo> playerInfo) {
        this.playerInfo = playerInfo;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public Map<Integer, PlayerInfo> getPlayerInfo() {
        return playerInfo;
    }

    @Override
    public void fire(StressGameClient stressGameClient) {
        stressGameClient.gameStarted(this);
    }

}
