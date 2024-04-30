package piece;

import main.GamePanel;

public class Rook extends Piece{

	public Rook(int color, int col, int row) {
		super(color, col, row);
		
		if(color == GamePanel.WHITE) {
			image = getImage("/piece/w_rook_1x_ns");
		}
		else {
			image = getImage("/piece/b_rook_1x_ns");
		}

	}

}
