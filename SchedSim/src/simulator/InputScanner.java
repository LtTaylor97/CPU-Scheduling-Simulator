package simulator;

import java.util.Scanner;

public class InputScanner {
	private static Scanner userIn = new Scanner(System.in);
	
	public String getLine() {
		return userIn.nextLine();
	}
	
	public int getInt() {
		return Integer.parseInt(userIn.nextLine());
	}
}
