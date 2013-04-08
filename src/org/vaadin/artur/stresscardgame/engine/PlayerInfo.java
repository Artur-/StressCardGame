package org.vaadin.artur.stresscardgame.engine;

import java.io.Serializable;

import org.vaadin.artur.geoip.GeoIP.Location;

public class PlayerInfo implements Serializable {

    private String name;
    private Location location;

    public PlayerInfo(String name, Location location) {
        super();
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

}
