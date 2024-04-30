package piece;

import main.GamePanel;

public class Knight extends Piece {

	public Knight(int color, int col, int row) {
		super(color, col, row);

		type = Type.KNIGHT;
		
		if (color == GamePanel.WHITE) {
			image = getImage("/piece/w_knight_1x_ns");
		} else {
			image = getImage("/piece/b_knight_1x_ns");
		}

	}

	public boolean canMove(int targetCol, int targetRow) {

		if (isWithinBoard(targetCol, targetRow)) {
			//knight can move if its movement ratio of col and row is 1:2 or 2:1
			if(Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow) == 2) {
				if(isValidSquare(targetCol, targetRow)) {
					return true;
				}
			}
		}

		return false;
	}

}
