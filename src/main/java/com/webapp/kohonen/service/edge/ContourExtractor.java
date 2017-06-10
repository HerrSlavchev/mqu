package com.webapp.kohonen.service.edge;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapp.kohonen.model.Contour;
import com.webapp.kohonen.model.EdgePoint;
import com.webapp.kohonen.service.util.MatrixCropUtil;

@Service
public class ContourExtractor {

	private EdgePointExtractor edgePointExtractor;

	private ShellExtractor shellExtractor;

	private ShellReinforcer shellReinforcer;

	private MatrixCropUtil matrixCropUtil;

	@Autowired
	public ContourExtractor(EdgePointExtractor edgePointExtractor, ShellExtractor shellExtractor,
			ShellReinforcer shellReinforcer, MatrixCropUtil matrixCropUtil) {
		this.edgePointExtractor = edgePointExtractor;
		this.shellExtractor = shellExtractor;
		this.shellReinforcer = shellReinforcer;
		this.matrixCropUtil = matrixCropUtil;
	}

	public Contour extractSobelContour(int[][] grayscaleImage) {

		EdgePoint[][] edgePoints = edgePointExtractor.extractSobelPoints(grayscaleImage);

		EdgePoint[][] croppedEdgePoints = matrixCropUtil.cropMatrix(edgePoints);

		int width = croppedEdgePoints.length;
		int height = croppedEdgePoints[0].length;
		EdgePoint[][] shell = shellExtractor.findShell(croppedEdgePoints);
		EdgePoint[][] reinforcedShell = shellReinforcer.reinforce(shell);

		
		List<List<EdgePoint>> rows = new ArrayList<>();

		for (int y = 0; y < height; y++) {
			List<EdgePoint> pts = new ArrayList<>();
			for (int x = 0; x < width; x++) {
				if (reinforcedShell[x][y] != null) {
					pts.add(reinforcedShell[x][y]);
				}
			}
			if (pts.size() <= 1) {
				continue;
			}
			if (pts.size() % 2 == 1) {
				pts.remove(pts.size() / 2);
			}
			rows.add(pts);
		}

		EdgePoint[][] rowsWithSobelPoints = new EdgePoint[rows.size()][];
		for (int y = 0; y < rows.size(); y++) {
			List<EdgePoint> pts = rows.get(y);
			rowsWithSobelPoints[y] = new EdgePoint[pts.size()];
			pts.toArray(rowsWithSobelPoints[y]);
		}

		return new Contour(rowsWithSobelPoints);
	}

}
