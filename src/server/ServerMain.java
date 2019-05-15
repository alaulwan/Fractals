package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
	public static final int BUFSIZE = 1024;
	public final static int MYPORT = 8888;

	public static void main(String[] args) throws IOException {
		int port = args.length >0 ? Integer.parseInt(args[0]):MYPORT;
		
		/* Create Server Socket */
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("TCP Server is Running on the port: " + port + ", Buffer Size: " + BUFSIZE);
		while (true)
		 {
			try {
				/* Create Socket and wait for a client */
				Socket socket = serverSocket.accept();

				/* Create a thread for each client */
				ServerThread httpServerThread = new ServerThread(socket, BUFSIZE);
				httpServerThread.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
