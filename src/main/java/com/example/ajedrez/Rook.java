// Rook.java
package com.example.ajedrez;

public class Rook extends Piece {
    public Rook(boolean isWhite) {
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

    // Calcular la diferencia en filas y columnas
    int deltaX = endX - startX;
    int deltaY = endY - startY;

    // Movimiento válido para una torre (solo en línea recta)
    if (deltaX == 0 || deltaY == 0) {
        // Verificar si hay obstáculos en el camino
        int stepX = deltaX == 0 ? 0 : deltaX > 0 ? 1 : -1;
        int stepY = deltaY == 0 ? 0 : deltaY > 0 ? 1 : -1;
        int x = startX + stepX;
        int y = startY + stepY;
        while (x != endX || y != endY) {
            if (board.getPiece(x, y) != null) {
                return false; // Hay un obstáculo en el camino
            }
            x += stepX;
            y += stepY;
        }
        return true; // No hay obstáculos en el camino
    }

    return false; // Movimiento inválido
}

}
