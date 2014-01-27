package chess;

import java.util.ArrayList;

public class Rook extends Piece {
	
	public Rook(int player, ChessGame game, Square square) {
		super(player, game, square);
		symbol = colors[player] + "R";
		AIVALUE = 5.1 * Math.pow(-1, player);
	}
	
	public int imageIndex() { return 2 + player(); }
	
	@Override
	public ArrayList<Square> moves(ArrayList<Piece> pieces) {
	//	System.out.println("moves");
		ArrayList<Square> moves = new ArrayList<Square>();
		Square square;
		int row = this.row();
		int column = this.column();
		for (int i=1;row+i<=7;i++) {
			square = board().square(row+i,column);
			if (!addToMoves(square,moves))
				break;
		}
		for (int i=1;row-i>=0;i++) {
			square = board().square(row-i,column);
			if (!addToMoves(square,moves))
				break;
		}
		for (int i=1;column+i<=7;i++) {
			square = board().square(row,column+i);
			if (!addToMoves(square,moves))
				break;
		}
		for (int i=1;column-i>=0;i++) {
			square = board().square(row,column-i);
			if (!addToMoves(square,moves))
				break;
		}
		return moves;
	}
}
