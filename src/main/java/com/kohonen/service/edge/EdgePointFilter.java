package com.kohonen.service.edge;

import com.kohonen.model.EdgePoint;

public interface EdgePointFilter {

	public EdgePoint apply(int[][] grayscaleImage, int x, int y);
	
}
