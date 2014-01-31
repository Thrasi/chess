package graphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import chess.Board;
import chess.ChessGame;
import chess.Piece;
import chess.Square;


public class BoardPanel extends JPanel {
	ChessGame game;
	Board board;
	Piece movingPiece;
	Image darkSquareImg, lightSquareImg;
	Image[] pieceImages;
	
	public BoardPanel(ChessGame game, Image[] images, Image darkImg, Image lightImg) {
		this.game = game;
		this.board = game.board();
		darkSquareImg = darkImg;
		lightSquareImg = lightImg;
		pieceImages = images;

		setLayout(new GridLayout(8,8));
		
		for(int i=0; i<64; i++){
			int row = i/8;
			int column = i%8;
			
			SquareButton squareBtn = new SquareButton(i);
			squareBtn.setPreferredSize(new Dimension(60,60));	
			squareBtn.addActionListener(new SquareListener(row,column));

			add(squareBtn);
		}
//		setVisible(true);
	}
	
	private class SquareListener implements ActionListener {
		int row, column;
		
		public SquareListener(int row, int column) {
			this.row = row;
			this.column = column;
			boolean lightSquare = (row%2==0 && (8*row+column)%2==0);
			lightSquare = lightSquare || (row%2!=0 && (8*row+column)%2!=0);
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			
			System.out.println("("+row+","+column+")");
			Square square = board.square(row, column);
			
			if (square.isOccupied() && square.piece().inTeamOf(game.king(game.player()))) {
				movingPiece = board.square(row, column).piece();
			} else if (movingPiece != null) {

				try {
					game.play(movingPiece, square);
//					game.play(movingPiece.row(),movingPiece.column(),square.row(),square.column());
					
				} catch (chess.MateException e) {
					String winner = (game.player() == 0) ? "White" : "Black";
					
					JOptionPane.showMessageDialog(new JFrame(),
						winner + " wins!",
						"Check Mate!",
						JOptionPane.PLAIN_MESSAGE);
				} catch (chess.StaleMateException e) {
					JOptionPane.showMessageDialog(new JFrame(),
						"It's a tie!",
						"Stale Mate!",
						JOptionPane.PLAIN_MESSAGE);
				} finally {
					repaint();
					movingPiece = null;
					System.out.println(board);
				}
			}
		}
	}
	
	class SquareButton extends JButton {
		int number, row, column;
		boolean lightSquare;
		
		public SquareButton(int number) {
			super();
			this.number = number;
			this.row = number/8;
			this.column = number%8;
			lightSquare = (row%2==0 && number%2==0) || (row%2!=0 && number%2!=0);
			
			setOpaque(false);
			setContentAreaFilled(false);
			setBorderPainted(false);
			setFocusPainted(false);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			update(g);
			super.paintComponent(g);
		}
		
		@Override
		public void update(Graphics g) {
			// FOR SCALING THINK ABOUT BUFFEREDIMAGE SINCE HEIGHT AND WIDTH
			// ARE MORE ACCESSIBLE
			
			Image pieceImg = null;
			if (board.square(row,column).isOccupied()) {
				int index = board.square(row,column).piece().imageIndex();
				pieceImg = pieceImages[index];
			}
			if (lightSquare)
				{ setIcon(new ImageIcon(combine(pieceImg, lightSquareImg))); }
			else
				{ setIcon(new ImageIcon(combine(pieceImg, darkSquareImg))); }
		}
	}
	
	private Image combine(Image piece, Image background) {
		// from Peter Walser, stackOverflow
		// load source images
		if (piece == null) {
			return background;
		}
		BufferedImage image = (BufferedImage) background;
		BufferedImage overlay = (BufferedImage) piece;
	
		// create the new image, canvas size is the max. of both image sizes
		int w = Math.max(image.getWidth(), overlay.getWidth());
		int h = Math.max(image.getHeight(), overlay.getHeight());
		BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	
		// paint both images, preserving the alpha channels
		Graphics g = combined.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.drawImage(overlay, 0, 0, null);
	
		// Save as new image
		return combined;
	}
}
