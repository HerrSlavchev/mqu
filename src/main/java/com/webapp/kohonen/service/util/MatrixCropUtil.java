package com.webapp.kohonen.service.util;

import org.springframework.stereotype.Component;

import com.webapp.kohonen.model.EdgePoint;

@Component
public class MatrixCropUtil {

	public EdgePoint[][] cropMatrix(EdgePoint[][] original) {

		EdgePoint[][] res;
		
		int maxI = Integer.MAX_VALUE;
		int minI = Integer.MIN_VALUE;
		int maxJ = Integer.MAX_VALUE;
		int minJ = Integer.MIN_VALUE;

		for (int i = 0; i < original.length; i++) {
			for (int j = 0; j < original[0].length; j++) {
				if (original[i][j] != null) {
					maxI = i;
					break;
				}
			}
		}
		for (int i = original.length - 1; i >= 0; i--) {
			for (int j = 0; j < original[0].length; j++) {
				if (original[i][j] != null) {
					minI = i;
					break;
				}
			}
		}
		for (int j = 0; j < original[0].length; j++) {
			for (int i = 0; i < original.length; i++) {
				if (original[i][j] != null) {
					maxJ = j;
					break;
				}
			}
		}
		for (int j = original[0].length - 1; j >= 0; j--) {
			for (int i = 0; i < original.length; i++) {
				if (original[i][j] != null) {
					minJ = j;
					break;
				}
			}
		}

		int dimI = (maxI - minI) + 1;
		int dimJ = (maxJ - minJ) + 1;
		res = new EdgePoint[dimI][dimJ];
		
		for(int i = minI; i <= maxI; i++) {
			for(int j = minJ; j <= maxJ; j++) {
				res[i - minI][j - minJ] = original[i][j];
			}
		}
		
		return res;
	}
	
	
}
