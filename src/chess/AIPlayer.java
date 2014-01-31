package chess;

public class AIPlayer extends Player {
	
	
	public AIPlayer(ChessGame game) {
		super(game);
	}

	@Override
	public void play() {
//		UI independent
		System.out.println("Try a random move");
		try {
			System.out.println("Try a random move");
			game().randomMove();
			System.out.println("Successful randomMove");
		} catch (MateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StaleMateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Piece piece = game.pieces()
//		game.play(piece.row(),piece.column(),destination.row(),destination.column());
	}

}
