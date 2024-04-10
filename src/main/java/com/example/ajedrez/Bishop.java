package com.example.ajedrez;

public class Bishop extends Piece {
    public Bishop(boolean isWhite) {
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

        // Calcular la diferencia entre las coordenadas de inicio y fin
        int deltaX = Math.abs(endX - startX);
        int deltaY = Math.abs(endY - startY);

        // Verificar si el movimiento es diagonal (mismo cambio en filas y columnas)
        return deltaX == deltaY;
    }
}
