package chess;

import java.util.ArrayList;

public class Square {
	private Piece piece;
	private int row, column;
	
	public Square(int row, int column){
		this.row = row;
		this.column = column;
	}
	
	
	public int row() { return row; }
	public int column() { return column; }
	public Piece piece() { return piece; }
	public void clear() { piece = null; }
	public boolean isOccupied() { return this.piece != null; }
	public void placePiece(Piece piece) { this.piece = piece; }
	
	@Override
	public boolean equals(Object o) {
		Square square = (Square)o;
		if (square == null)
			return false;
		
		return square.row() == row && square.column() == column;
	}
	
	public String display() {
		if (piece == null) {
			return "|  ";
		}
		else {
			return "|"+piece.symbol;
		}
	}
	
	public boolean isChecked(ArrayList<Piece> enemies){
		for (Piece enemy : enemies){
			if (enemy.checks(this)){
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		return "("+row+","+column+")";
	}
}
