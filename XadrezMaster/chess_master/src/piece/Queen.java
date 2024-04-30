package piece;

import main.GamePanel;

public class Queen extends Piece{

	public Queen(int color, int col, int row) {
		super(color, col, row);
		
		if(color == GamePanel.WHITE) {
			image = getImage("/piece/w_queen_1x_ns");
		}
		else {
			image = getImage("/piece/b_queen_1x_ns");
		}

	}

}
