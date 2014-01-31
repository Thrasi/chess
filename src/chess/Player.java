package chess;

import java.util.ArrayList;

public abstract class Player {
	private int colour, number;
	private ArrayList<Piece> pieces;
	private ChessGame game;
	
	public Player(ChessGame game) {
		this.game = game;
		pieces = new ArrayList<Piece>();
	}
	
	public ChessGame game() { return game; }
	
	public void remove(Piece piece) {
		pieces.remove(piece);
	}
	
	public void add(Piece piece) {
		pieces.add(piece);
	}
	
	public ArrayList<Piece> pieces() {
		return this.pieces;
	}
	
	public abstract void play();
}
