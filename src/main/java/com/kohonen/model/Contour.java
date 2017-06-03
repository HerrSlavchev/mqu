package com.kohonen.model;

public class Contour {

	private EdgePoint[][] edgePoints;
    private int minRow;
    private int maxRow;
    private int minCol;
    private int maxCol;
    private int regionSurface;
    private int objectSurface;
    
    public Contour(EdgePoint[][] edgePoints) {
        this.edgePoints = edgePoints;
        
        minRow = edgePoints[0][0].getRow();
        maxRow = edgePoints[edgePoints.length - 1][0].getRow();
        
        minCol = edgePoints[0][0].getCol();
        maxCol = -1;
        for(int i = 0; i < edgePoints.length; i++) {
        	EdgePoint[] arr = edgePoints[i];
        	if(arr[0].getCol() < minCol) {
        		minCol = arr[0].getCol();
        	}
        	if(arr[arr.length - 1].getCol() > maxCol) {
        		maxCol = arr[arr.length - 1].getCol();
        	}
        }
        
        objectSurface = 0;
        for (int row = 0; row < edgePoints.length; row++) {
            EdgePoint[] arr = edgePoints[row];
            int start = minCol;
            int end = maxCol;
            for (int i = 0; i < arr.length; i += 2) {
                start = arr[i].getCol();
                if (i + 1 < arr.length) {
                    end = arr[i + 1].getCol();
                } else {
                    end = maxCol;
                }
                objectSurface += (end - start);
            }
        }
        
        regionSurface = (maxRow - minRow) * (maxCol - minCol);
    }
    
    public EdgePoint[][] getEdgePoints() {
        return edgePoints;
    }

    public int getMinRow() {
        return minRow;
    }

    public int getMaxRow() {
        return maxRow;
    }

    public int getMinCol() {
        return minCol;
    }

    public int getMaxCol() {
        return maxCol;
    }
    
    public int getRegionSurface() {
        return regionSurface;
    }
    
    public int getObjectSurface() {
        return objectSurface;
    }
}
