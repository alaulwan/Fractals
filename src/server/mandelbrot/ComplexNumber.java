package server.mandelbrot;

public class ComplexNumber {
	private final double real;
    private final double imag;
    
    public ComplexNumber(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }
    
    public double absolute() {
        return Math.hypot(real, imag);
    }
    
    public static ComplexNumber add(ComplexNumber z1, ComplexNumber z2) {
        double real = z1.real + z2.real;
        double imag = z1.imag + z2.imag;
        return new ComplexNumber(real, imag);
    }
    
    public static ComplexNumber multiply(ComplexNumber z1, ComplexNumber z2) {
        double real = z1.real * z2.real - z1.imag * z2.imag;
        double imag = z1.real * z2.imag + z1.imag * z2.real;
        return new ComplexNumber(real, imag);
    }
    
    public ComplexNumber square() {
        return multiply(this, this);
    }
    

}
