// Player.java
package com.example.ajedrez;

public class Player {
    private boolean isWhite;
    private String name;

    public Player(String name, boolean isWhite) {
        this.name = name;
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public String getName() {
        return name;
    }
}
