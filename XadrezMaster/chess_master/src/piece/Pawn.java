package piece;

import main.GamePanel;

public class Pawn extends Piece{

	public Pawn(int color, int col, int row) {
		super(color, col, row);
		
		if(color == GamePanel.WHITE) {
			image = getImage("/piece/w_pawn_1x_ns");
		}
		else {
			image = getImage("/piece/b_pawn_1x_ns");
		}
	}

}
