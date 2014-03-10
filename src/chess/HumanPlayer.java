package chess;

public class HumanPlayer extends Player {

	public HumanPlayer(ChessGame game, int colour) {
		super(game, colour);
	}

	@Override
	public void play() {
		// why is this necessary?  AI won't play without it
		System.out.print("");
//		UI dependent!
	}

}
