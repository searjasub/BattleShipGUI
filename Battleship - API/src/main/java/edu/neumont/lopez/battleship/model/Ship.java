package edu.neumont.lopez.battleship.model;

import edu.neumont.lopez.battleship.enumeration.Ships;

public class Ship {
    private final String name;
    private final int size;

    Ship(Ships ship) {
        this.name = ship.getName();
        this.size = ship.getSize();
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
