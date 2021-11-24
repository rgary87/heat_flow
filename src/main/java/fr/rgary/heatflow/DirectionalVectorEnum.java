package fr.rgary.heatflow;

import fr.rgary.heatflow.trigonometry.Vector;

import javax.annotation.PostConstruct;

public enum DirectionalVectorEnum {
    UP_LEFT(new Vector(-1, -1)),
    UP(new Vector(-1, 0)),
    UP_RIGHT(new Vector(-1, 1)),
    RIGHT(new Vector(0, 1)),
    BOTTOM_RIGHT(new Vector(1, 1)),
    BOTTOM(new Vector(1, 0)),
    BOTTOM_LEFT(new Vector(1, -1)),
    LEFT(new Vector(0, -1));

    static {
        UP_LEFT.opposite = BOTTOM_RIGHT;
        UP.opposite = BOTTOM;
        UP_RIGHT.opposite = BOTTOM_LEFT;
        RIGHT.opposite = LEFT;
        BOTTOM_RIGHT.opposite = UP_LEFT;
        BOTTOM.opposite = UP;
        BOTTOM_LEFT.opposite = UP_RIGHT;
        LEFT.opposite = RIGHT;
    }

    private Vector vector;
    private DirectionalVectorEnum opposite;

    DirectionalVectorEnum(Vector vector) {
        this.vector = vector;
        this.opposite = opposite;
    }

    public Vector getVector() {
        return vector;
    }

    public DirectionalVectorEnum getOpposite() {
        return opposite;
    }
}
