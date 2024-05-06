package main;

import java.util.ArrayList;

import piece.Piece;

public interface GamePanelInterface {
	public void launchGame();
	public void setPieces();
	public void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target);
	public void update();
	public void simulate();
	public void changePlayer();
	public boolean isValidMove(Piece king, int colPlus, int rowPlus);
}
