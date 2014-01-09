package chess;

import java.util.ArrayList;

public class Pawn extends Piece{
	private int movingDirection;
	
	public Pawn(int player, ChessGame game, Board board, Square square) {
		super(player, game, board, square);
		movingDirection = (player == 0) ? -1 : 1;
		symbol = colors[player] + "P";
	}
	
	public void promote(Piece piece){
		this.square().clear();
		this.square().placePiece(piece);
	}
	
	@Override 
	public ArrayList<Square> killMoves(ArrayList<Piece> pieces) {
		ArrayList<Square> killMoves = new ArrayList<Square>();
		for (Square destination : moves(pieces)) {
			if (destination.column() != this.column())
				killMoves.add(destination);
		}
		return killMoves;
	}

	@Override
	public ArrayList<Square> moves(ArrayList<Piece> pieces){
		/*
		 * TODO: en passat! needs to keep track of which piece was moved last
		 * and it should be implemented after the gameplay has been implemented
		 */
		
		ArrayList<Square> moves = new ArrayList<Square>();
		Square square;
		int row = this.row();
		int column = this.column();
		
		// Be careful with the position reference.
		if (row+movingDirection >= 0 && row+movingDirection <= 7) {// general move
			square = board().square(row+movingDirection,column);
			if (!square.isOccupied())
				addToMoves(square,moves);
		}
		if (row == 1 && movingDirection > 0 || row == 6 && movingDirection < 0) { // first move
			Square firstSquare = board().square(row+movingDirection,column);
			square = board().square(row+2*movingDirection,column);
			if (!firstSquare.isOccupied() && !square.isOccupied())
				addToMoves(square,moves);
		}
		
		// for the kill we need to check if it is occupied in order to use "addToMoves"
		if (column > 0 && !(row == 7 || row == 0)) {
			square = board().square(row+movingDirection,column-1);
			if (square.isOccupied() || square.equals(game().passatSquare()))
				addToMoves(square,moves);
		}
		if (column < 7 && !(row == 7 || row == 0)) {
			square = board().square(row+movingDirection,column+1);
			if (square.isOccupied() || square.equals(game().passatSquare()))
				addToMoves(square,moves);
		}
		
		return moves;
	}
}