package piece;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.Board;
import piece.Piece;

public class PieceTest {

    private Piece piece;

    @Before
    public void setUp() {
        piece = new Piece(0, 0, 0); // Inicializando com valores para teste
    }

    @Test
    public void testGetX() {
        int col = 3;
        int expectedX = col * Board.SQUARE_SIZE;
        assertEquals(expectedX, piece.getX(col));
    }

    @Test
    public void testGetY() {
        int row = 4;
        int expectedY = row * Board.SQUARE_SIZE;
        assertEquals(expectedY, piece.getY(row));
    }

    @Test
    public void testGetCol() {
        int x = 150; 
        int expectedCol = x / Board.SQUARE_SIZE;
        assertEquals(expectedCol, piece.getCol(x));
    }

    @Test
    public void testGetRow() {
        int y = 200; 
        int expectedRow = y / Board.SQUARE_SIZE;
        assertEquals(expectedRow, piece.getRow(y));
    }

    @Test
    public void testIsValidSquare() {
        // Teste para uma peça que não está em uma posição ocupada
        assertTrue(piece.isValidSquare(2, 2));
    }

}
