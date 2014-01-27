package chess;
import java.util.ArrayList;


public class King extends Piece{
	
	public King(int player, ChessGame game, Square square){
		super(player, game, square);
		symbol = colors[player] + "K";
		AIVALUE = 100 * Math.pow(-1, player);
	}
	
	public int imageIndex() { return 10 + player(); }
	
	// To get passed an infinite loop caused by isMated and castling.  Don't forget the '!'!
	@Override   
	public boolean canMove(ArrayList<Piece> pieces) {
		System.out.println(super.legalMoves(pieces));
		return !super.legalMoves(pieces).isEmpty();
	}
	
	@Override
	public ArrayList<Square> moves(ArrayList<Piece> pieces){
		ArrayList<Square> moves = new ArrayList<Square>();
		int row = this.row();
		int column = this.column();
		
		// go through a 1-nearest neighbour square around the king and
		// make sure it is on the board, then remove the current position.
		for (int i=-1;i<=1;i++){
			if (row+i >= 0 && row+i <= 7) {
				for (int j=-1;j<=1;j++){
					if (column+j >= 0 && column+j <= 7) {
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
		
		return moves;
	}
	
	@Override
	// When castling was added in moves it caused problems of infinite looping. Solved.
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
	// This is solved, read other comments.
	private void addCastling(int border, int increment, ArrayList<Square> moves) {
		Square probe = board().square(this.row(), this.column());
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		
		for (Piece piece : game().pieces()) {
			pieces.add(piece);
		}

		if (probe.isChecked(this, pieces)) { return; }
		
		for (int i=1;i<=2;i++) {
			probe = board().square(probe.row(), probe.column()+increment);
			if (probe.isOccupied() || probe.isChecked(this, pieces)) { return; }
		}
		
		while ((probe.column() != border) && !(probe.isOccupied() )) {
			probe = board().square(probe.row(), probe.column()+increment);
		}
		
		if (!game().movedPieces().contains(probe.piece()) && probe.piece() instanceof Rook) {
			moves.add(board().square(probe.row(), this.column()+2*increment));
		}
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
		// This I need to think about!  If king is checked by rook then the square behind the
		// king is not!  Will canMove return true since the square is not checked? Nope since
		// canMove uses super.legalMoves which actually moves the piece and makes sure that the
		// move does not result in a check!
		// but that means that the system below is not correct, checking the king specially if 
		// his moves are checked since moves() does not move the king to the actual square.
		// Now it should be correct.
		for (int i=0;i<pieces.size();i++){
//		for (Piece piece : pieces){  // this caused an exception with the iterator, think about it.
			Piece piece = pieces.get(i);

			if (piece.inTeamOf(this) && piece.canMove(pieces)){
				System.out.println(piece.symbol);
				System.out.println(piece.legalMoves(pieces));
				System.out.println(piece.canMove(pieces));
				return false;
			}
		}
		return true;
	}
	
}

	
