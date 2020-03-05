package client;

import compute.Task;

public class Adding implements Task{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double x = 0.0;
    private double y = 0.0;

    public Adding(double x, double y){
	this.x = x;
	this.y = y;
    }

    public Double execute(){
	
	return Double.valueOf(x+y);
	
    }

}
