package appletChess;

import graphics.BoardPanel;

import javax.imageio.ImageIO;
import javax.swing.JApplet; 

import applicationChess.GraphicalChessGame;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import chess.*;


public class chessAppletTest extends JApplet {
	private ChessGame game;
	
	// the images are loaded differently when the applet is deployed, with getImage(getCodeBase(),name)
	private String imgPath = "/Users/magnus/Documents/su/oop/eclipsWorkspace/Chess2.0/images/";
	
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
		
		try {
			// scaling the squares makes the graphics much faster
			darkSquareImg = ImageIO.read(new File(imgPath+"darkSquare.png"));
			darkSquareImg = GraphicalChessGame.getScaledImage(darkSquareImg,60,60);
			lightSquareImg = ImageIO.read(new File(imgPath+"lightSquare.png"));
			lightSquareImg = GraphicalChessGame.getScaledImage(lightSquareImg,60,60);
			int index = 0;
			for (String img : images) {
				Image pieceImg = ImageIO.read(new File(imgPath+img));
				pieceImg = GraphicalChessGame.getScaledImage(pieceImg,60,60);
				pieceImages[index++] = pieceImg;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("images failed to load");
		}
		
		BoardPanel panel = new BoardPanel(game, pieceImages, darkSquareImg, lightSquareImg);
		panel.setPreferredSize(new Dimension(480,480));
		add(panel);
		repaint();
	}
}
