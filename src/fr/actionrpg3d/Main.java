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
		if (choice == 1) {
			new Client().joinSolo();
		} else if (choice == 2) {
			System.out.println("Hostname ?");
			String hostname = new Scanner(System.in).nextLine();
			new Client().joinServer(hostname.length() == 0 ? "localhost" : hostname);
		} else if (choice == 3) {
			System.out.println("Hostname ?");
			String hostname = new Scanner(System.in).nextLine();
			new Server(hostname.length() == 0 ? "localhost" : hostname);
		}
	}

}
