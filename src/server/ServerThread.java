package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import server.response.Response;
import server.response.ResponseFactory;
import server.response.ResponseInternalServerError500;

public class ServerThread extends Thread {
	private Socket socket;
	public static int counter = 0;
	private final int clientId;
	private final int BUFSIZE;
	private InputStream inputStream;
	private OutputStream outputStream;

	public ServerThread(Socket socket, int BUFSIZE) {
		this.BUFSIZE = BUFSIZE;
		this.socket = socket;
		this.clientId = ++counter;
		try {
			inputStream = new DataInputStream(this.socket.getInputStream());
			outputStream = new DataOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// create an empty message
		String recievedMessage = "";
		int blocksCounter =0;
		int messageLength = 0;
		// create buffer
		byte[] buf = new byte[BUFSIZE];
		/*
		 * do loop until the receiving of the current message be done (if the server's
		 * buffer-size is small, then we need more loops)
		 */
		do {
			int readedBytes = 0;
			try {
				// read and save the received message in the buffer
				readedBytes = inputStream.read(buf);
			} catch (IOException e) {
				closeSoket();
				System.out.println("Error while receiving");
				return;
			}

			// Convert the buffer to string
			if (readedBytes>0)
				recievedMessage += new String(buf,0 , readedBytes);

			blocksCounter++;
			if (blocksCounter == 1) {
				messageLength = calculateMessageLength(recievedMessage);
			}
		} while (recievedMessage.length() < messageLength);
		
		if (recievedMessage != null && !recievedMessage.isEmpty()) {
			// Create new request according to the received message
			Request recievedRequest = new Request(recievedMessage);

			// Print information about the client and the request's header in the console
			printRequestSummary(recievedRequest.recievedMessage);

			/*
			 * Create a response factory to generate a suitable response according to the
			 * request
			 */
			ResponseFactory responseFactory = new ResponseFactory(recievedRequest);

			// Generate the response. ()
			Response response = responseFactory.getResponse();

			/*
			 * If the response is null (in case the Response Factory encountered an error 
			 * while generate the response) Then the server will returns the response 500
			 */
			if (response == null)
				response = new ResponseInternalServerError500();

			// Send the response to the client
			response.send(outputStream);
		}

		// close the socket
		closeSoket();

	}

	// I did not make real calculation since the request-size is always too small
	private int calculateMessageLength(String recievedMessage) {
		return recievedMessage.length();
	}

	// Method to print a request-summary
	private void printRequestSummary(String recievedHeader) {
		if (recievedHeader != null && !recievedHeader.isEmpty()) {
			System.out.printf("\n[Client " + clientId + "] TCP echo request from %s",
					socket.getInetAddress().getHostName());
			System.out.printf(" using port %d\n", socket.getPort());
			System.out.println("Recieved(" + recievedHeader.length() + " bytes):\n" + "Header:\n" + recievedHeader);

		}

	}

	// Method to close the socket and print a tip in the console
	private void closeSoket() {
		try {
			socket.close();
			System.out.println("\n******* Client " + clientId + ": connection is closed *******\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
