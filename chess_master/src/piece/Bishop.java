package piece;

import main.GamePanel;

public class Bishop extends Piece{

	public Bishop(int color, int col, int row) {
		super(color, col, row);
		
		type = Type.BISHOP;
	
		if(color == GamePanel.WHITE) {
			image = getImage("/piece/w_bishop_1x_ns");
		}
		else {
			image = getImage("/piece/b_bishop_1x_ns");
		}

	}
	
	public boolean canMove(int targetCol, int targetRow) {

		if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false) {
			if(Math.abs(targetCol - preCol) == Math.abs(targetRow - preRow)) {
				if(isValidSquare(targetCol, targetRow)) {
					return true;
				}
			}
		}

		return false;
	}

}
