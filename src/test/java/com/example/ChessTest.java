package com.example;

import com.example.ajedrez.*;


import org.junit.Test;
import static org.junit.Assert.*;

public class ChessTest {
    @Test
    public void testPieceMovement() {
        // Prueba el movimiento de las piezas
        Game game = new Game("Player1", "Player2");
        // Realiza algunos movimientos y verifica si son válidos
        assertTrue(game.isValidMove(1, 0, 3, 0)); // Movimiento válido para el peón
        assertFalse(game.isValidMove(1, 0, 2, 0)); // Movimiento inválido para el peón
        assertTrue(game.isValidMove(0, 1, 2, 2)); // Movimiento válido para el alfil
        assertFalse(game.isValidMove(0, 1, 3, 3)); // Movimiento inválido para el alfil
        // Agrega más pruebas de movimiento para otras piezas
    }

    
    @Test
    public void testStalemate() {
        // Prueba el estancamiento
        Game game = new Game("Player1", "Player2");
        // Configura un tablero de prueba donde no hay movimientos posibles para uno de los jugadores
        // y verifica si se detecta correctamente el estancamiento
        assertFalse(game.isStalemate()); // No hay estancamiento
        // Agrega más pruebas con tableros configurados para diferentes escenarios de estancamiento
    }
    
    @Test
    public void testInvalidMove() {
        // Prueba movimientos inválidos
        Game game = new Game("Player1", "Player2");
        // Intenta realizar movimientos inválidos y verifica si se detectan correctamente
        assertFalse(game.isValidMove(0, 0, 2, 2)); // Movimiento inválido, no hay pieza en la posición inicial
        assertFalse(game.isValidMove(1, 0, 5, 0)); // Movimiento inválido, intento de mover demasiadas casillas con el peón
        // Agrega más pruebas de movimientos inválidos para cubrir diferentes casos
    }
}
