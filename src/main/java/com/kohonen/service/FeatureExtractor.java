package com.kohonen.service;

import java.awt.image.BufferedImage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kohonen.model.Contour;
import com.kohonen.model.FeatureSpace;
import com.kohonen.model.Histogram;
import com.kohonen.service.edge.ContourExtractor;
import com.kohonen.service.edge.GradientExtractor;
import com.kohonen.service.hsb.HSBExtractor;
import com.kohonen.service.util.ImageUtil;

@Service
public class FeatureExtractor {

	public static final int THUMB_DIM = 64;

	private ImageUtil imageUtil;
	private ContourExtractor contourExtractor;
	private HSBExtractor hsbExtractor;
	private GradientExtractor gradientExtractor;

	@Autowired
	public FeatureExtractor(ImageUtil imageUtil, ContourExtractor contourExtractor, HSBExtractor hsbExtractor, GradientExtractor gradientExtractor) {
		this.contourExtractor = contourExtractor;
		this.hsbExtractor = hsbExtractor;
		this.imageUtil = imageUtil;
		this.gradientExtractor = gradientExtractor;
	}

	public FeatureSpace extractValues(BufferedImage image) {

		float[] features = new float[FeatureSpace.FEATURES_COUNT];

		int[][] grayScale = imageUtil.toGrayScale(image);
		Contour contour = contourExtractor.extractSobelContour(grayScale);
		Histogram gradientHistogram = gradientExtractor.extractValues(contour);
		float objectSurface = contour.getObjectSurface();
		float regionSurface = contour.getRegionSurface();
		float ratio = objectSurface / regionSurface;
		Histogram hueHistogram = new Histogram(FeatureSpace.HSB_BINS, 0f, 1f);
		Histogram saturationHistogram = new Histogram(FeatureSpace.HSB_BINS, 0f, 1f);
		Histogram brightnessHistogram = new Histogram(FeatureSpace.HSB_BINS, 0f, 1f);
		hsbExtractor.extractHSBHistograms(image.getRaster(), hueHistogram, saturationHistogram, brightnessHistogram, contour);

		int idx = 0;
		for (int i = 0; i < FeatureSpace.HSB_BINS; i++) {
			features[idx + i] = hueHistogram.getNormalizedValues()[i];
		}

		idx += FeatureSpace.HSB_BINS;
		for (int i = 0; i < FeatureSpace.HSB_BINS; i++) {
			features[idx + i] = saturationHistogram.getNormalizedValues()[i];
		}

		idx += FeatureSpace.HSB_BINS;
		for (int i = 0; i < FeatureSpace.HSB_BINS; i++) {
			features[idx + i] = brightnessHistogram.getNormalizedValues()[i];
		}
		idx += FeatureSpace.HSB_BINS;
		for (int i = 0; i < FeatureSpace.GRADIENT_BINS; i++) {
			features[idx + i] = gradientHistogram.getNormalizedValues()[i];
		}
		idx += FeatureSpace.GRADIENT_BINS;
		features[idx] = ratio;

		return new FeatureSpace(features);
	}

}
