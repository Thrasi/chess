package chess;

import java.util.ArrayList;

public abstract class Player {
	private int colour;
	private ArrayList<Piece> pieces;
	private ChessGame game;
	
	public Player(ChessGame game, int colour) {
		this.game = game;
		pieces = new ArrayList<Piece>();
		this.colour = colour;
	}
	
	public boolean isMated() {
		return this.isChecked() && game.king(colour).isMated();
	}
	
	public boolean isChecked() {
		return game.king(colour).isChecked();
	}
	
	public ChessGame game() { return game; }
	
	public int colour() { return colour; }
	
	public void remove(Piece piece) { pieces.remove(piece); }
	
	public void add(Piece piece) { pieces.add(piece); }
	
	public ArrayList<Piece> pieces() { return this.pieces; }
	
	public abstract void play();
	
}
