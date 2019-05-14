package server.response;

import server.Request;
import server.Request.RequestType;
import server.Request.RequestedInfo;

public class ResponseFactory {
	private Request recievedRequest;
	private Response response;
	
	public ResponseFactory(Request recievedRequest) {
		this.recievedRequest = recievedRequest;
	}
	
	public Response getResponse() {
		if (recievedRequest.getRequestType() == RequestType.GET) {
			response = generateGetResponse();
		}
		
		return response;
	}

	private Response generateGetResponse() {
		if (recievedRequest.getRequestedInfo().equals(RequestedInfo.MANDELBROT))
			response = generateMandelbrotResponse();
		return response;
	}

	private Response generateMandelbrotResponse() {
		response = new MandelbrotResponse(recievedRequest);
		return response;
	}
	
}
