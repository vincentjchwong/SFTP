
/**
 * Code is taken from Computer Networking: A Top-Down Approach Featuring
 * the Internet, second edition, copyright 1996-2002 J.F Kurose and K.W. Ross,
 * All Rights Reserved.
 **/

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;

class TCPClient {
	public static String command;
	public static BufferedReader inFromUser;
	public static Socket clientSocket;
	public static DataOutputStream outToServer;
	public static BufferedReader inFromServer;
	
	public static String response;
	public static byte[] byteReponse;
	
	public static void main(String argv[]) throws Exception {

		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		// Keep the client running
		while (true) {
			System.out.println("Client is currently not connected. Please type \"connect\" to connect to the server");
			if (inFromUser.readLine().equals("connect")) {
				clientSocket = new Socket("localhost", 6789);
				
				outToServer = new DataOutputStream(clientSocket.getOutputStream());
		
				inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				// While the client is connected, keep receiving responses and sending commands
				while (true) {
					readServerResponse();
					
					command = inFromUser.readLine();
					sendCommand();
					
					if (command.equals("DONE")) {
						clientSocket.close();
						break;
					}
					
					// Error checking for RETR function
					if ((command.startsWith("RETR")) && (command.length() < 5)) {
						readServerResponse();
						continue;
					}
					
					// Further error checking for RETR
					if (command.startsWith("RETR ")) {
						readServerResponse();
						if (response.substring(0,1).equals("-")) {
							continue;
						}
						
						String fileName = command.substring(5);
						String filePath = "./clientFiles/" + fileName;
						//System.out.println("File path: " + filePath);
						File file = new File(filePath);
						
						if (file.isFile() || Integer.parseInt(response.substring(1)) > new File("./clientFiles/").getUsableSpace()) {
							System.out.println("FROM CLIENT: File already exists or there is not enough space");
							command = "STOP";
							sendCommand();
							continue;
						}
						
						while (true) {
							command = inFromUser.readLine();
							sendCommand();
							if (command.substring(0,4).equals("SEND")) {
								Retrieve();
								
								Files.write(Paths.get(filePath), response.getBytes());
								break;
							}
							else if (command.substring(0,4).equals("STOP")) {
								break;
							}
							readServerResponse();
						}
					}
				}
			}
		}
	}
	
	// Reads the server's response
	public static void readServerResponse() {
		StringBuilder sb = new StringBuilder();
		while (true) {
			try {
				char responseChar = (char) inFromServer.read();
				if (responseChar == '\0') {
					break;
				}
				sb.append(responseChar);
				
			}
			catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		response = sb.toString();
		System.out.println("FROM SERVER: " + response);
	}
	
	// Sends the user command through to the server
	public static void sendCommand() throws IOException {
		while (command.length() < 4) {
			System.out.println("FROM CLIENT: Command is too short");
			command = inFromUser.readLine();
		}
		char[] commandChars = command.toCharArray();
		for (int i = 0; i < commandChars.length; i++) {
			try {
				outToServer.write(commandChars[i]);
			}
			catch (IOException e) {
				e.getMessage();
			}
		}
		
		try {
			outToServer.write('\0');
		}
		catch (IOException e) {
			e.getMessage();
		}
	}
	
	// Act on cmd RETR
	public static void Retrieve() {
		StringBuilder sb = new StringBuilder();
		
		long startTime = System.currentTimeMillis();
		while (false||(System.currentTimeMillis()-startTime)<1000) {
			try {
				startTime = System.currentTimeMillis();
				char responseChar = (char) inFromServer.read();
				if (responseChar == '\0') {
					break;
				}
				sb.append(responseChar);
				
			}
			catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		response = sb.toString();
	}
}