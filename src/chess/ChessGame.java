package chess;

import java.util.ArrayList;
import statiska.inputHelper;
import java.util.Random;


public class ChessGame {
	private Board board;
	private King[] kings = new King[2];
	private ArrayList<Piece> pieces = new ArrayList<Piece>();
	private ArrayList<Piece> movedPieces = new ArrayList<Piece>();
//	private ArrayList<Piece> p1Pieces = new ArrayList<Piece>();
//	private ArrayList<Piece> p2Pieces = new ArrayList<Piece>();
//	private int p1 = 0, p2 = 1;
//	private boolean p1Turn;
	private Square enPassantSquare;
	
	public ChessGame(){
		board = new Board(8,this);
		//This one causes an exception with the by moving the black
		// queen to 1,4. Why? Seems like an altered iterator but I have
		// not found it. This does not happen if I move some other piece first
		//
//		int player = 0;
//		pieces.add(new Rook(player,this,board,board.square(7, 0)));
//		kings[player] = new King(player,this,board,board.square(7, 4));
//		pieces.add(kings[player]);
//		player = 1;
//		pieces.add(new Rook(player,this,board,board.square(0, 0)));
//		pieces.add(new Queen(player,this,board,board.square(0, 3)));
//		kings[player] = new King(player,this,board,board.square(0, 4));
//		pieces.add(kings[player]);
//		for (Piece piece : pieces) {
//			movedPieces.add(piece);
//		}
		
		
		
//		int player = 0;
//		pieces.add(new Rook(player,this,board,board.square(4, 0)));
//		kings[player] = new King(player,this,board,board.square(0, 2));
//		pieces.add(kings[player]);
//		//pieces.add(new Queen(player,this,board,board.square(0, 3)));
//		
//		player = 1;
//		kings[player] = new King(player,this,board,board.square(0, 0));
//		pieces.add(kings[player]);
//		for (Piece piece : pieces) {
//			movedPieces.add(piece);
//		}
		
		
		// castling:
		int player = 0;
		pieces.add(new Rook(player,this,board,board.square(7, 0)));
		kings[player] = new King(player,this,board,board.square(7, 4));
		pieces.add(kings[player]);
		pieces.add(new Rook(player,this,board,board.square(7, 7)));
		
		player = 1;
		pieces.add(new Rook(player,this,board,board.square(0, 0)));
		pieces.add(new Bishop(player,this,board,board.square(0, 2)));
		kings[player] = new King(player,this,board,board.square(0, 4));
		pieces.add(kings[player]);
		pieces.add(new Rook(player,this,board,board.square(0, 7)));
		
		
		// normal starting position
		
//		int player = 0;
//		pieces.add(new Rook(player,this,board,board.square(7, 0)));
//		pieces.add(new Knight(player,this,board,board.square(7, 1)));
//		pieces.add(new Bishop(player,this,board,board.square(7, 2)));
//		pieces.add(new Queen(player,this,board,board.square(7, 3)));
//		kings[player] = new King(player,this,board,board.square(7, 4));
//		pieces.add(kings[player]);
//		pieces.add(new Bishop(player,this,board,board.square(7, 5)));
//		pieces.add(new Knight(player,this,board,board.square(7, 6)));
//		pieces.add(new Rook(player,this,board,board.square(7, 7)));
//		
//		for (int i=0;i<8;i++)
//			pieces.add(new Pawn(player,this,board,board.square(6, i)));
//		
//		player = 1;
//		pieces.add(new Rook(player,this,board,board.square(0, 0)));
//		pieces.add(new Knight(player,this,board,board.square(0, 1)));
//		pieces.add(new Bishop(player,this,board,board.square(0, 2)));
//		pieces.add(new Queen(player,this,board,board.square(0, 3)));
//		kings[player] = new King(player,this,board,board.square(0, 4));
//		pieces.add(kings[player]);
//		pieces.add(new Bishop(player,this,board,board.square(0, 5)));
//		pieces.add(new Knight(player,this,board,board.square(0, 6)));
//		pieces.add(new Rook(player,this,board,board.square(0, 7)));
//		
//		for (int i=0;i<8;i++)
//			pieces.add(new Pawn(player,this,board,board.square(1, i)));
		
	}
	
	public int play(int row1, int column1, int row2, int column2, int player) throws MateException,StaleMateException{
		Piece movingPiece = board().square(row1, column1).piece();
		Square destination = board().square(row2, column2);
//		Piece victim = destination.piece();
		if (movingPiece == null){
			return player;
		} // maybe useful if an empty square is chosen

		System.out.println(movingPiece.legalMoves(pieces));

		if (player == movingPiece.player()) {
			if (movingPiece.canMoveTo(destination, pieces)) {
				
				// en passant:
				if (movingPiece instanceof Pawn) {
					enPassant(movingPiece, destination);
				}else{
					enPassantSquare = null;
				}
				
				if (movingPiece instanceof King && Math.abs(column1-column2) == 2) {
					
					Square rookDestination = board().square(row1, (column1+column2)/2);
					Piece rook = (column1-column2<0) ? board.square(row1, 7).piece() : board.square(row1, 0).piece();
					
					move(rook,rookDestination);
//					pieces.remove(victim);
//					rook.square().clear();
//					rook.assignSquare(rookDestination);
//					rookDestination.placePiece(rook);
				}
				
				// move the pieces
				move(movingPiece, destination);
				
				//if (movingPiece instanceof King || movingPiece instanceof Rook) {
				if (!movedPieces.contains(movingPiece)) {
					movedPieces.add(movingPiece);
				}
				//}
					
				// change players
				player = (player+1)%2;
				
				// check if game is ended
				hasLost(player);
//				if (kings[player].isChecked(pieces)) {
//					System.out.println("CHECK!");
//					if (kings[player].isMated(pieces)) {
//						System.out.println("MATE!");
//						throw new ArithmeticException();
//					}
//				}else {
//					boolean staleMate = true;
//					for (Piece piece : pieces) {
//						if (piece.inTeamOf(kings[player]) && !piece.legalMoves(pieces).isEmpty()) {
//							staleMate = false;
//							break;
//						}
//					}
//					if (staleMate)
//						throw new ClassCastException();
//				}
			}
			else{
				System.out.println("canMoveTo failed");
			}
		}
		return player;
	}
	
	private void hasLost(int player) throws MateException,StaleMateException {
		if (kings[player].isChecked(pieces)) {
			System.out.println("CHECK!");
			if (kings[player].isMated(pieces)) {
				System.out.println("MATE!");
				throw new MateException();
			}
		}else {
			boolean staleMate = true;
			for (Piece piece : pieces) {
				if (piece.inTeamOf(kings[player]) && !piece.legalMoves(pieces).isEmpty()) {
					staleMate = false;
					break;
				}
			}
			if (staleMate)
				throw new StaleMateException();
		}
	}
		
	private void enPassant(Piece movingPiece, Square destination) {
		int row1 = movingPiece.row();
		int column1 = movingPiece.column();
		int row2 = destination.row();
		int column2 = destination.column();
		
		if (Math.abs(row1-row2) == 2) {
			enPassantSquare = board().square((row1+row2)/2, column1);
		} else {
			if (destination.equals(enPassantSquare)) {
				Piece victim=board().square(row1, column2).piece();
				board().square(row1, column2).clear();
				pieces.remove(victim);
			}
			enPassantSquare = null;
		}
		
//		if (destination.equals(enPassantSquare)) {
//			Piece victim=board().square(row1, column2).piece();
//			board().square(row1, column2).clear();
//			pieces.remove(victim);
//			enPassantSquare = null;
//		}else if (Math.abs(row1-row2) == 2) {
//			enPassantSquare = board().square((row1+row2)/2, column1);
//		}else{
//			enPassantSquare = null;
//		}
	}
	
	public void move(Piece movingPiece, Square destination) {
		Piece victim = destination.piece();
		pieces.remove(victim);
		movingPiece.square().clear();
		movingPiece.assignSquare(destination);
		destination.placePiece(movingPiece);
	}
	
	public Square passatSquare() { return enPassantSquare; }
	public King king(int player) { return kings[player]; }
	public Board board() { return this.board; }
	public ArrayList<Piece> pieces() { return pieces; }
	public ArrayList<Piece> movedPieces() { return movedPieces; }
	
	public int randomMove(int player) throws MateException,StaleMateException {
		int newPlayer = player;
		while (player == newPlayer) {
			Random generator = new Random();
			int pieceNr = generator.nextInt(pieces.size());
			Piece piece = pieces.get(pieceNr);
			if (piece.legalMoves(pieces).isEmpty()) {
				
			}
			else {
				int moveNr = generator.nextInt(piece.legalMoves(pieces).size());
				Square move = piece.legalMoves(pieces).get(moveNr);
				newPlayer = play(piece.row(),piece.column(),move.row(),move.column(),player);
			}
		}
		return newPlayer;
	}
	
	public static void main(String[] args) {
		ChessGame game = new ChessGame();
		System.out.println(game.movedPieces());
		
		
		int player = 0, r1, r2, c1, c2;
		System.out.println(game.board);
		//game.play(0, 3, 1, 4, player);
		int playCount = 0, plays = 200;
		try {
		player = game.play(0, 4, 0, 6, player);
		}catch(MateException e){
			
		}catch(StaleMateException a){
			
		}
		try{
			while(playCount<plays){
				playCount++;
				System.out.println(game.board);
//				player = game.randomMove(player);
				
				System.out.printf("%s, choose a piece to move.\n",(player == 0) ? "White" : "black");
				r1 = inputHelper.readInt("row: ", 0, 7);
				c1 = inputHelper.readInt("column: ", 0, 7);
				System.out.println("Choose a destination for the piece.");
				r2 = inputHelper.readInt("row: ", 0, 7);
				c2 = inputHelper.readInt("column: ", 0, 7);
				
				player = game.play(r1, c1, r2, c2, player);
			}
		}
		catch (MateException e) {
			System.out.println(game.board);
			System.out.printf("Check mate!\n");
			System.out.printf("%s Wins\n",(player == 0) ? "White" : "black");
			System.out.printf("%s looses\n",(player == 0) ? "black" : "White");
		}
		catch (StaleMateException e) {
			System.out.println(game.board);
			System.out.printf("Stalemate!\n");
		}
	}
}

class MateException extends Exception {

	MateException() {
		super();
	}
}
class StaleMateException extends Exception {

	StaleMateException() {
		super();
	}
}
