package chess;
import java.util.ArrayList;

public abstract class Piece {
	private int player;
	private Square square;
	private ChessGame game;
	private Board board;
	private Player owner;
	// for display in terminal
	protected String symbol;
	protected static String[] colors = {"w","b"};
	protected double AIVALUE;
	
	public Piece(int colour, ChessGame game, Square square) {
		this.player = colour;
		this.owner = game.players[player];
		this.game = game;
		this.square = square;
		this.square.placePiece(this);
		this.board = game.board();
	}
	
	public abstract ArrayList<Square> moves();
	
	// for graphical display
	public abstract int imageIndex();
	
	public ArrayList<Square> killMoves() {
		return moves();
	}
	
	public ArrayList<Square> legalMoves() { // maybe this should belong to game
		
		ArrayList<Square> legalMoves = new ArrayList<Square>();
		for (Square destination : moves()) {
			
			Square origin = this.square();
			Piece victim = destination.piece();
			
			move(this, destination);
			
			if ( !king().isChecked()) {
				legalMoves.add(square);
			}
			
			// reset the pieces
			move(this, origin);
			
			if (victim != null) {
				destination.placePiece(victim);
				enemies().add(victim);
			}
		}
		return legalMoves;
	}
	
	private void move(Piece movingPiece, Square destination) {
		game.move(movingPiece, destination);
	}
	
	public boolean checks(King king) {
		return checks(king.square());
	}
	
	public boolean checks(Square square) { 
		return killMoves().contains(square); 
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
	
	public boolean canMoveTo(Square square) { return legalMoves().contains(square); }
	
	public boolean canMove() { return !legalMoves().isEmpty(); }
	public boolean inTeamOf(Piece piece) { return (this.player == piece.player); }
	public boolean isEnemyOf(Piece piece) { return !inTeamOf(piece); }
	public ArrayList<Piece> enemies() { return game.players[(player+1)%2].pieces(); }
	public ArrayList<Piece> teamMates() { return game.players[player].pieces(); }
	
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
