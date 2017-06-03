package com.kohonen.model;

public class FeatureSpace {

	public static final int HSB_BINS = 16;
    public static final int GRADIENT_BINS = 16;
    public static final int OTHER_BINS = 1;
    public static final int FEATURES_COUNT = HSB_BINS*3 + GRADIENT_BINS + OTHER_BINS;

    private final float[] features;

    public FeatureSpace(float[] features) {
        this.features = features;
    }
    
    public float[] getFeatures(){
        return features;
    }
}
