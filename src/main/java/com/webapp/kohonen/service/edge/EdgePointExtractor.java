package com.webapp.kohonen.service.edge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.webapp.kohonen.model.EdgePoint;

@Component
public class EdgePointExtractor {

	private EdgePointFilter edgePointFilter;
	
	@Autowired
	public EdgePointExtractor(EdgePointFilter edgePointFilter) {
		this.edgePointFilter = edgePointFilter;
	}
	
	public EdgePoint[][] extractSobelPoints(int[][] grayscaleImage) {
		int width = grayscaleImage.length;
		int height = 0;
		if (width > 0) {
			height = grayscaleImage[0].length;
		}

		EdgePoint[][] sobelPoints = new EdgePoint[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				EdgePoint sobelPoint = edgePointFilter.apply(grayscaleImage, x, y);
				if (sobelPoint != null) {
					sobelPoints[x][y] = sobelPoint;
				}
			}
		}
		
		return sobelPoints;
	}
}
