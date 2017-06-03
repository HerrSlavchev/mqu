package com.kohonen.service.edge;

import org.springframework.stereotype.Component;

import com.kohonen.model.EdgePoint;

@Component
public class ShellExtractor {

	/**
	 * 
	 * @param edgePoints NxM matrix with detected points, where N is number of columns and M rows
	 * @return NxM matrix containing only the first and last point on each row/column
	 */
	public EdgePoint[][] findShell(EdgePoint[][] edgePoints) {
		
		EdgePoint[][] res;
		
		int width = edgePoints.length;
		int height = edgePoints[0].length;
		int minCol = Integer.MAX_VALUE;
		int minRow = Integer.MAX_VALUE;
		int maxCol = 0;
		int maxRow = 0;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				EdgePoint sobelPoint = edgePoints[x][y];
				if (sobelPoint != null) {
					int spRow = y;
					int spCol = x;
					if (minCol > spCol) {
						minCol = spCol;
					}
					if (maxCol < spCol) {
						maxCol = spCol;
					}
					if (minRow > spRow) {
						minRow = spRow;
					}
					if (maxRow < spRow) {
						maxRow = spRow;
					}
				}
			}
		}

		res = new EdgePoint[width][height];
		// for each row: keep only the first and last EdgePoint
		for (int y = minRow; y < maxRow; y++) {
			for (int x = minCol; x < maxCol; x++) {
				if (edgePoints[x][y] != null) {
					res[x][y] = edgePoints[x][y];
					break;
				}
			}
			for (int x = maxCol - 1; x >= minCol; x--) {
				if (edgePoints[x][y] != null) {
					res[x][y] = edgePoints[x][y];
					break;
				}
			}
		}
		// for each column: keep only the top and bottom point
		for (int x = minCol; x < maxCol; x++) {
			for (int y = minRow; y < maxRow; y++) {
				if (edgePoints[x][y] != null) {
					res[x][y] = edgePoints[x][y];
					break;
				}
			}
			for (int y = maxRow - 1; y >= minRow; y--) {
				if (edgePoints[x][y] != null) {
					res[x][y] = edgePoints[x][y];
					break;
				}
			}
		}
		
		return res;
	}
}
