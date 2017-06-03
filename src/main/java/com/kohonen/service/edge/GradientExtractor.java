package com.kohonen.service.edge;

import org.springframework.stereotype.Service;

import com.kohonen.model.Contour;
import com.kohonen.model.EdgePoint;
import com.kohonen.model.FeatureSpace;
import com.kohonen.model.Histogram;

@Service
public class GradientExtractor {

	public Histogram extractValues(Contour contour) {
		Histogram gradientHistogram = new Histogram(FeatureSpace.GRADIENT_BINS, 0f, (float) Math.PI);

		EdgePoint[][] edgePoints = contour.getEdgePoints();

		for (int row = 0; row < edgePoints.length; row++) {
			for (EdgePoint point : edgePoints[row]) {
				gradientHistogram.addValue(point.getTheta());
			}
		}

		//shift the histogram so the highest bin is always first (this fixes bias from rotation)
		int maxIdx = 0;
		float[] values = gradientHistogram.getValues();
		for (int i = 0; i < values.length; i++) {
			if(values[i] > values[maxIdx]) {
				maxIdx = i;
			}
		}
		float[] tmpValues = new float[values.length];
		for(int i = 0; i < values.length; i++) {
			tmpValues[i] = values[(i + maxIdx) % values.length];
		}
		for(int i = 0; i < values.length; i++) {
			values[i]  = tmpValues[i];
		}

		return gradientHistogram;
	}
}
