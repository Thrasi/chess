package chess;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class ChessGame {
	private Board board;
	private King[] kings = new King[2];
	private ArrayList<Piece> pieces = new ArrayList<Piece>();
	private ArrayList<Piece> movedPieces = new ArrayList<Piece>();
	private int player = 0;
	private Square enPassantSquare;
	private Player currentPlayer, blackPlayer, whitePlayer;
	protected Player[] players = new Player[2];
	protected boolean gameOn = true;
	public static final int WHITE = 0, BLACK = 1;
	
	public ChessGame(){
		board = new Board(8,this);
		players[0] = new HumanPlayer(this,WHITE);
		whitePlayer = players[WHITE];
		players[1] = new AIPlayer(this,BLACK);
		blackPlayer = players[BLACK];
		currentPlayer = players[0];
		
		// normal starting position
		
		int player = 0;
		pieces.add(new Rook(player,this,board.square(7, 0)));
		players[player].add(pieces.get(pieces.size()-1));
		pieces.add(new Knight(player,this,board.square(7, 1)));
		players[player].add(pieces.get(pieces.size()-1));
		pieces.add(new Bishop(player,this,board.square(7, 2)));
		players[player].add(pieces.get(pieces.size()-1));
		pieces.add(new Queen(player,this,board.square(7, 3)));
		players[player].add(pieces.get(pieces.size()-1));
		kings[player] = new King(player,this,board.square(7, 4));
		pieces.add(kings[player]);	// its important to add the king to pieces
		players[player].add(pieces.get(pieces.size()-1));
		pieces.add(new Bishop(player,this,board.square(7, 5)));
		players[player].add(pieces.get(pieces.size()-1));
		pieces.add(new Knight(player,this,board.square(7, 6)));
		players[player].add(pieces.get(pieces.size()-1));
		pieces.add(new Rook(player,this,board.square(7, 7)));
		players[player].add(pieces.get(pieces.size()-1));
		
		for (int i=0;i<8;i++){
			pieces.add(new Pawn(player,this,board.square(6, i)));
			players[player].add(pieces.get(pieces.size()-1));
		}
		
		player = 1;
		pieces.add(new Rook(player,this,board.square(0, 0)));
		players[player].add(pieces.get(pieces.size()-1));
		pieces.add(new Knight(player,this,board.square(0, 1)));
		players[player].add(pieces.get(pieces.size()-1));
		pieces.add(new Bishop(player,this,board.square(0, 2)));
		players[player].add(pieces.get(pieces.size()-1));
		pieces.add(new Queen(player,this,board.square(0, 3)));
		players[player].add(pieces.get(pieces.size()-1));
		kings[player] = new King(player,this,board.square(0, 4));
		pieces.add(kings[player]);  // its important to add the king to pieces
		players[player].add(pieces.get(pieces.size()-1));
		pieces.add(new Bishop(player,this,board.square(0, 5)));
		players[player].add(pieces.get(pieces.size()-1));
		pieces.add(new Knight(player,this,board.square(0, 6)));
		players[player].add(pieces.get(pieces.size()-1));
		pieces.add(new Rook(player,this,board.square(0, 7)));
		players[player].add(pieces.get(pieces.size()-1));
		
		for (int i=0;i<8;i++) {
			pieces.add(new Pawn(player,this,board.square(1, i)));
			players[player].add(pieces.get(pieces.size()-1));
		}
	}
	
	public int play(Piece movingPiece, Square destination) throws MateException, StaleMateException {
		int row1 = movingPiece.row(), column1 = movingPiece.column();
		int column2 = destination.column();

		if (this.player == movingPiece.player()) {
			if (movingPiece.canMoveTo(destination)) {
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
				// change players, maybe this allowes the AI to perform a move while this method call is still 
				// within hasLost() and causes a concurrentmodification exception.
				
				// check if game is ended
				hasLost((this.player+1)%2);
				changePlayer();
			}
		}
		return this.player;
	}
	
	// split this into two method or change the name...  stalemate is not a loss.
	// TODO: implement the threefold repetition draw and other possibilities
	private void hasLost(int player) throws MateException, StaleMateException {
//		ArrayList<Piece> teamMates = players[player].pieces();
//		if (kings[player].isChecked()) {
//			if (kings[player].isMated()) {
//				gameOn = false;
//				System.out.println("Mate, game on: "+gameOn);
//				throw new MateException();
//			}
//		}
//		else {
//			boolean staleMate = true;
//			
//			for (Piece piece : teamMates) {
//				if (piece.inTeamOf(kings[player]) && !piece.legalMoves().isEmpty()) {
//					staleMate = false;
//					break;
//				}
//			}
//			if (staleMate) {
//				gameOn = false;
//				System.out.println("Stale mate, game on: "+gameOn);
//				throw new StaleMateException();
//			}
//		}
		Player p = opponent();
		if (p.isMated()) {
			gameOn = false;
			System.out.println("Mate, game on: "+gameOn);
			throw new MateException();
		}
		else {
			if (staleMated(p)) {
				gameOn = false;
				System.out.println("Stale mate, game on: "+gameOn);
				throw new StaleMateException();
			}
		}
	}
	
	private boolean mated(Player player) {
		ArrayList<Piece> teamMates = player.pieces();
		if (player.isChecked()) {
			if (player.isMated()) {
				gameOn = false;
				System.out.println("Mate, game on: "+gameOn);
				return true;
			}
		}
		return false;
	}
	
	private boolean staleMated(Player player) {
		boolean staleMate = true;
		for (Piece piece : player.pieces()) {
			if (!piece.legalMoves().isEmpty()) {
				staleMate = false;
				break;
			}
		}
		return staleMate;
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
	
	private Piece promotePawn(Pawn pawn) {
		Square square = pawn.square();
		Piece promotedPiece = null;
		pieces.remove(pawn);
		
		int typeOfPiece = promotePawnInterface();
		
		switch (typeOfPiece) {
		case 0:
			promotedPiece = new Rook(player, this, square); break;
		case 1:
			promotedPiece = new Knight(player, this, square); break;
		case 2:
			promotedPiece = new Bishop(player, this, square); break;
		case 3:
		default:
			promotedPiece = new Queen(player, this, square); break;
		}
		
		pieces.add(promotedPiece);
		
		return promotedPiece;
	}
	
	// choose a queen as default.  Should be overridden by the user interface.
	public int promotePawnInterface() {
		return 3; 
	}
	
	// move to board?
	public void move(Piece movingPiece, Square destination) {
		Piece victim = destination.piece();
		if (victim != null) {
			victim.teamMates().remove(victim);
		}
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
	
	public void changePlayer() { 
		this.player = (this.player+1)%2;
		this.currentPlayer = (this.currentPlayer.equals(players[0])) ? players[1] : players[0];
	}
	
	public int player() { return player; }
	
	public void randomMove() throws MateException, StaleMateException {
		int newPlayer = this.player;
		
		while (this.player == newPlayer) {
			System.out.println("Game on: "+gameOn);
			Random generator = new Random();
			int pieceNr = generator.nextInt(currentPlayer.pieces().size());
			Piece piece = currentPlayer.pieces().get(pieceNr);
			
			ArrayList<Square> possibleMoves = piece.legalMoves();
			if (!possibleMoves.isEmpty()) {	
				int moveNr = generator.nextInt(possibleMoves.size());
				Square move = possibleMoves.get(moveNr);
				play(piece, move);
			}
		}
	}
	
	public void start() {
		
	}
	
	public Player currentPlayer() { return currentPlayer; }
	
	public Player opponent() {
		return (this.currentPlayer.equals(players[0])) ? players[1] : players[0];
	}
	private void pause() {
		try {
			TimeUnit.MILLISECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private ArrayList<Piece> copyPieceList(ArrayList<Piece> list) {
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		for (Piece piece : list) {
			pieces.add(piece);
		}
		return pieces;
	}
	
	int depth = 2;
	double alpha;
//	This favors pieces that are created first, I need to randomize.
	public void minimax_move() throws MateException, StaleMateException {
		Piece movingPiece = null;
		Square destination = null;
		System.out.println(currentPlayer().colour() == BLACK);
		System.out.println(currentPlayer().colour());
		System.out.println(currentPlayer() instanceof AIPlayer);
		double v = (currentPlayer().colour() == BLACK) ? 1000 : -1000;
		ArrayList<Piece> pieces = copyPieceList(currentPlayer().pieces());
		
		for (Piece piece : pieces) {
			for (Square move : piece.legalMoves()) {
				Square origin = piece.square();
				Piece victim = move.piece();
				this.move(piece, move);
//				pause();
				double vv = (currentPlayer().colour() == BLACK) ? max_Value(depth) : min_Value(depth);
				boolean newBest = (currentPlayer().colour() == BLACK) ? vv < v : vv > v;
				if (newBest) {
					v = vv;
					movingPiece = piece;
					destination = move;
				}
//				Reset the pieces
				this.move(piece, origin);
				if (victim != null) {
					move.placePiece(victim);
					piece.enemies().add(victim);
				}			
			}	
		}
		System.out.println(movingPiece);
		System.out.println(destination);
		this.play(movingPiece, destination);
		System.out.println("DONE!");
		System.out.println(v);
	}
//	function for choosing the best for white
	private double max_Value(int d) {
		if (whitePlayer.isMated()) { return -1000; }
		boolean staleMate = true;
		if (d == 0) { return board().value(); }
		
		double v = -1000;

		ArrayList<Piece> whitePieces = copyPieceList(whitePlayer.pieces());

		for (Piece piece : whitePieces) {
			staleMate = false;
			for (Square move : piece.legalMoves()) {
				Square origin = piece.square();
				Piece victim = move.piece();
				this.move(piece, move);
//				pause();
				double vv = min_Value(d-1);
				v = Math.max(v, vv);
//				Reset the pieces
				this.move(piece, origin);
				if (victim != null) {
					move.placePiece(victim);
					piece.enemies().add(victim);
				}
			}
		}
		if (staleMate) { return 0; }
		return v;
	}
	
//	function for choosing the best for black
	private double min_Value(int d) {
		if (blackPlayer.isMated()) { return 1000; }
		boolean staleMate = true;
		if (d == 0) { return board().value(); }
		
		double v = 1000;
		ArrayList<Piece> blackPieces = copyPieceList(blackPlayer.pieces());
		for (Piece piece : blackPieces) {
			staleMate = false; 
			for (Square move : piece.legalMoves()) {
				Square origin = piece.square();
				Piece victim = move.piece();
				this.move(piece, move);
//				pause();
				double vv = max_Value(d-1);
				v = Math.min(v, vv);
//				Reset the pieces
				this.move(piece, origin);
				if (victim != null) {
					move.placePiece(victim);
					piece.enemies().add(victim);
				}
			}
		}
		if (staleMate) { return 0; }
		return v;
	}
}
