package chess;
import java.util.ArrayList;


public class King extends Piece{
	boolean hasMoved;
	
	public King(int player, ChessGame game, Board board, Square square){
		super(player, game, board, square);
		symbol = colors[player] + "K";
		hasMoved = false;
	}
	
	public boolean hasMoved() {
		return hasMoved;
	}
	
	@Override
	public ArrayList<Square> moves(ArrayList<Piece> pieces){
		ArrayList<Square> moves = new ArrayList<Square>();
		int row = this.row();
		int column = this.column();
		
		// go through a 1-nearest neighbour square around the king and
		// make sure it is on the board, then remove the current position.
		for (int i=-1;i<=1;i++){
			if (row+i >= 0 && row+i <= 7){
				for (int j=-1;j<=1;j++){
					if (column+j >= 0 && column+j <= 7){

						//System.out.println("i: "+(row+i)+", j: "+(column+j));
						Square destination = board().square(row+i, column+j);
						Piece piece = destination.piece();
						if (!destination.isOccupied()) {
							moves.add(destination);
						}
						else if (piece.isEnemyOf(this)) {
							moves.add(destination);
						}
					}
				}
			}
		}
		
		moves.remove(this.square());
		
		// castling
//		if (!game().movedPieces().contains(this)) {
//			addCastling(0,-1,moves);
//			addCastling(7,1,moves);
//		}
		
		return moves;
	}
	
	@Override
	// When castling was added in moves it caused problems 
	public ArrayList<Square> legalMoves(ArrayList<Piece> pieces){
		
		ArrayList<Square> legalMoves = new ArrayList<Square>();
		legalMoves = super.legalMoves(pieces);
		if (!game().movedPieces().contains(this)) {
			addCastling(0,-1,legalMoves);
			addCastling(7,1,legalMoves);
		}
		return legalMoves;
	}
	
	// .isChecked within moves causes an infinite cycle when the enemy king is chosen since
	// it has not been moved either.  
	// Solved by removing making a copy of the pieces list and removing the king.
	
	// Need to go through which squares are controlled for checks and maybe change.
	// Done but it fucked it up. I'm in the process of solving it.
	// It works now.
	// Next I have overridden legalMoves and call addCasting there in stead of in moves.  This allows
	// me to not remove the enemy king from the pieces copy which relieves me of manually making sure 
	// that the enemy king doesn't check the squares that the king passes through.
	// Except when checking if a king is mated! Since it calls legalMoves rather than moves.
	// Think about it and solve it!
	private void addCastling(int border, int increment, ArrayList<Square> moves) {
		Square probe = board().square(this.row(), this.column());
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		Piece enemyKing = game().king((player()+1)%2);
		for (Piece piece : game().pieces()) {
			pieces.add(piece);
		}
		pieces.remove(enemyKing);
		if (probe.isChecked(this, pieces)) { return; }
//		System.out.println("a");
//		System.out.println(probe);
		for (int i=1;i<=2;i++) {
//			System.out.println(i);
			probe = board().square(probe.row(), probe.column()+increment);
//			System.out.println(probe);
			if (probe.isOccupied() || probe.isChecked(this, pieces)) { return; }
		}
//		System.out.println("b");
//		System.out.println(probe);
		while ((probe.column() != border) && !(probe.isOccupied() )) {
			probe = board().square(probe.row(), probe.column()+increment);
//			System.out.println(probe);
		}
//		System.out.println("c");
		if (!game().movedPieces().contains(probe.piece()) && probe.piece() instanceof Rook) {
			moves.add(board().square(probe.row(), this.column()+2*increment));
		}
		
		// need to add checking by the enemyKing could get messy!
		
//		while ((probe.column() <= border+2*increment) &&//
//				!(probe.isOccupied() || probe.isChecked(this, pieces))) {
//			
//			probe = board().square(probe.row(), probe.column()+increment);
//		}
//		
//		while ((probe.column() != border) && !(probe.isOccupied() )) {
//			probe = board().square(probe.row(), probe.column()+increment);
//		}
//		
//		if (probe.isOccupied() && !game().movedPieces().contains(probe.piece()) //
//							   && probe.piece() instanceof Rook) {
//			
//			moves.add(board().square(probe.row(), this.column()+2*increment));
//		}
	}
	
	public boolean isChecked(ArrayList<Piece> pieces){
		for (Piece piece : pieces){
			if (piece.isEnemyOf(this) && piece.checks(this, pieces)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isMated(ArrayList<Piece> pieces){

		// !piece.equals(this) in combination with the later for loop finally takes care of not
		// causing an infinite loop when using the copy of pieces in castling. Is this simpler?
		// or is the prior method preferred?
		//System.out.println("entered mate");
		//int i =0;
		for (int i=0;i<pieces.size();i++){
//		for (Piece piece : pieces){  // this caused an exception with the iterator, no idea why.
//			System.out.println(pieces);
//			System.out.println(pieces.get(i));
//			if (piece.inTeamOf(this) && piece.canMove(pieces)){
//				System.out.println("could move");
//				return false;
//			}
			Piece piece = pieces.get(i);
//			if (!pieces.get(i).equals(this) && pieces.get(i).inTeamOf(this) && pieces.get(i).canMove(pieces)){
			if (!piece.equals(this) && piece.inTeamOf(this) && piece.canMove(pieces)){
//				System.out.println("could move");
				return false;
			}
			
			

//			System.out.println(pieces);
		}
		for (Square move : this.moves(pieces)) {
			if (!move.isChecked(this, pieces))
				return false;
		}
//		System.out.println(pieces);
		return true;
	}
	
}

	
