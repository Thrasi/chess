package appletChess;

//import javax.imageio.ImageIO;
import graphics.BoardPanel;

import javax.swing.JApplet;

import applicationChess.GraphicalChessGame;

import java.awt.*;


import chess.*;


public class chessApplet extends JApplet {// implements ActionListener{
	private ChessGame game;
	public void init() {
		this.game = new GraphicalChessGame();
		Image darkSquareImg = null;
		Image lightSquareImg = null;
		Image[] pieceImages = new Image[12];
		
		String[] images = {"whitePawn.png","blackPawn.png",//
						"whiteRook.png","blackRook.png",//
						"whiteKnight.png","blackKnight.png",//
						"whiteBishop.png","blackBishop.png",//
						"whiteQueen.png","blackQueen.png",//
						"whiteKing.png","blackKing.png"};
		
		
		// scaling the squares makes the graphics much faster
		
		darkSquareImg = getImage(getCodeBase(),"darkSquare.png");
		darkSquareImg = GraphicalChessGame.getScaledImage(darkSquareImg,60,60);
		lightSquareImg = getImage(getCodeBase(),"lightSquare.png");
		lightSquareImg = GraphicalChessGame.getScaledImage(lightSquareImg,60,60);
		int index = 0;
		for (String img : images) {
			Image pieceImg = getImage(getCodeBase(),img);
			pieceImg = GraphicalChessGame.getScaledImage(pieceImg,60,60);
			pieceImages[index++] = pieceImg;
		}
		
		BoardPanel panel = new BoardPanel(game, pieceImages, darkSquareImg, lightSquareImg);
		panel.setPreferredSize(new Dimension(480,480));
		validate();
		repaint();
	}
}