package com.webapp.kohonen.model;

public class Histogram {
	
	private final float[] values;
	private float[] normalizedValues;
	private final float lowerBoundary;
	private final float upperBoundary;
	
	public Histogram (int binCount, float lowerBoundary, float upperBoundary) {
		values = new float[binCount];
		this.lowerBoundary = lowerBoundary;
		this.upperBoundary = upperBoundary;
	}
	
	public void addValue(float value) {
		int binIdx = findBinIdx(value);
		values[binIdx]++;
	}
	
	public float[] getNormalizedValues() {
		if(normalizedValues == null) {
			evalNormalizedValues();
		}
		return normalizedValues;
	}
	
	private void evalNormalizedValues() {
		float sum = 0;
		normalizedValues = new float[values.length];
		for(int i = 0; i < values.length; i++) {
			sum += values[i];
		}
		for(int i = 0; i < values.length; i++) {
			normalizedValues[i] = values[i] / sum;
		}
	}
	
	public float[] getValues() {
		return values;
	}
	
	private int findBinIdx(float value) {
		
		int binIdx;
		
		float range = upperBoundary - lowerBoundary;
		float binRange = range / values.length;
		binIdx = (int) ((value - lowerBoundary) / binRange);
		
		if (binIdx < 0) {
			binIdx = 0;
		} else if (binIdx >= values.length) {
			binIdx = values.length - 1;
		}
		
		return binIdx;
	}

}
