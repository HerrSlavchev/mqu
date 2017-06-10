package com.webapp.kohonen.service.hsb;

import java.awt.Color;
import java.awt.image.Raster;

import org.springframework.stereotype.Service;

import com.webapp.kohonen.model.Contour;
import com.webapp.kohonen.model.EdgePoint;
import com.webapp.kohonen.model.Histogram;

@Service
public class HSBExtractor {

	public void extractHSBHistograms(Raster raster, Histogram hue, Histogram saturation, Histogram brightness) {
		int[] rgba = new int[4];
		float[] hsb = new float[3];
		for (int row = 0; row < raster.getHeight(); row++) {
			for (int col = 0; col < raster.getWidth(); col++) {
				extractValues(raster, col, row, hue, saturation, brightness, rgba, hsb);
			}
		}
	}

	public void extractHSBHistograms(Raster raster, Histogram hue, Histogram saturation, Histogram brightness,
			Contour contour) {
		int[] rgba = new int[4];
		float[] hsb = new float[3];
		EdgePoint[][] edgePoints = contour.getEdgePoints();
		
		for (int i = 0; i < edgePoints.length; i++ ) {
			EdgePoint[] rowBoundary = edgePoints[i];
			int row = rowBoundary[0].getRow();
			for(int j = 0; j < rowBoundary.length / 2; j+= 2) {
				for(int col = rowBoundary[j].getCol(); col <= rowBoundary[j+1].getCol(); col++) {
					extractValues(raster, col, row, hue, saturation, brightness, rgba, hsb);
				}
			}
		}
	}

	private void extractValues(Raster raster, int col, int row, Histogram hue, Histogram saturation,
			Histogram brightness, int[] rgba, float[] hsb) {
		raster.getPixel(col, row, rgba);
		Color.RGBtoHSB(rgba[0], rgba[1], rgba[2], hsb);
		hue.addValue(hsb[0]);
		saturation.addValue(hsb[1]);
		brightness.addValue(hsb[2]);
	}
	
}
