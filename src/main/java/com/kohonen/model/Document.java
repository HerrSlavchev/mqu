package com.kohonen.model;

public class Document {

	private String path = "";

	private float distanceToExample = 0;

	private final FeatureSpace featureSpace;

	public Document(String path, FeatureSpace featureSpace) {
		this.path = path;
		this.featureSpace = featureSpace;
	}

	public float[] getFeatures() {
		return featureSpace.getFeatures();
	}

	public FeatureSpace getFeatureSpace() {
		return featureSpace;
	}
	
	public String getPath() {
		return path;
	}

	public float getDistanceToExample() {
		return distanceToExample;
	}

	public void setDistanceToExample(float distanceToExample) {
		this.distanceToExample = distanceToExample;
	}

}
