package fr.actionrpg3d;

import java.util.Scanner;

public class Main {
	
	private static final String VERSION = "0.1.0";
	private static final String DATE = "27/05/2020";
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		System.out.println("\"actionrpg3d\" " + VERSION + ", par PTLC_, le " + DATE + ", pour tous, vive l'Amour !");
		System.out.println("1: solo");
		System.out.println("2: client");
		System.out.println("3: server");
		int choice = new Scanner(System.in).nextInt();
		if (choice == 1)
			new Client().joinSolo();
		if (choice == 2)
			new Client().joinServer();
		if (choice == 3)
			new Server();
	}

}
