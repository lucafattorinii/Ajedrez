
package com.example.ajedrez;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;



public class Game {
    private Board board;
    private Player[] players;
    private int currentPlayerIndex;
    private List<Move> movesHistory;

    public Game(String player1Name, String player2Name) {
        board = new Board();
        players = new Player[2];
        players[0] = new Player(player1Name, true);
        players[1] = new Player(player2Name, false);
        currentPlayerIndex = 0;
        movesHistory = new ArrayList<>();
        initializeBoard();
    }

    // Constructor adicional para aceptar un tablero simulado
    public Game(String player1Name, String player2Name, Board simulatedBoard) {
        this.board = simulatedBoard;
        this.players = new Player[2];
        this.players[0] = new Player(player1Name, true);
        this.players[1] = new Player(player2Name, false);
        this.currentPlayerIndex = 0;
        this.movesHistory = new ArrayList<>();
        // No necesitas llamar a initializeBoard() aquí ya que el tablero se proporciona directamente
    }

    private void initializeBoard() {
        // Inicializar el tablero con las piezas
        // Implementar según las reglas del ajedrez
        
        // Colocar peones en las filas 2 y 7
        for (int i = 0; i < 8; i++) {
            board.setPiece(1, i, new Pawn(true)); // Peones blancos en fila 2
            board.setPiece(6, i, new Pawn(false)); // Peones negros en fila 7
        }
        
        // Colocar torres en las esquinas
        board.setPiece(0, 0, new Rook(true)); // Torre blanca en a1
        board.setPiece(0, 7, new Rook(true)); // Torre blanca en h1
        board.setPiece(7, 0, new Rook(false)); // Torre negra en a8
        board.setPiece(7, 7, new Rook(false)); // Torre negra en h8
        
        // Colocar caballos junto a las torres
        board.setPiece(0, 1, new Knight(true)); // Caballo blanco en b1
        board.setPiece(0, 6, new Knight(true)); // Caballo blanco en g1
        board.setPiece(7, 1, new Knight(false)); // Caballo negro en b8
        board.setPiece(7, 6, new Knight(false)); // Caballo negro en g8
        
        // Colocar alfiles junto a los caballos
        board.setPiece(0, 2, new Bishop(true)); // Alfil blanco en c1
        board.setPiece(0, 5, new Bishop(true)); // Alfil blanco en f1
        board.setPiece(7, 2, new Bishop(false)); // Alfil negro en c8
        board.setPiece(7, 5, new Bishop(false)); // Alfil negro en f8
        
        // Colocar reyes y reinas
        board.setPiece(0, 4, new King(true)); // Rey blanco en e1
        board.setPiece(7, 4, new King(false)); // Rey negro en e8
        board.setPiece(0, 3, new Queen(true)); // Reina blanca en d1
        board.setPiece(7, 3, new Queen(false)); // Reina negra en d8
    }

    public void makeMove(int startX, int startY, int endX, int endY) {
        // Validar coordenadas de entrada
        if (startX < 0 || startX >= 8 || startY < 0 || startY >= 8 ||
            endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            throw new IllegalArgumentException("Coordenadas no válidas.");
        }
    
        // Comprueba si el juego ha terminado.
        if (isGameOver()) {
            throw new IllegalStateException("El juego ha terminado.");
        }
    
        // Get the current player
        Player currentPlayer = getCurrentPlayer();
    
        if (currentPlayer == null) {
            throw new IllegalStateException("Jugador actual no válido.");
        }
    
        // Consigue la pieza en las coordenadas iniciales.
        Piece piece = board.getPiece(startX, startY);
        if (piece == null) {
            throw new IllegalArgumentException("No hay ninguna pieza en las coordenadas iniciales.");
        }
    
        // Comprueba si la pieza pertenece al jugador actual
        if (piece.isWhite() != currentPlayer.isWhite()) {
            throw new IllegalArgumentException("La pieza seleccionada no pertenece al jugador actual.");
        }
    
        // Comprueba si el movimiento es un enroque
        if (isCastlingMove(startX, startY, endX, endY)) {
            performCastling(startX, startY, endX, endY);
        } else {
            // Mueve la pieza y agrega el movimiento al historial
            board.movePiece(startX, startY, endX, endY);
            movesHistory.add(new Move(startX, startY, endX, endY));
        }
    
        // Comprueba si el rey del jugador actual está en jaque mate
        if (isKingInCheckmate(currentPlayer.isWhite())) {
            System.out.println("¡Jaque mate! " + currentPlayer.getName() + " gana!");
            // Fin del juego
            return;
        }
    
        // Comprueba si el rey del jugador actual está en jaque
        if (isKingInCheck(currentPlayer.isWhite())) {
            System.out.println("¡Jaque! " + currentPlayer.getName() + " está en jaque.");
        }
    
        // Comprueba si el juego está en punto muerto
        if (isStalemate()) {
            System.out.println("¡Estancamiento! El juego es un empate.");
            // Fin del juego
            return;
        }
    
        // Comprueba si es necesario promover un peón
        checkPromotion(endX, endY);
    
        // Cambia el turno al siguiente jugador
        switchTurn();
    }
    

    public void promotePawn(int x, int y) {
        Piece pawn = board.getPiece(x, y);
        System.out.println("Tu peón ha alcanzado el fondo del tablero. Elige una pieza para promover:");
        System.out.println("1. Reina");
        System.out.println("2. Torre");
        System.out.println("3. Caballo");
        System.out.println("4. Alfil");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.close(); // Cerrar el scanner después de haber obtenido la entrada
        Piece newPiece = null;
        switch (choice) {
            case 1:
                newPiece = new Queen(pawn.isWhite());
                break;
            case 2:
                newPiece = new Rook(pawn.isWhite());
                break;
            case 3:
                newPiece = new Knight(pawn.isWhite());
                break;
            case 4:
                newPiece = new Bishop(pawn.isWhite());
                break;
            default:
                System.out.println("Opción no válida. Se promoverá a la reina por defecto.");
                newPiece = new Queen(pawn.isWhite());
                break;
        }
        // Reemplazar el peón con la nueva pieza
        board.setPiece(x, y, newPiece);
        System.out.println("¡Tu peón ha sido promovido a " + newPiece.getClass().getSimpleName() + "!");
    }
    
    
    
    
    private boolean isCastlingMove(int startX, int startY, int endX, int endY) {
        // Verificar si el movimiento es un enroque
        Piece piece = board.getPiece(startX, startY);
        if (piece instanceof King && Math.abs(startY - endY) == 2) {
            // Verificar si el enroque es válido
            if (!piece.hasMoved() && !isKingInCheck(piece.isWhite())) {
                // Verificar si no hay piezas entre el rey y la torre
                int rookY = endY > startY ? 7 : 0; // Coordenada Y de la torre
                int step = endY > startY ? 1 : -1; // Dirección de la búsqueda
                for (int i = startY + step; i != endY; i += step) {
                    if (board.getPiece(startX, i) != null) {
                        return false; // Hay una pieza en el camino
                    }
                }
                // Verificar si la torre no se ha movido
                Piece rook = board.getPiece(startX, rookY);
                return rook instanceof Rook && !rook.hasMoved();
            }
        }
        return false;
    }
    
    private void performCastling(int startX, int startY, int endX, int endY) {
        // Realizar el enroque
        // Mover el rey a su nueva posición
        board.movePiece(startX, startY, endX, endY);
        // Mover la torre a su nueva posición
        int rookStartY = endY > startY ? 7 : 0; // Coordenada Y de la torre
        int rookEndY = endY > startY ? endY - 1 : endY + 1; // Nueva coordenada Y de la torre
        board.movePiece(startX, rookStartY, startX, rookEndY);
    }
    
    private void checkPromotion(int endX, int endY) {
        Piece piece = board.getPiece(endX, endY);
        if (piece instanceof Pawn) {
            if ((piece.isWhite() && endX == 0) || (!piece.isWhite() && endX == 7)) {
                // El peón ha alcanzado el fondo del tablero
                promotePawn(endX, endY);
            }
        }
    }
    
    
    

    public boolean isGameOver() {
        return isKingInCheckmate(players[0].isWhite()) || isKingInCheckmate(players[1].isWhite()) || isStalemate();
    }

    public boolean isGameInProgress() {
        return !isGameOver();
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    public void switchTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 2;
    }

    public Player getOpponentPlayer() {
        return players[(currentPlayerIndex + 1) % 2];
    }
    

    public boolean isKingInCheckmate(boolean isWhite) {
        // Verificar si el rey está en jaque
        if (!isKingInCheck(isWhite)) {
            return false; // El rey no está en jaque mate
        }
        
        // Obtener todos los posibles movimientos del jugador en jaque
        List<Move> possibleMoves = getAllPossibleMoves(isWhite);
        
        // Para cada posible movimiento, verificar si el rey sigue en jaque después del movimiento
        for (Move move : possibleMoves) {
            // Simular el movimiento en un nuevo tablero
            Board newBoard = simulateMove(move);
            // Verificar si el rey sigue en jaque después del movimiento simulado
            if (!newBoard.isKingInCheck(isWhite)) {
                return false; // Hay al menos un movimiento que saca al rey del jaque
            }
        }
        
        // Si no hay movimientos que saquen al rey del jaque, entonces es jaque mate
        return true;
    }
    

    // Método para obtener todos los movimientos posibles para un jugador
    private List<Move> getAllPossibleMoves(boolean isWhite) {
        List<Move> possibleMoves = new ArrayList<>();

        // Iterar sobre todas las piezas en el tablero
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPiece(i, j);
                if (piece != null && piece.isWhite() == isWhite) {
                    // Obtener todos los posibles movimientos para esta pieza
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            if (board.isValidMove(i, j, x, y)) {
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
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPiece(i, j);
                if (piece != null) {
                    newBoard.setPiece(i, j, piece);
                }
            }
        }
        // Realizar el movimiento en el nuevo tablero
        newBoard.movePiece(move.getStartX(), move.getStartY(), move.getEndX(), move.getEndY());
        return newBoard;
    }

    public boolean isKingInCheck(boolean isWhite) {
        // Obtener las coordenadas del rey del color dado
        int kingX = -1;
        int kingY = -1;
        outerLoop:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPiece(i, j);
                if (piece instanceof King && piece.isWhite() == isWhite) {
                    kingX = i;
                    kingY = j;
                    break outerLoop;
                }
            }
        }
    
        // Verificar si se encontró el rey
        if (kingX == -1 || kingY == -1) {
            throw new IllegalStateException("No se encontró el rey del color dado.");
        }
    
        // Verificar si el rey del jugador opuesto está en jaque
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPiece(i, j);
                if (piece != null && piece.isWhite() != isWhite) {
                    if (piece.isValidMove(board, i, j, kingX, kingY)) {
                        return true;
                    }
                }
            }
        }
    
        return false; // El rey no está en jaque
    }
    
    
    
    
    

    public boolean isValidMove(int startX, int startY, int endX, int endY) {
        // Verificar si las coordenadas están dentro del tablero
        if (!isValidCoordinate(startX, startY) || !isValidCoordinate(endX, endY)) {
            return false;
        }
    
        // Verificar si la pieza en la casilla de inicio pertenece al jugador actual
        Piece piece = board.getPiece(startX, startY);
        Player currentPlayer = getCurrentPlayer();
        if (piece == null || piece.isWhite() != currentPlayer.isWhite()) {
            return false;
        }
    
        // Verificar si el movimiento es válido según las reglas del juego
        return board.isValidMove(startX, startY, endX, endY);
    }

    public boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
    
    public boolean isCheckmate(boolean isWhite) {
        // Verificar si el rey está en jaque
        if (!isKingInCheck(isWhite)) {
            return false; // El rey no está en jaque mate
        }
    
        // Obtener todos los posibles movimientos del jugador en jaque
        List<Move> possibleMoves = getAllPossibleMoves(isWhite);
    
        // Para cada posible movimiento, verificar si el rey sigue en jaque después del movimiento
        for (Move move : possibleMoves) {
            // Simular el movimiento en un nuevo tablero
            Board newBoard = simulateMove(move);
            // Verificar si el rey sigue en jaque después del movimiento simulado
            if (!newBoard.isKingInCheck(isWhite)) {
                return false; // Hay al menos un movimiento que saca al rey del jaque
            }
        }
    
        // Si no hay movimientos que saquen al rey del jaque, entonces es jaque mate
        return true;
    }
    
    

    public boolean isStalemate() {
        // Obtener el jugador actual
        Player currentPlayer = getCurrentPlayer();
    
        // Verificar si el jugador no tiene ninguna pieza
        if (!hasAnyPieces(currentPlayer)) {
            return true; // Es un punto muerto si el jugador no tiene ninguna pieza
        }
    
        // Inicializar las coordenadas de la pieza actual
        int i = 0;
        int j = 0;
    
        // Buscar la primera pieza del jugador actual en el tablero
        outerLoop:
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                Piece piece = board.getPiece(i, j);
                if (piece != null && piece.isWhite() == currentPlayer.isWhite()) {
                    // Se encontró una pieza del jugador actual
                    break outerLoop;
                }
            }
        }
    
        // Verificar si hay algún movimiento legal disponible para la pieza encontrada
        while (i < 8 && j < 8) {
            Piece piece = board.getPiece(i, j);
            if (piece != null && piece.isWhite() == currentPlayer.isWhite()) {
                // Obtener todos los movimientos posibles para la pieza en esta posición
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        if (isValidMove(i, j, x, y)) {
                            // Si hay al menos un movimiento válido, no hay estancamiento
                            return false;
                        }
                    }
                }
            }
    
            // Avanzar a la siguiente posición en el tablero
            j++;
            if (j >= 8) {
                j = 0;
                i++;
            }
        }
    
        // Si no se encontraron movimientos legales para el jugador actual, es un estancamiento
        return true;
    }
    
    private boolean hasAnyPieces(Player player) {
        // Iterar sobre todas las posiciones del tablero
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Verificar si hay una pieza del jugador en esta posición
                Piece piece = board.getPiece(i, j);
                if (piece != null && piece.isWhite() == player.isWhite()) {
                    return true; // El jugador tiene al menos una pieza
                }
            }
        }
        return false; // El jugador no tiene ninguna pieza
    }
    
    

    public Player[] getPlayers() {
        return players;
    }
}
