package com.webapp.kohonen.service.edge;

import org.springframework.stereotype.Component;

import com.webapp.kohonen.model.EdgePoint;

@Component
public class SobelFilter implements EdgePointFilter {

	
	private static final int[][] X_FILTER = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };
	private static final int[][] Y_FILTER = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };

	public static final int SOBEL_TRESHHOLD = 63;
	public static final int SOBEL_FILTER_DIMENSION = X_FILTER.length;
	public static final int SOBEL_CENTER = SOBEL_FILTER_DIMENSION / 2;
	
	/**
	 * 
	 * @param grayscaleImage representation of the image in gray scale via NxM matrix, where N is number of columns and M rows
	 * @param x the x-coordinate of the pixel
	 * @param y the y-coordinate of the pixel
	 * @return a EdgePoint if the gradient is above the SOBEL_TRESHOLD, otherwise null
	 */
	public EdgePoint apply(int[][] grayscaleImage, int x, int y) {

		EdgePoint res = null;

		int gradX = 0;
		int gradY = 0;
		if(x + SOBEL_FILTER_DIMENSION <= grayscaleImage.length && y + SOBEL_FILTER_DIMENSION <= grayscaleImage[0].length) {
			for (int i = 0; i < SOBEL_FILTER_DIMENSION; i++) {
				for (int j = 0; j < SOBEL_FILTER_DIMENSION; j++) {
					gradX += grayscaleImage[x + i][y + j] * X_FILTER[i][j];
					gradY += grayscaleImage[x + i][y + j] * Y_FILTER[i][j];
				}
			}
		}
		
		int gradient = Math.abs(gradX) + Math.abs(gradY);
		if (gradient > SOBEL_TRESHHOLD) {
			int spRow = y + SOBEL_CENTER;
			int spCol = x + SOBEL_CENTER;

			float spTheta;
			if (gradX == 0) {
				if (gradY == 0) {
					spTheta = 0;
				} else if (gradY < 0) {
					spTheta = (float) -Math.PI / 2;
				} else {
					spTheta = (float) Math.PI / 2;
				}
			} else {
				spTheta = (float) Math.atan(gradY / gradX);
			}
			spTheta += Math.PI / 2;

			res = new EdgePoint(spRow, spCol, gradX, gradY, gradient, spTheta);
		}
		
		return res;
	}
}
