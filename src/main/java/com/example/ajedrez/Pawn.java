// Pawn.java
package com.example.ajedrez;

public class Pawn extends Piece {
    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
public boolean isValidMove(Board board, int startX, int startY, int endX, int endY) {
    // Verificar si las coordenadas de inicio y fin están dentro del tablero
    if (!board.isValidCoordinate(startX, startY) || !board.isValidCoordinate(endX, endY)) {
        return false;
    }

    // Verificar si la casilla de inicio y fin son la misma
    if (startX == endX && startY == endY) {
        return false;
    }

    // Verificar si hay una pieza en la casilla de inicio
    Piece startPiece = board.getPiece(startX, startY);
    if (startPiece == null) {
        return false;
    }

    // Verificar si la casilla de destino está ocupada por una pieza del mismo color
    Piece endPiece = board.getPiece(endX, endY);
    if (endPiece != null && endPiece.isWhite() == startPiece.isWhite()) {
        return false;
    }

    // Calcular la dirección del movimiento (hacia arriba para las blancas, hacia abajo para las negras)
    int direction = startPiece.isWhite() ? 1 : -1;

    // Si el peón se está moviendo hacia adelante
    if (startY == endY) {
        // Movimiento normal de un peón: una casilla hacia adelante
        if (endX == startX + direction && !board.isSquareOccupied(endX, endY)) {
            return true;
        }
        // Movimiento inicial de un peón: dos casillas hacia adelante desde su posición inicial
        if (startX == (startPiece.isWhite() ? 1 : 6) && endX == startX + (2 * direction)) {
            // Verificar si hay obstáculos en el camino
            if (!board.isSquareOccupied(startX + direction, endY) && !board.isSquareOccupied(endX, endY)) {
                return true;
            }
        }
    }
    // Si el peón está capturando una pieza en diagonal
    else if (Math.abs(endY - startY) == 1) {
        // El peón solo puede capturar en diagonal hacia adelante
        if (endX == startX + direction && endPiece != null && endPiece.isWhite() != startPiece.isWhite()) {
            return true;
        }
    }

    return false;
}

}

