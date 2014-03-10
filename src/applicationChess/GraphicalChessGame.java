package applicationChess;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import chess.*;

public class GraphicalChessGame extends ChessGame{
	
	public GraphicalChessGame() {
		super();
	}
	
	@Override
	public int promotePawnInterface() {
		Object[] options = {"Rook",
                			"Knight",
                			"Bishop",
                			"Queen"};
		
		return JOptionPane.showOptionDialog(new JFrame(),
				"To what rank do you want to promote"
				+ " your pawn: ",
				"Pawn Promotion",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[3]);
	}
	
	@Override
	public void start() {
		// this makes the AI stop!
		while(gameOn) {  
			this.currentPlayer().play();
		}
	}
	
	// found this on some forum
	public static Image getScaledImage(Image srcImg, int w, int h){
		//srcImg.getScaledInstance(arg0, arg1, arg2)  USE THIS INSTEAD OF THIS METHOD
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);//ARGB for the transparency
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();
	    return resizedImg;
	}
}
