package com.webapp.kohonen.service.edge;

import com.webapp.kohonen.model.EdgePoint;

public interface EdgePointFilter {

	public EdgePoint apply(int[][] grayscaleImage, int x, int y);
	
}
