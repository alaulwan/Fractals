package server.response;

import java.io.IOException;
import java.io.OutputStream;

import server.Request;

public abstract class Response {
	
	protected String responseText;
	protected Request recievedRequest;
	
	public Response(String responseText) {
		this.responseText =responseText;
	}
	
	public Response(Request recievedRequest) {
		this.recievedRequest =recievedRequest;
	}
	
	// Send the response to the client
	public void send(OutputStream outStream) {
		try {
			String length = responseText.length()+"_";
			outStream.write(length.getBytes());
			Thread.sleep(1000);
			outStream.write(responseText.getBytes());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
