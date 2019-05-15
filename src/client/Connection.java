package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Connection {
	Socket socket;
	InputStream inputStream;
	OutputStream outputStream;
	
	final String RemoteIP;
	final int RemotePort;
	final int BUFSIZE = 1024;
	byte[] buf;
	
	public Connection (ServerInfo server) {
		socket = new Socket();
		RemoteIP = server.IP;
		RemotePort = server.Port;
		buf = new byte[BUFSIZE];
		try {
			/* Create local endpoint */
			SocketAddress localBindPoint = new InetSocketAddress(0);

			/* Create remote endpoint */
			SocketAddress remoteBindPoint = new InetSocketAddress(RemoteIP, RemotePort);
			
			socket.bind(localBindPoint);
			socket.connect(remoteBindPoint);
			
			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new DataOutputStream(socket.getOutputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendCommand(String command) {
		try {
			outputStream.write(command.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String receiveResponse() {
		StringBuilder receivedMessage = new StringBuilder();

		int blockNum =0;
		int length =0;
		do {
			int numberOfBytes=0;
			try {
				numberOfBytes = inputStream.read(buf);
				receivedMessage.append(new String(buf, 0, numberOfBytes));
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (blockNum ==0) {
				length = getLength(receivedMessage);
			}
			blockNum++;
		} while (receivedMessage.length() < length);
		return receivedMessage.toString();
	}

	private int getLength(StringBuilder receivedMessage) {
		String lengthAsText = receivedMessage.toString().split("_", 2)[0];
		int length = Integer.parseInt(lengthAsText);
		receivedMessage.replace(0, lengthAsText.length()+1, "");
		return length;
	}

}
