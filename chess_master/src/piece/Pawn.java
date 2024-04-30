package piece;

import main.GamePanel;

public class Pawn extends Piece{

	public Pawn(int color, int col, int row) {
		super(color, col, row);
		
		type = Type.PAWN;
		
		if(color == GamePanel.WHITE) {
			image = getImage("/piece/w_pawn_1x_ns");
		}
		else {
			image = getImage("/piece/b_pawn_1x_ns");
		}
	}

	public boolean canMove(int targetCol, int targetRow) {
		
		//define the value based in its color
		int moveValue;
		if(color == GamePanel.WHITE) {
			moveValue = -1;
		}
		else {
			moveValue = 1;
		}
		
		//checking hitting piece
		hittingP = getHittingP(targetCol, targetCol);
		
		//1 square move
		if(targetCol == preCol && targetRow == preRow + moveValue && hittingP == null) {
			return true;
		}
		
		//2 squares move
		if(targetCol == preCol && targetRow == preRow + moveValue*2 && hittingP == null && moved == false &&
				pieceIsOnStreightLine(targetCol, targetRow) == false) {
			return true;
		}
		
		//diagonal move and capture
		if(Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue && hittingP != null &&
				hittingP.color != color) {
			return true;
		}
		
		//en passant
		if(Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue) {
			for(Piece piece : GamePanel.simPieces) {
				if(piece.col == targetCol && piece.row == preRow && piece.twoStepped == true) {
					hittingP = piece;
					return true;
				}
			}
		}
		
		return false;
		
	}
}
