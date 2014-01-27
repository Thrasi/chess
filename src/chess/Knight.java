package chess;

import java.util.ArrayList;

public class Knight extends Piece {
	
	public Knight(int player, ChessGame game, Square square) {
		super(player, game, square);
		symbol = colors[player] + "k";
		AIVALUE = 3.2 * Math.pow(-1, player);
	}

	public int imageIndex() { return 4 + player(); }
	
	@Override
	public ArrayList<Square> moves(ArrayList<Piece> pieces) {
		ArrayList<Square> moves = new ArrayList<Square>();
		Square square;
		int row = this.row();
		int column = this.column();
		
		// up or down
		if (row+2<=7) {
			if (column+1<=7) {
				square = board().square(row+2,column+1);
				addToMoves(square,moves);
			}
			if (column-1>=0) {
				square = board().square(row+2,column-1);
				addToMoves(square,moves);
			}
		}
		// down or up
		if (row-2>=0) {
			if (column+1<=7) {
				square = board().square(row-2,column+1);
				addToMoves(square,moves);
			}
			if (column-1>=0) {
				square = board().square(row-2,column-1);
				addToMoves(square,moves);
			}
		}
		// right
		if (column+2<=7) {
			if (row+1<=7) {
				square = board().square(row+1,column+2);
				addToMoves(square,moves);
			}
			if (row-1>=0) {
				square = board().square(row-1,column+2);
				addToMoves(square,moves);
			}
		}
		// left
		if (column-2>=0) {
			if (row+1<=7) {
				square = board().square(row+1,column-2);
				addToMoves(square,moves);
			}
			if (row-1>=0) {
				square = board().square(row-1,column-2);
				addToMoves(square,moves);
			}
		}
		
		return moves;
	}
}