package chess;

import java.util.ArrayList;
import java.util.Random;

public abstract class ChessGame {
	private Board board;
	private King[] kings = new King[2];
	private ArrayList<Piece> pieces = new ArrayList<Piece>();
	private ArrayList<Piece> movedPieces = new ArrayList<Piece>();
	private int player = 0;
	private Square enPassantSquare;
	private Piece promotionPiece;
	
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
		
		// normal starting position
		
		int player = 0;
		pieces.add(new Rook(player,this,board.square(7, 0)));
		pieces.add(new Knight(player,this,board.square(7, 1)));
		pieces.add(new Bishop(player,this,board.square(7, 2)));
		pieces.add(new Queen(player,this,board.square(7, 3)));
		kings[player] = new King(player,this,board.square(7, 4));
		pieces.add(kings[player]);	// its important to add the king to pieces
		pieces.add(new Bishop(player,this,board.square(7, 5)));
		pieces.add(new Knight(player,this,board.square(7, 6)));
		pieces.add(new Rook(player,this,board.square(7, 7)));
		
		for (int i=0;i<8;i++){
			pieces.add(new Pawn(player,this,board.square(6, i)));
		}
		
		player = 1;
		pieces.add(new Rook(player,this,board.square(0, 0)));
		pieces.add(new Knight(player,this,board.square(0, 1)));
		pieces.add(new Bishop(player,this,board.square(0, 2)));
		pieces.add(new Queen(player,this,board.square(0, 3)));
		kings[player] = new King(player,this,board.square(0, 4));
		pieces.add(kings[player]);  // its important to add the king to pieces
		pieces.add(new Bishop(player,this,board.square(0, 5)));
		pieces.add(new Knight(player,this,board.square(0, 6)));
		pieces.add(new Rook(player,this,board.square(0, 7)));
		
		for (int i=0;i<8;i++) {
			pieces.add(new Pawn(player,this,board.square(1, i)));
		}
	}
	
	public int play(int row1, int column1, int row2, int column2) throws MateException, StaleMateException {
		Piece movingPiece = board().square(row1, column1).piece();
		Square destination = board().square(row2, column2);
		if (movingPiece == null) {
			return this.player;
		}

		if (this.player == movingPiece.player()) {
			if (movingPiece.canMoveTo(destination, pieces)) {
				// en passant and promotion:
				if (movingPiece instanceof Pawn) {
					enPassant(movingPiece, destination);
					if (destination.row() == 0 || destination.row() == 7)
						movingPiece = promotePawn((Pawn)movingPiece);
				}
				else {
					enPassantSquare = null;
				}
				
				if (movingPiece instanceof King && Math.abs(column1-column2) == 2) {
					Square rookDestination = board().square(row1, (column1+column2)/2);
					Piece rook = (column1-column2<0) ? board.square(row1, 7).piece() : board.square(row1, 0).piece();
					move(rook,rookDestination);
				}
				// move the pieces
				move(movingPiece, destination);
				
				if (movingPiece instanceof King || movingPiece instanceof Rook) {
					if (!movedPieces.contains(movingPiece)) {
						movedPieces.add(movingPiece);
					}
				}
				// change players
				changePlayer();
				// check if game is ended
				hasLost(this.player);
			}
		}
		return this.player;
	}
	
	
	// split this into two method or change the name...  stalemate is not a loss.
	// TODO: implement the threefold repetition draw and other possibilities
	private void hasLost(int player) throws MateException, StaleMateException {
		if (kings[player].isChecked(pieces)) {
			System.out.println("CHECK!");
			
			if (kings[player].isMated(pieces)) {
				System.out.println("MATE!");
				throw new MateException();
			}
		}
		else {
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
		} 
		else {
			if (destination.equals(enPassantSquare)) {
				Piece victim=board().square(row1, column2).piece();
				board().square(row1, column2).clear();
				pieces.remove(victim);
			}
			enPassantSquare = null;
		}
	}
	
	// this process can probably be made safer, at least more elegant.
	private Piece promotePawn(Pawn pawn) {
		pieces.remove(pawn);
		promotePawnInterface(pawn);
		pieces.add(this.promotionPiece);
		return this.promotionPiece;
	}
	
	public void setPromotionPiece(Piece piece) {
		this.promotionPiece = piece;
	}
	
	public abstract void promotePawnInterface(Piece pawn);
	
	
	// move to board?
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
	
	private void changePlayer() { this.player = (this.player+1)%2; }
	
	public int player() { return player; }
	
	public void randomMove() throws MateException, StaleMateException {
		int newPlayer = this.player;
		
		while (this.player == newPlayer) {
			Random generator = new Random();
			int pieceNr = generator.nextInt(pieces.size());
			Piece piece = pieces.get(pieceNr);
			
			if (!piece.legalMoves(pieces).isEmpty()) {	
				int moveNr = generator.nextInt(piece.legalMoves(pieces).size());
				Square move = piece.legalMoves(pieces).get(moveNr);
				play(piece.row(),piece.column(),move.row(),move.column());
			}
		}
	}
}
