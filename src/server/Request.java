package server;

public class Request {
	
	
	public enum RequestType {GET, POST, PUT}
	public enum RequestedInfo {MANDELBROT}

	
	public double minReal, minIm, maxReal, maxIm;
	public int imageW, imageH, maxIte;
	String recievedMessage;
	RequestType requestType;
	RequestedInfo requestedInfo;
	
	public Request (String recievedMessage) {
		this.recievedMessage = recievedMessage;
		extraxtParameters();
	}

	private void extraxtParameters() {
		String[] parametersArr = recievedMessage.split("/");
		requestType = RequestType.valueOf(parametersArr[0].trim());
		requestedInfo = RequestedInfo.valueOf(parametersArr[1].toUpperCase());
		minReal = Double.parseDouble(parametersArr[2]);
		minIm = Double.parseDouble(parametersArr[3]);
		maxReal = Double.parseDouble(parametersArr[4]);
		maxIm = Double.parseDouble(parametersArr[5]);
		imageW = Integer.parseInt(parametersArr[6]);
		imageH = Integer.parseInt(parametersArr[7]);
		maxIte = Integer.parseInt(parametersArr[8]);
	}

	public RequestType getRequestType() {
		return requestType;
	}
	
	public RequestedInfo getRequestedInfo() {
		return requestedInfo;
	}

}
