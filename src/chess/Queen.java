package chess;

import java.util.ArrayList;

public class Queen extends Piece{
	
	public Queen(int player, ChessGame game, Board board, Square square) {
		super(player, game, board, square);
		symbol = colors[player] + "Q";
	}
	
	@Override
	public ArrayList<Square> moves(ArrayList<Piece> pieces){
		ArrayList<Square> moves = new ArrayList<Square>();
		Square square;
		int row = this.row();
		int column = this.column();
		
		for (int i=1;row+i<=7 && column+i<=7;i++) {
			square = board().square(row+i,column+i);
			if (!addToMoves(square,moves))
				break;
		}
		for (int i=1;row-i>=0 && column-i>=0;i++) {
			square = board().square(row-i,column-i);
			if (!addToMoves(square,moves))
				break;
		}
		for (int i=1;row-i>=0 && column+i<=7;i++) {
			square = board().square(row-i,column+i);
			if (!addToMoves(square,moves))
				break;
		}
		for (int i=1;row+i<=7 && column-i>=0;i++) {
			square = board().square(row+i,column-i);
			if (!addToMoves(square,moves))
				break;
		}
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