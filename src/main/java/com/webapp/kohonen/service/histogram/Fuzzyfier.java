package com.webapp.kohonen.service.histogram;

import java.util.HashMap;
import java.util.Map;

public class Fuzzyfier {

	private int bins = 0;
    float binLength = 1;
    private final Map<Float, Distribution> cache = new HashMap<>();

    public Fuzzyfier(int bins, float normalizationMinimum, float normalizationMaximum) {
        this.bins = bins;
        binLength = (normalizationMaximum - normalizationMinimum) / bins;
    }

    public void addValueToHistogramm(float value, float[] histogramm) {
    	Distribution res;
        Distribution cached = cache.get(value);
        if (cached != null) {
            res = cached;
        } else {
            res = extractValuesForIndex(value);
            cache.put(value, res);
        }
        updateHistogram(res, histogramm);
    }

    private Distribution extractValuesForIndex(float value) {

        Distribution res = new Distribution();
        float relativeIdx = value / binLength;
        int resIdx = (int) relativeIdx;
        res.lowerBin = resIdx;
        if (value % bins == 0) {
            res.lowerBinValue = 1;
        } else if (resIdx >= bins - 1) {
            res.lowerBin = bins - 1;
            res.lowerBinValue = 1;
        } else {
            float distToLowerBin = relativeIdx - resIdx;
            float distToUpperBin = 1 - distToLowerBin;
            res.lowerBinValue = distToUpperBin;
            res.upperBinValue += distToLowerBin;
        }

        return res;
    }

    private void updateHistogram(Distribution dist, float[] target) {
        int lowerIdx = dist.lowerBin;
        target[lowerIdx] += dist.lowerBinValue;
        if (dist.upperBinValue != 0) {
            target[lowerIdx + 1] += dist.upperBinValue;
        }
    }

    public void setBins(int bins) {
    	this.bins = bins;
    }
    
    public static void normalizeHistogramm(int norm, float[] histogramm){
        if (norm != 0 && norm != 1) {
            for (int i = 0; i < histogramm.length; i++) {
                histogramm[i] /= norm;
            }
        }
    }
    
    private class Distribution {
        public int lowerBin;
        public float lowerBinValue;
        public float upperBinValue;
    }
    
    public static float[] normalize(float[] feed, int start, int end, int upperBound){
        float[] res = new float[end-start];
        float max = feed[0];
        for(int i = 1; i < feed.length; i++){
            if(feed[i] > max) {
                max = feed[i];
            }
        }
        float coeff = upperBound / max;
        for(int i = 0; i < feed.length; i++){
            res[i] = feed[i]/coeff;
        }
        return res;
    }
}
