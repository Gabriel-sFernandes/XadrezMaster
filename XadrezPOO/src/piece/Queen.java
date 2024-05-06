package piece;

import main.GamePanel;
import main.Type;

public class Queen extends Piece{
    public Queen(int col, int row, int color) {
        super(col, row, color);

        type = Type.QUEEN;

        if (color == GamePanel.WHITE) {
            image = getImage("/piece/w-queen");
        }else {
            image = getImage("/piece/b-queen");
        }
    }

    public boolean canMove(int targetCol, int targetRow){

        if (isWithThinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)){
            //Vertical & Horizontal
            if (targetCol == preCol || targetRow == preRow){
                if (isValidSquare(targetCol,targetRow) && !pieceIsOnStraightLine(targetCol, targetRow)){
                    return true;
                }
            }

            //Diagonal
            if (Math.abs(targetCol - preCol) == Math.abs(targetRow - preRow)){
                if (isValidSquare(targetCol, targetRow) && !pieceIsOnDiagonalLine(targetCol, targetRow)){
                    return true;
                }
            }
        }
        return false;
    }
}
