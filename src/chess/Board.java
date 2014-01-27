package chess;


public class Board {
	
	private Square[][] board;
	private ChessGame game;   // is this even needed?
	
	public Board(int size, ChessGame game) {
		board = new Square[size][size];
		this.game = game;
		for (int row=0;row<size;row++) {
			for (int column=0;column<size;column++) {
				board[row][column] = new Square(row,column);
			}
		}
	}
	
	public Square square(int row, int column) { return board[row][column]; }
	
	public String toString(){
		String line="0", boardString ="\n  0  1  2  3  4  5  6  7\n";
		for (int row=0;row<8;row++) {
			for (Square square : board[row]) {
				line += square.display();
			}
			boardString += line+"|\n";
			line = ""+(row+1);
		}
		return boardString;
	}
	
	public double value() {
		double value = 0;
		for (Piece piece : game.pieces()) {
			value += piece.AIVALUE;
		}
//		for (int row=0;row<board.length;row++) {
//			for (int column=0;column<board.length;column++) {
//				if (board[row][column].isOccupied()) {
//					System.out.println(board[row][column].piece().AIVALUE);
//					value += board[row][column].piece().AIVALUE;
//					
//				}
//				
//			}
//		}
		return value;
	}
	

}












