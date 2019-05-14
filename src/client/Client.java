package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Client {
	public double minReal, minIm, maxReal, maxIm;
	public int imageW, imageH, maxIte, divisions;
	String command;
	List<ServerInfo> serverList = new ArrayList<ServerInfo>();
	List<MandelbrotPart> partsList = new ArrayList<MandelbrotPart>();

	public Client(String[] args) {
		extraxtArgs(args);
		
	}

	private void extraxtArgs(String[] args) {
		minReal = Double.parseDouble(args[0]);
		minIm = Double.parseDouble(args[1]);
		maxReal = Double.parseDouble(args[2]);
		maxIm = Double.parseDouble(args[3]);
		maxIte = Integer.parseInt(args[4]);

		imageW = Integer.parseInt(args[5]);
		imageH = Integer.parseInt(args[6]);
		divisions = Integer.parseInt(args[7]);
		
		for (int i=8; i< args.length; i++ ) {
			String ip = args[i].split(":")[0];
			String port = args[i].split(":")[1];
			ServerInfo server = new ServerInfo(ip, Integer.parseInt(port));
			serverList.add(server);
		}	
	}

	public void satrtClient() {
		String path = null;
		try {
			sendAndreceive();
			String mergedResult = merge(partsList);
			path = saveToPGM(mergedResult);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (path != null)
			System.out.println("Done.\nMandelbrot is saved to the file:\n" + path);
		else
			System.out.println("Error");

	}
	
	public void sendAndreceive() throws InterruptedException {
		divideMandelbrotIntoPartsAndSend();
		waitAllResponses();
	}

	private void waitAllResponses() throws InterruptedException {
		for (MandelbrotPart mPart : partsList)
			mPart.join();
	}

	

	private List<MandelbrotPart> divideMandelbrotIntoPartsAndSend() {
		int serverIndex = -1;
		int newImageW = imageW / divisions;
		int newImageH = imageH / divisions;

		double xStep = (maxReal-minReal) / divisions;
		double yStep = (maxIm-minIm) / divisions;
		
		for (int i=0 ; i< divisions ; i++) {
			double newMinIm = minIm + (i*yStep);
			double newMaxIm = newMinIm + yStep;

			for (int j=0 ; j< divisions ; j++) {
				serverIndex++;
				serverIndex = serverIndex >= serverList.size()? 0 : serverIndex;
				ServerInfo server = serverList.get(serverIndex);
				double newMinReal = minReal + (j*xStep);
				double newMaxReal = newMinReal + xStep;
				MandelbrotPart mPart = new MandelbrotPart(newMinReal, newMinIm, newMaxReal, newMaxIm, newImageW, newImageH, maxIte);
				mPart.connect(server);
				partsList.add(mPart);
				mPart.start();
			}
		}
		return partsList;
	}

	private String saveToPGM(String mergedResult) {
		File f = new File("Mandelbrot.pgm");
		try {
			OutputStream os = new FileOutputStream(f);
			os.write("P2\r\n".getBytes());
			os.write("# Mandelbrot\r\n".getBytes());
			os.write((imageW + " " + imageH + "\r\n").getBytes());
			os.write((255 + "\r\n").getBytes());
			os.write(mergedResult.getBytes());
			os.close();
			return f.getAbsolutePath();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String merge(List<MandelbrotPart> partsList) {
		StringBuilder sb = new StringBuilder();
		int partIndex = 0;
		for (int i=0 ; i< divisions ; i++) {
			List<MandelbrotPart> partsListToMerge = new ArrayList<MandelbrotPart>();
			for (int j=0 ; j< divisions ; j++) {
				partsListToMerge.add(partsList.get(partIndex++));
			}
			addToStringBuilder(partsListToMerge, sb);
		}
//		sb.deleteCharAt(sb.length()-1).deleteCharAt(sb.length()-1);
		sb.delete(sb.length()-2, sb.length());
		return sb.toString();
	}

	private void addToStringBuilder(List<MandelbrotPart> partsListToMerge, StringBuilder sb) {
		String line = null;
		do {
			String space = "";
			for (MandelbrotPart mPart : partsListToMerge) {
				line = mPart.getNextLine();
				if(line !=null && !line.isEmpty()) {
					sb.append(space).append(line.trim());
					space = " ";
				}
			}
			if(line !=null && !line.isEmpty())
				sb.append("\r\n");
		} while (line != null && !line.isEmpty());
		
	}

	
}
