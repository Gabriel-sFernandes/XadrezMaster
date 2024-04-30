package piece;

import main.GamePanel;

public class King extends Piece{

	public King(int color, int col, int row) {
		super(color, col, row);
		
		if(color == GamePanel.WHITE) {
			image = getImage("/piece/w_king_1x_ns");
		}
		else {
			image = getImage("/piece/b_king_1x_ns");
		}

	}

}
