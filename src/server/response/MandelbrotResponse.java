package server.response;

import server.Request;
import server.mandelbrot.Mandelbrot;

public class MandelbrotResponse extends Response {
	public MandelbrotResponse(String responseText) {
		super(responseText);
	}

	public MandelbrotResponse(Request recievedRequest) {
		super(recievedRequest);
		String mandelbrot = Mandelbrot.generate(recievedRequest.minReal, recievedRequest.minIm, recievedRequest.maxReal, recievedRequest.maxIm, recievedRequest.imageW, recievedRequest.imageH, recievedRequest.maxIte);
		responseText = mandelbrot;
	}
}
