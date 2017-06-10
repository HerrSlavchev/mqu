package com.webapp.kohonen.model;

public class EdgePoint {

    private int row;
    private int col;

    private int gradX;
    private int gradY;
    private int grad;
    private float theta;

    public EdgePoint(int row, int col, int gradX, int gradY, int grad, float theta) {
    	
    	this.row = row;
    	this.col = col;
    	
    	this.gradX = gradX;
    	this.gradY = gradY;
    	this.grad = grad;
    	this.theta = theta;
    }
    
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getGradX() {
        return gradX;
    }

    public int getGradY() {
        return gradY;
    }

    public int getGrad() {
        return grad;
    }

    public float getTheta() {
        return theta;
    }

}
