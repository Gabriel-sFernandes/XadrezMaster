package piece;

import main.GamePanel;

public class Rook extends Piece{

	public Rook(int color, int col, int row) {
		super(color, col, row);
		
		type = Type.ROOK;
		
		if(color == GamePanel.WHITE) {
			image = getImage("/piece/w_rook_1x_ns");
		}
		else {
			image = getImage("/piece/b_rook_1x_ns");
		}

	}
	
	public boolean canMove(int targetCol, int targetRow) {

		if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false) {
			//rook can move as long as either its col or row is the same
			if(targetCol == preCol || targetRow == preRow) {
				if(isValidSquare(targetCol, targetRow) && pieceIsOnStreightLine(targetCol, targetRow) == false) {
					return true;
				}
			}
		}

		return false;
	}

}
