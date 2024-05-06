package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

import piece.Bishop;
import piece.King;
import piece.Knight;
import piece.Pawn;
import piece.Piece;
import piece.Queen;
import piece.Rook;

public class GamePanel extends JPanel implements Runnable, GamePanelInterface{
    private static final long serialVersionUID = 45233574336541220L;
	public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    final int FPS = 60;
    Thread gameThread;
    Board board = new Board();
    Mouse mouse = new Mouse();

    //Pieces
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> simPieces = new ArrayList<>();
    ArrayList<Piece> promoPieces = new ArrayList<>();
    public static Piece castlingP;
    Piece activeP, checkingP;


    //Color
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentColor = WHITE;

    //Booleans
    boolean canMove;
    boolean validSquare;
    boolean promotion;
    boolean gameOver;
    boolean stalemate;

    public GamePanel() {

        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setBackground(Color.black);
        addMouseMotionListener(mouse);
        addMouseListener(mouse);

        setPieces();
//        testPromotion();
//        testIllegal();
        copyPieces(pieces, simPieces);
    }

    @Override
    public void launchGame() {

        gameThread = new Thread(this);
        gameThread.start();

    }

    @Override
    public void setPieces() {

        // WHITE TEAM
        pieces.add(new  Pawn(0, 6, WHITE));
        pieces.add(new  Pawn(1, 6, WHITE));
        pieces.add(new  Pawn(2, 6, WHITE));
        pieces.add(new  Pawn(3, 6, WHITE));
        pieces.add(new  Pawn(4, 6, WHITE));
        pieces.add(new  Pawn(5, 6, WHITE));
        pieces.add(new  Pawn(6, 6, WHITE));
        pieces.add(new  Pawn(7, 6, WHITE));
        pieces.add(new  Rook(0,7, WHITE));
        pieces.add(new  Rook(7, 7,WHITE));
        pieces.add(new  Knight(1, 7,WHITE));
        pieces.add(new  Knight(6, 7,WHITE));
        pieces.add(new  Bishop(2, 7,WHITE));
        pieces.add(new  Bishop(5, 7,WHITE));
        pieces.add(new  Queen(3, 7,WHITE));
        pieces.add(new  King(4, 7,WHITE));

        // BLACK TEAM
        pieces.add(new Pawn(0, 1,BLACK));
        pieces.add(new Pawn(1, 1,BLACK));
        pieces.add(new Pawn(2, 1,BLACK));
        pieces.add(new Pawn(3, 1,BLACK));
        pieces.add(new Pawn(4, 1,BLACK));
        pieces.add(new Pawn(5, 1,BLACK));
        pieces.add(new Pawn(6, 1,BLACK));
        pieces.add(new Pawn(7, 1,BLACK));
        pieces.add(new Rook(0, 0,BLACK));
        pieces.add(new Rook(7, 0,BLACK));
        pieces.add(new Knight(1, 0,BLACK));
        pieces.add(new Knight(6, 0,BLACK));
        pieces.add(new Bishop(2, 0,BLACK));
        pieces.add(new Bishop(5, 0,BLACK));
        pieces.add(new Queen(3, 0,BLACK));
        pieces.add(new King(4, 0,BLACK));
    }

    //Test
    public void testPromotion() {
        pieces.add(new Pawn(0,4,WHITE));
        pieces.add(new Pawn(5,6,BLACK));
    }

    public void testIllegal(){
        pieces.add(new Queen(2,2,WHITE));
        pieces.add(new King(3,4,WHITE));
        pieces.add(new King(0,3,BLACK));
        pieces.add(new Bishop(0,4,BLACK));
        pieces.add(new Queen(4,4,BLACK));

    }

    @Override
	public void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target){
        target.clear();
        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i));
        }
    }

    @Override
    public void run() {

        //Game loop
        double drawInterval = (double) 1000000000 /FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null){

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime)/drawInterval;
            lastTime = currentTime;

            if (delta >= 1){
                update();
                repaint();
                delta--;
            }
        }
    }

    @Override
    public void update(){

        if (promotion){
            promotion();
        }else if (!gameOver && !stalemate){
            // Mouse Button Pressed //
            if (mouse.pressed) {
                if (activeP == null) {
                    // If the activeP is Null, check if you can pick up a piece
                    for (Piece piece : simPieces) {
                        // if the mouse is on an all piece, pick it up as the activeP
                        if (piece.color == currentColor &&
                                piece.col == mouse.x / Board.SQUARE_SIZE &&
                                piece.row == mouse.y / Board.SQUARE_SIZE) {
                            activeP = piece;
                        }
                    }
                }else {
                    // if the player is holding a piece, simulate the move
                    simulate();
                }

            }
            // Mouse Button released
            if (!mouse.pressed){

                if (activeP != null){

                    if (validSquare){
                        // Move confirmed

                        //Update the piece list in case a piece has been captured and removed during the simulation
                        copyPieces(simPieces, pieces);
                        activeP.updatePosition();
                        if (castlingP != null){
                            castlingP.updatePosition();
                        }

                        if (isKingInCheck() && isCheckMate()){
                            gameOver = true;
                        }else if (isStalemate() && !isKingInCheck()){
                            stalemate = true;
                        }
                        else { // The game is still going on
                            if (canPromote()){
                                promotion = true;
                            }else {
                                changePlayer();
                            }
                        }


                    }else {
                        //The move is not valid so reset everything
                        copyPieces(pieces, simPieces);
                        activeP.resetPosition();
                        activeP = null;
                    }
                }
            }
        }
    }

    private void promotion() {

        if (mouse.pressed){
            for(Piece piece : promoPieces){
                if (piece.col == mouse.x/Board.SQUARE_SIZE && piece.row == mouse.y/Board.SQUARE_SIZE){
                    switch (piece.type){
                        case ROOK: simPieces.add(new Rook(activeP.col, activeP.row, currentColor)); break;
                        case KNIGHT: simPieces.add(new Knight(activeP.col, activeP.row, currentColor)); break;
                        case BISHOP: simPieces.add(new Bishop(activeP.col, activeP.row, currentColor)); break;
                        case QUEEN: simPieces.add(new Queen(activeP.col, activeP.row, currentColor)); break;
                        default: break;
                    }
                    simPieces.remove(activeP.getIndex());
                    copyPieces(simPieces, pieces);
                    activeP = null;
                    promotion = false;
                    changePlayer();
                }
            }
        }

    }

    @Override
    public void simulate() {

        canMove = false;
        validSquare = false;

        // Reset the piece list in every loop
        // This is basically for restoring the removed piece during the simulation
        copyPieces(pieces, simPieces);

        // Reset the castling piece's position
        if (castlingP != null){
            castlingP.col = castlingP.preCol;
            castlingP.x = castlingP.getX(castlingP.col);
            castlingP = null;
        }

        // if a piece is being hold, update its position
        activeP.x = mouse.x - Board.HALF_SQUARE_SIZE;
        activeP.y = mouse.y - Board.HALF_SQUARE_SIZE;
        activeP.col = activeP.getCol(activeP.x);
        activeP.row = activeP.getRow(activeP.y);

        // Check if the piece is hovering over a reachable square
        if (activeP.canMove(activeP.col, activeP.row)){

            canMove = true;

            // if hitting a piece, remove it from the list
            if(activeP.hittingP != null){
                simPieces.remove(activeP.hittingP.getIndex());
            }

            checkCastling();

            if (!isIllegal(activeP) || !opponentCanCaptureKing()){
                validSquare = true;
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        //Board
        board.draw(g2);

        //Piece
        for(Piece p : simPieces){
            p.draw(g2);
        }

        if (activeP != null){
            if (canMove){
                if (isIllegal(activeP) || opponentCanCaptureKing()){
                    g2.setColor(Color.red);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OUT, 0.7f));
                    g2.fillRect(activeP.col*Board.SQUARE_SIZE, activeP.row*Board.SQUARE_SIZE,
                            Board.SQUARE_SIZE, Board.SQUARE_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }else {
                    g2.setColor(Color.white);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OUT, 0.7f));
                    g2.fillRect(activeP.col*Board.SQUARE_SIZE, activeP.row*Board.SQUARE_SIZE,
                            Board.SQUARE_SIZE, Board.SQUARE_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }
            }
            // Draw the active piece in the end sp it won't be hidde by the board or the colored square
            activeP.draw(g2);
        }
        //Status Messages
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Book Antiqua", Font.PLAIN, 25));
        g2.setColor(Color.white);

        if (promotion){
            g2.drawString("Promover p/: " , 630, 150);
            for (Piece piece : promoPieces)
                g2.drawImage(piece.image, piece.getX(piece.col), piece.getY(piece.row),
                        Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
        }else {
            if (currentColor == WHITE){
                g2.drawString("brancas jogam", 610,490);
                if (checkingP != null && checkingP.color == BLACK){
                    g2.setColor(Color.red);
                    g2.drawString("O Rei", 610, 530);
                    g2.drawString("está em xeque!", 610, 560);
                    }
            }else {
                g2.drawString("negras jogam", 610,120);
                if (checkingP != null && checkingP.color == WHITE) {
                    g2.setColor(Color.red);
                    g2.drawString("O Rei", 610, 50);
                    g2.drawString("está em xeque!", 610, 80);
                }
            }
        }

        if (gameOver){

            String s = (currentColor == WHITE) ? "Vitória Branca" : "Vitória Negra";

            // Draw semi-transparent black rectangle
            g2.setColor(new Color(0, 0, 0, 128)); // 128 is the alpha value for 50% opacity
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Draw text message on top of the semi-transparent background
            g2.setFont(new Font("Arial", Font.BOLD, 88));
            g2.setColor(Color.green);
            g2.drawString(s,50, 300);
        }

        if (stalemate){
            // Draw semi-transparent black rectangle
            g2.setColor(new Color(0, 0, 0, 128)); // 128 is the alpha value for 50% opacity
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Draw text message on top of the semi-transparent background
            g2.setFont(new Font("Arial", Font.BOLD, 88));
            g2.setColor(Color.lightGray);
            g2.drawString("Stalemate",50, 300);
        }
    }

    @Override
    public void changePlayer(){

        if (currentColor == WHITE){
            currentColor = BLACK;
            //Reset black's two stepped status
            for (Piece piece : pieces){
                if (piece.color == BLACK){
                    piece.twoStepped = false;
                }
            }
        }else {
            currentColor = WHITE;
            //Reset white's two stepped status
            for (Piece piece : pieces){
                if (piece.color == WHITE){
                    piece.twoStepped = false;
                }
            }
        }
        activeP = null;
    }

    private void checkCastling(){

        if (castlingP != null){
            if (castlingP.col == 0) {
                castlingP.col += 3;
            }else if (castlingP.col == 7){
                castlingP.col -= 2;
            }
            castlingP.x = castlingP.getX(castlingP.col);
        }
    }

    private  boolean canPromote(){

        if (activeP.type == Type.PAWN){
            if (currentColor == WHITE && activeP.row == 0 || currentColor == BLACK && activeP.row == 7){
                promoPieces.clear();
                promoPieces.add(new Rook(9,2,currentColor));
                promoPieces.add(new Knight(9,3,currentColor));
                promoPieces.add(new Bishop(9,4,currentColor));
                promoPieces.add(new Queen(9,5,currentColor));
                return true;
            }
        }
        return false;
    }

    private boolean isKingInCheck() {

        Piece king = getKing(true);

        if (activeP.canMove(king.col, king.row)){
            checkingP = activeP;
            return true;
        }else {
            checkingP = null;
        }

        return false;
    }

    private Piece getKing(boolean opponent){

        Piece king = null;

        for (Piece piece : simPieces){
            if (opponent){
                if (piece.type == Type.KING && piece.color != currentColor){
                    king = piece;
                }
            }else {
                if (piece.type == Type.KING && piece.color == currentColor){
                    king = piece;
                }
            }
        }

        return king;
    }

    private boolean isIllegal(Piece king){

        if (king.type == Type.KING){
             for (Piece piece : simPieces){
                 if (piece != king && piece.color != king.color && piece.canMove(king.col, king.row)){
                     return true;
                 }
             }
        }
        return false;
    }

    private boolean opponentCanCaptureKing() {

        Piece king = getKing(false);

        for (Piece piece : simPieces){
            if (piece.color != king.color && piece.canMove(king.col, king.row)){
                return true;
            }
        }

        return false;
    }

    private boolean isCheckMate(){

        Piece king = getKing(true);

        if (kingCanMove(king)) {
            return false;
        } else {
            // The player still had a chance
            // Check if he can block attack with his pieces

            // Check the position of the checking piece and the king in check
            int colDiff = Math.abs(checkingP.col - king.col);
            int rowDiff = Math.abs(checkingP.row - king.row);

            if (colDiff == 0) {
                // The checking piece is attacking vertically
                if (checkingP.row < king.row) {
                    // The checking piece is above the king
                    for (int row = checkingP.row; row < king.row; row++) {
                        for (Piece piece : simPieces) {
                            if (piece != king && piece.color != currentColor &&
                                    piece.canMove(checkingP.col, row)) {
                                return false;
                            }
                        }
                    }
                }

                if (checkingP.row > king.row) {
                    // The checking piece is below the king
                    for (int row = checkingP.row; row > king.row; row--) {
                        for (Piece piece : simPieces) {
                            if (piece != king && piece.color != currentColor &&
                                    piece.canMove(checkingP.col, row)) {
                                return false;
                            }
                        }
                    }
                }

            } else if (rowDiff == 0) {
                // The checking piece is attacking horizontally
                if (checkingP.col < king.col) {
                    // The checking piece is to the left
                    for (int col = checkingP.col; col < king.row; col++) {
                        for (Piece piece : simPieces) {
                            if (piece != king && piece.color != currentColor &&
                                    piece.canMove(col, checkingP.row)) {
                                return false;
                            }
                        }
                    }
                }

                if (checkingP.col > king.col) {
                    // The checking piece is to the right
                    for (int col = checkingP.col; col > king.row; col--) {
                        for (Piece piece : simPieces) {
                            if (piece != king && piece.color != currentColor &&
                                    piece.canMove(col, checkingP.row)) {
                                return false;
                            }
                        }
                    }
                }
            } else if (colDiff == rowDiff) {
                // The checking piece is attacking diagonally
                if (checkingP.row < king.row) {
                    // The checking piece is above the king
                    if (checkingP.col < king.col) {
                        // The checking piece is in the upper left
                        for (int col = checkingP.col, row = checkingP.row; col < king.col; col++, row++) {
                            for (Piece piece : simPieces) {
                                if (piece != king && piece.color != currentColor &&
                                        piece.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }
                    }

                    if (checkingP.col > king.col) {
                        // The checking piece is in the upper right
                        for (int col = checkingP.col, row = checkingP.row; col > king.col; col--, row++) {
                            for (Piece piece : simPieces) {
                                if (piece != king && piece.color != currentColor &&
                                        piece.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }
                    }
                }

                if (checkingP.row > king.row) {
                    // The checking piece is below the king
                    if (checkingP.col < king.col) {
                        // The checking piece is in the lower left
                        for (int col = checkingP.col, row = checkingP.row; col < king.col; col++, row--) {
                            for (Piece piece : simPieces) {
                                if (piece != king && piece.color != currentColor &&
                                        piece.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }
                    }

                    if (checkingP.col > king.col) {
                        // The checking piece is in the lower right
                        for (int col = checkingP.col, row = checkingP.row; col > king.col; col--, row--) {
                            for (Piece piece : simPieces) {
                                if (piece != king && piece.color != currentColor &&
                                        piece.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            } else {
                // The checking place is knight
            }
        }

        return true;

    }

    private boolean kingCanMove(Piece king){

        // Simulate if there is a square where the king can move
        if (isValidMove(king, -1, -1)) {return true;}
        if (isValidMove(king, 0, -1)) {return true;}
        if (isValidMove(king, 1, -1)) {return true;}
        if (isValidMove(king, -1, 0)) {return true;}
        if (isValidMove(king, 1, 0)) {return true;}
        if (isValidMove(king, -1, 1)) {return true;}
        if (isValidMove(king, 0, 1)) {return true;}
        if (isValidMove(king, 1, 1)) {return true;}

        return false;
    }

    @Override
    public boolean isValidMove(Piece king, int colPlus, int rowPlus){

        boolean isValidMove = false;

        // Update the temporary King position
        king.col += colPlus;
        king.row += rowPlus;

        if (king.canMove(king.col, king.row)) {
            if (king.hittingP != null) {
                simPieces.remove(king.hittingP.getIndex());
            }

            if (isIllegal(king) == false) {
                isValidMove = true;
            }
        }

        // Reset the temporary King position
        king.resetPosition();
        copyPieces(pieces, simPieces);

        return isValidMove;
    }

    private boolean isStalemate(){
        int count = 0;
        // Count the number of piece
        for (Piece piece : simPieces){
            if (piece.color != currentColor){
                count++;
            }
        }
        // If only one piece (the King) is left
        if (count == 1) return !kingCanMove(getKing(true));

        return false;
    }
}
