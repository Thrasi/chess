package applicationChess;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

import graphics.BoardPanel;

public class ApplicationChess extends GraphicalChessGame{
	BoardPanel panel;
	public ApplicationChess() {
		super();
		Image darkSquareImg = null;
		Image lightSquareImg = null;
		Image[] pieceImages = new Image[12];
		
		String imgPath = "images/";
		String[] images = {"whitePawn.png","blackPawn.png",//
						"whiteRook.png","blackRook.png",//
						"whiteKnight.png","blackKnight.png",//
						"whiteBishop.png","blackBishop.png",//
						"whiteQueen.png","blackQueen.png",//
						"whiteKing.png","blackKing.png"};
		
		try {
			// scaling the squares makes the graphics much faster
			darkSquareImg = ImageIO.read(new File(imgPath+"darkSquare.png"));
			darkSquareImg = getScaledImage(darkSquareImg,60,60);
			lightSquareImg = ImageIO.read(new File(imgPath+"lightSquare.png"));
			lightSquareImg = getScaledImage(lightSquareImg,60,60);
			int index = 0;
			Image pieceImg;
			for (String img : images) {
				pieceImg = ImageIO.read(new File(imgPath+img));
				pieceImg = getScaledImage(pieceImg,60,60);
				pieceImages[index++] = pieceImg;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("images failed to load");
		}
		
		BoardPanel panel = new BoardPanel(this, pieceImages, darkSquareImg, lightSquareImg);
		
//		panel = new BoardPanel(this, pieceImages, darkSquareImg, lightSquareImg);
		JFrame frame = new JFrame();
//		JFrame frame = new Chess();
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		panel.repaint();
		frame.setVisible(true);
		
	}

	public static void main(String[] args) {
		ApplicationChess ac = new ApplicationChess();
		
		ac.start();
	}
	
//	class Chess extends JFrame implements ActionListener{
//		@Override
//		public void paint(Graphics g) {
//			
//		}
//
//		@Override
//		public void actionPerformed(ActionEvent arg0) {
//			for (Component c : panel.getComponents()) {
//				c.update(getGraphics());
//			}
////			super.paint(getGraphics());
//			
//		}
//		
//	}
}
