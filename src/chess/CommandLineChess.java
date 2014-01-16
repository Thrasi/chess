package chess;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CommandLineChess extends ChessGame{
	
	@Override
	public void promotePawnInterface(Piece piece) {
		// default: just choose a queen
		String input = readString("Please choose [r]ook/[k]night/[b]ishop/[q]ueen: ",false).toLowerCase();
		Piece promotedPiece = null;
		while (promotedPiece == null) {
			char typeOfPiece = input.charAt(0);
			switch (typeOfPiece) {
				case 'r':
					promotedPiece = new Rook(piece.player(), piece.game(), piece.square()); break;
				case 'k':
					promotedPiece = new Knight(piece.player(), piece.game(), piece.square()); break;
				case 'b':
					promotedPiece = new Bishop(piece.player(), piece.game(), piece.square()); break;
				case 'q':
					promotedPiece = new Queen(piece.player(), piece.game(), piece.square()); break;
				default:
					input = readString("Enter r, k, b or q: ",false);
			}
		}
		piece.game().setPromotionPiece(promotedPiece);
		//return promotedPiece;
	}

	public static void main(String[] args) {
		ChessGame game = new CommandLineChess();
		System.out.println(game.movedPieces());
		
		int r1, r2, c1, c2;
		System.out.println(game.board());
		int playCount = 0, plays = 200;
		
		try{
			while(playCount<plays){
				playCount++;
				System.out.println(game.board());
//				player = game.randomMove();
				
				System.out.printf("%s, choose a piece to move.\n",(game.player() == 0) ? "White" : "black");
				r1 = readInt("row: ", 0, 7);
				c1 = readInt("column: ", 0, 7);
				System.out.println("Choose a destination for the piece.");
				r2 = readInt("row: ", 0, 7);
				c2 = readInt("column: ", 0, 7);
				
				game.play(r1, c1, r2, c2);
			}
		}
		catch (MateException e) {
			System.out.println(game.board());
			System.out.printf("Check mate!\n");
			System.out.printf("%s Wins\n",(game.player() == 0) ? "White" : "black");
			System.out.printf("%s looses\n",(game.player() == 0) ? "black" : "White");
		}
		catch (StaleMateException e) {
			System.out.println(game.board());
			System.out.printf("Stalemate!\n");
		}
	}
	
	////////////////// Helper functions: 
	
	private static Scanner scan = new Scanner(System.in);
	
	public static void clearBuffer() {
		scan.nextLine();
	}
	
	public static int readInt(String prompt) {
		System.out.print(prompt);
		while (true) {
			try {
				int input = scan.nextInt();
				scan.nextLine(); // to remove the carriage return symbol
				return input;
			} catch (InputMismatchException e) {
				System.out.println("\nEnter an integer: ");
				scan.nextLine(); // to remove the carriage return symbol
			}
		}
	}
	public static int readInt(String prompt, int min, int max){
		int input = readInt(prompt);
		while (min > input || input > max){
			System.out.printf("\nInput should be between %d and %d\n",min,max);
			input = readInt(prompt);
		}
		return input;
	}
	
	public static String readString(String prompt){
		System.out.print(prompt);
		return scan.nextLine().trim();
	}
	public static String readString(String prompt,boolean allowEmpty){
		String input = readString(prompt);
		while (input.isEmpty()){
			System.out.printf("\nInput should be non-empty!\n");
			input = readString(prompt);
		}
		return input;
	}
	
}
