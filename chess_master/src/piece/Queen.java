package piece;

import main.GamePanel;

public class Queen extends Piece {

	public Queen(int color, int col, int row) {
		super(color, col, row);
		
		type = Type.QUEEN;

		if (color == GamePanel.WHITE) {
			image = getImage("/piece/w_queen_1x_ns");
		} else {
			image = getImage("/piece/b_queen_1x_ns");
		}

	}

	public boolean canMove(int targetCol, int targetRow) {
		if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false) {
			
			//vertical and horizontal
			if(targetCol == preCol || targetRow == preRow) {
				if(isValidSquare(targetCol, targetRow) && pieceIsOnStreightLine(targetCol, targetRow) == false) {
					return true;
				}
			}
			
			//diagonal
			if(Math.abs(targetCol - preCol) == Math.abs(targetRow) - preRow) {
				if(isValidSquare(targetCol, targetRow) && pieceIsOnDiagonalLine(targetCol, targetRow) == false) {
					return true;
				}
			}
		}
		return false;
	}

}
