//Board.java
package com.example.ajedrez;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private Piece[][] pieces;

    public Board() {
        pieces = new Piece[8][8];
        // Inicializar el tablero con las piezas
    }

    public Piece getPiece(int x, int y) {
        return pieces[x][y];
    }

    public void setPiece(int x, int y, Piece piece) {
        pieces[x][y] = piece;
    }

    // Método para validar si una casilla está ocupada
    public boolean isSquareOccupied(int x, int y) {
        return pieces[x][y] != null;
    }

    // Método para mover una pieza en el tablero
    public void movePiece(int startX, int startY, int endX, int endY) {
        Piece piece = getPiece(startX, startY);
        setPiece(endX, endY, piece);
        setPiece(startX, startY, null);
    }

    // Método para validar si el movimiento es legal
    public boolean isValidMove(int startX, int startY, int endX, int endY) {
        // Validar si las coordenadas están dentro del tablero
        if (!isValidCoordinate(startX, startY) || !isValidCoordinate(endX, endY)) {
            return false;
        }

        // Validar si hay una pieza en la casilla de inicio
        Piece piece = getPiece(startX, startY);
        if (piece == null) {
            return false;
        }

        // Validar el movimiento según las reglas de la pieza
        return piece.isValidMove(this, startX, startY, endX, endY);
    }

    // Método para validar si las coordenadas están dentro del tablero
    public boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    // Método para imprimir el tablero en la consola
    public void printBoard() {
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                Piece piece = pieces[i][j];
                System.out.print(piece != null ? piece.getClass().getSimpleName().charAt(0) : ".");
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    // Método para verificar si el rey está en jaque
    public boolean isKingInCheck(boolean isWhite) {
        // Obtener la posición del rey
        int kingX = -1, kingY = -1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = getPiece(i, j);
                if (piece instanceof King && piece.isWhite() == isWhite) {
                    kingX = i;
                    kingY = j;
                    break;
                }
            }
        }

        if (kingX == -1 || kingY == -1) {
            // No se encontró el rey, algo está mal
            return false;
        }

        // Verificar si alguna pieza enemiga puede atacar al rey
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = getPiece(i, j);
                if (piece != null && piece.isWhite() != isWhite && isValidMove(i, j, kingX, kingY)) {
                    return true;
                }
            }
        }

        return false;
    }

    // Método para verificar si el rey está en jaque mate
    public boolean isKingInCheckmate(boolean isWhite) {
        // Si el rey no está en jaque, no puede estar en jaque mate
        if (!isKingInCheck(isWhite)) {
            return false;
        }

        // Buscar todas las posibles jugadas del jugador en jaque
        List<Move> possibleMoves = getAllPossibleMoves(isWhite);

        // Para cada posible movimiento, verificar si el rey sigue en jaque
        for (Move move : possibleMoves) {
            Board newBoard = simulateMove(move);
            if (!newBoard.isKingInCheck(isWhite)) {
                return false;
            }
        }

        return true;
    }

    // Método para obtener todos los movimientos posibles para un jugador
    private List<Move> getAllPossibleMoves(boolean isWhite) {
        List<Move> possibleMoves = new ArrayList<>();

        // Iterar sobre todas las piezas en el tablero
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = getPiece(i, j);
                if (piece != null && piece.isWhite() == isWhite) {
                    // Obtener todos los posibles movimientos para esta pieza
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            if (isValidMove(i, j, x, y)) {
                                possibleMoves.add(new Move(i, j, x, y));
                            }
                        }
                    }
                }
            }
        }

        return possibleMoves;
    }

    // Método para simular un movimiento en un tablero duplicado
    private Board simulateMove(Move move) {
        Board newBoard = new Board();
        // Copiar el estado del tablero actual
        for (int i = 0; i < 8; i++) {
            System.arraycopy(this.pieces[i], 0, newBoard.pieces[i], 0, 8);
        }
        // Realizar el movimiento en el nuevo tablero
        newBoard.movePiece(move.getStartX(), move.getStartY(), move.getEndX(), move.getEndY());
        return newBoard;
    }
}
