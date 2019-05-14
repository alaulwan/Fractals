package server.mandelbrot;

public class Mandelbrot {
	
	public final static int boundary = 2;

	public static String generate(double minReal, double minIm, double maxReal, double maxIm, int imageW, int imageH, int maxIte)  {
    	double xStep = (maxReal - minReal)/(imageW-1);
        double yStep = (maxIm - minIm)/(imageH-1);
        StringBuilder sb = new StringBuilder((imageW * imageH));
        
        for (int i = 0; i < imageH; i++) {
            for (int j = 0; j < imageW; j++) {
                double x0 = minReal + (j* xStep);
                double y0 = minIm + (i* yStep);
                ComplexNumber c = new ComplexNumber(x0, y0);
                int gray = maxIte - checkC(c, maxIte);
                gray = 255 * gray/maxIte;
                sb.append(gray+" ");
            }
            sb.deleteCharAt(sb.length()-1).append("\r\n");
        }
        sb.delete(sb.length()-2, sb.length());
        return sb.toString();
    }
	
    private static int checkC(ComplexNumber c, int maxIte) {
    	ComplexNumber z = new ComplexNumber(0, 0);
        for (int currentIteration = 0; currentIteration < maxIte; currentIteration++) {
            z = ComplexNumber.add(z.square(), c);
        	if (z.absolute() > boundary)
            	return currentIteration;
        }
        return maxIte;
    }

    
}

