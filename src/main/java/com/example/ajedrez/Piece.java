// Piece.java
package com.example.ajedrez;

public abstract class Piece {
    private boolean isWhite;
    
    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    private boolean moved;

    public Piece() {
        this.moved = false;
    }

    public boolean hasMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    // Método abstracto para validar movimientos de piezas
    public abstract boolean isValidMove(Board board, int startX, int startY, int endX, int endY);
}
