package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class MandelbrotPart extends Thread {
	public double minReal, minIm, maxReal, maxIm;
	public int imageW, imageH, maxIte;
	String command;
	String mandelbrot;
	BufferedReader reader;
	Connection conn;
	
	public MandelbrotPart(double newMinReal, double newMinIm, double newMaxReal, double newMaxIm, int newImageW,
			int newImageH, int maxIte) {
		this.imageW = newImageW;
		this.imageH = newImageH;
		this.minReal = newMinReal;
		this.maxReal = newMaxReal;
		this.minIm = newMinIm;
		this.maxIm = newMaxIm;
		this.maxIte = maxIte;
	}
	
	public void connect(ServerInfo server) {
		conn = new Connection(server);
	}

	@Override
	public void run() {
		sendAndreceive();
	}
	
	private void prepareCommand() {
		StringBuilder sb = new StringBuilder();
		sb.append("GET /mandelbrot/");
		sb.append(minReal).append("/").append(minIm).append("/");
		sb.append(maxReal).append("/").append(maxIm).append("/");
		sb.append(imageW).append("/").append(imageH).append("/").append(maxIte);
		command = sb.toString();
	}
	
	public void sendAndreceive() {
		prepareCommand();
		conn.sendCommand(command);
		mandelbrot = conn.receiveResponse();
		reader = new BufferedReader(new StringReader(mandelbrot));
	}
	
	public String getNextLine() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
