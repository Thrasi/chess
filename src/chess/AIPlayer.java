package chess;

import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends Player {
	
	
	public AIPlayer(ChessGame game, int colour) {
		super(game, colour);
	}

	@Override
	public void play() {
//		UI independent
//		System.out.println("Try a random move");
		try {
//			This works!
			long startTime = System.nanoTime();
			game().minimax_move();
			long endTime = System.nanoTime();

			long duration = endTime - startTime;
			System.out.printf("Time taken: %.3f s\n",duration*Math.pow(10, -9));
//			game().randomMove();
//			System.out.println("Successful randomMove");
		} catch (MateException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (StaleMateException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	
	public int[] playAI() {
		int bestPiece = 0, bestMove = 0, moveIndex;
		double boardValue, bestMoveValue = 1000;
		Random generator = new Random();
//		for (int piece=0; piece<pieces().size(); piece++) {
//			
//		}

//		ArrayList<int[]> intar = new ArrayList<int[]>();	// if I want to return many moves
		int pieceCount = pieces().size();
		for (Piece piece : pieces()) {
			moveIndex = 0;
			for (Square destination : piece.legalMoves()) {
				Square origin = piece.square();
				Piece victim = destination.piece();
				
				game().move(piece, destination);
				
				boardValue = piece.board().value();
				System.out.println(boardValue);
//				The following comparison favors pieces that were created first.
//				if the values are equal we choose the first piece and this can cause
//				repetitive behavior, at least in a shallow AI.  This might solve itself naturally
//				with deeper AI but until then I'll create a randomizer.
//				This randomizer may not be uniform.  And must be omptimized.
				
				if (Math.abs(boardValue-bestMoveValue)<0.001) {
					if (generator.nextInt(pieceCount)==0) {
						bestMoveValue = boardValue;
						bestPiece = pieces().indexOf(piece);
						System.out.println(destination);
						bestMove = moveIndex;
					}
				}else if (boardValue < bestMoveValue) {
					bestMoveValue = boardValue;
					bestPiece = pieces().indexOf(piece);
					System.out.println(destination);
					bestMove = moveIndex;
				}
				// reset the pieces
				game().move(piece, origin);
				
				if (victim != null) {
					destination.placePiece(victim);
					piece.enemies().add(victim);
				}
				moveIndex++;

//				int[] a = {1,2};
//				intar.add(a);
			}
			
		}
//		System.out.println(intar);
		
		System.out.println(bestMoveValue);
		System.out.println(bestPiece);
		System.out.println(bestMove);
		System.out.println(pieces().get(bestPiece));
		System.out.println(pieces().get(bestPiece).legalMoves().get(bestMove));
//		int[] move = {bestPiece,bestMove};
		return new int[] {bestPiece,bestMove};
		
	}
}
