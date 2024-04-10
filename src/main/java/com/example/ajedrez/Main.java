package com.example.ajedrez;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Crear una nueva instancia de la clase Game con los nombres de los jugadores
        Game game = new Game("Player1", "Player2");
        
        // Crear un objeto Scanner para leer la entrada del usuario desde la consola
        Scanner scanner = new Scanner(System.in);

        // Bucle principal del juego
        while (game.isGameInProgress()) {
            // Mostrar el tablero actual
            game.getBoard().printBoard();

            // Obtener el jugador actual
            Player currentPlayer = game.getCurrentPlayer();
            System.out.println("Turno de " + currentPlayer.getName() + ":");

            // Solicitar al jugador las coordenadas del movimiento
            System.out.print("Ingrese coordenadas de inicio (fila columna): ");
            int startX = scanner.nextInt();
            int startY = scanner.nextInt();
            System.out.print("Ingrese coordenadas de destino (fila columna): ");
            int endX = scanner.nextInt();
            int endY = scanner.nextInt();

            // Intentar realizar el movimiento
            try {
                game.makeMove(startX, startY, endX, endY);
            } catch (IllegalArgumentException e) {
                // Si se produce un error, mostrar un mensaje de error y continuar con el bucle
                System.out.println("Error: " + e.getMessage());
                continue; // Solicitar otro movimiento
            }

            // Cambiar al siguiente turno
            game.switchTurn();
        }

        // Mostrar el resultado final del juego
        if (game.isKingInCheckmate(true)) {
            // Si hay jaque mate para el jugador blanco, mostrar el mensaje correspondiente
            System.out.println("¡Jaque mate! " + game.getPlayers()[1].getName() + " gana.");
        } else if (game.isKingInCheckmate(false)) {
            // Si hay jaque mate para el jugador negro, mostrar el mensaje correspondiente
            System.out.println("¡Jaque mate! " + game.getPlayers()[0].getName() + " gana.");
        } else {
            // Si no hay jaque mate para ninguno de los jugadores, mostrar que el juego ha terminado en empate
            System.out.println("¡Empate! El juego ha terminado en tablas.");
        }
        
        // Cerrar el objeto Scanner después de utilizarlo
        scanner.close();
    }
}
