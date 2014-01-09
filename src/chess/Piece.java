package chess;
import java.util.ArrayList;

public abstract class Piece {
	private int player;
	private Square square;
	private ChessGame game;
	private Board board;
	// for display in terminal
	protected String symbol;
	protected static String[] colors = {"w","b"};
	//
	
	public Piece(int player, ChessGame game, Board board, Square square) {
		this.player = player;
		this.game = game;
		this.square = square;
		this.square.placePiece(this);
		this.board = board;
	}
	
	public abstract ArrayList<Square> moves(ArrayList<Piece> pieces);
	
	public ArrayList<Square> killMoves(ArrayList<Piece> pieces) {
		return moves(pieces);
	}
		
//	public ArrayList<Square> killMoves(ArrayList<Piece> pieces) {
//		ArrayList<Square> killMoves = new ArrayList<Square>();
//		for (Square destination : moves(pieces)) {
//			if (square.isOccupied()) {
//				killMoves.add(destination);
//			}
//		}
//		return killMoves;
//	}
	
	public ArrayList<Square> legalMoves(ArrayList<Piece> pieces) { // maybe this should belong to game
		ArrayList<Square> legalMoves = new ArrayList<Square>();
//		System.out.println(moves(pieces));
		for (Square destination : moves(pieces)) {
			
			Square origin = this.square();
			Piece victim = destination.piece();
			
			move(this,destination);
//			
//			pieces.remove(victim);
//			origin.clear();
//			this.assignSquare(destination);
//			destination.placePiece(this);
			
			if ( !king().isChecked(pieces)) {
				legalMoves.add(square);
			}
			
			// reset the pieces
			move(this,origin);
			// victim = blablabla
//			pieces.remove(victim);
//			destination.clear();
//			this.assignSquare(origin);
//			origin.placePiece(this);
			
			if (victim != null) {
				destination.placePiece(victim);
				pieces.add(victim);
			}
		}
//		System.out.println(legalMoves);
		return legalMoves;
	}
	
	private void move(Piece movingPiece, Square destination) {
		game.move(movingPiece, destination);
	}
	
	public boolean checks(King king, ArrayList<Piece> pieces) { 
		return killMoves(pieces).contains(king.square()); 
		}
	public boolean checks(Square square, ArrayList<Piece> pieces) { 
		return killMoves(pieces).contains(square); 
		}
	
	// Try to add square to moves and report whether we should add a new one. 
	public boolean addToMoves(Square square, ArrayList<Square> moves) {
		if (!square.isOccupied()) {
			moves.add(square);
			return true;
		}
		else if (square.piece().isEnemyOf(this)) {
			moves.add(square);
		}
		return false;
	}
	
	public boolean canMoveTo(Square square, ArrayList<Piece> pieces) { return legalMoves(pieces).contains(square); }
	
	public boolean canMove(ArrayList<Piece> pieces) {
//		System.out.println("can move");
		boolean baba = !legalMoves(pieces).isEmpty(); 
//		System.out.println(baba);
//		System.out.println("can move");
		return baba; 
	}
	public boolean inTeamOf(Piece piece) { return (this.player == piece.player); }
	public boolean isEnemyOf(Piece piece) { return !inTeamOf(piece); }
	
	public int row() { return square.row(); }
	public int column() { return square.column(); }
	public Board board() { return board; }
	public Square square() { return square; }
	public void assignSquare(Square square) { this.square = square; }
	public int player() { return player; }
	public ChessGame game() { return game; }
	
	public King king() { return this.game.king(this.player); }
	
	public void printPosition() { System.out.println("("+row()+","+column()+")"); }
	public String toString() { return symbol; }
}
