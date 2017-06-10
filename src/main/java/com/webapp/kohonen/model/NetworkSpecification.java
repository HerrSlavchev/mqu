package com.webapp.kohonen.model;

public class NetworkSpecification {

	private int rows;
	private int cols;
	private int epochs;
	private double learningRate;
	private double radius;
	
	public NetworkSpecification(int rows, int cols, int epochs, double learningRate, double radius) {
		this.rows = rows;
		this.cols = cols;
		this.epochs = epochs;
		this.learningRate = learningRate;
		this.radius = radius;
	}
	
	public NetworkSpecification() {
		
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public int getEpochs() {
		return epochs;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public double getRadius() {
		return radius;
	}
	
}
