package com.webapp.kohonen.service.edge;

import org.springframework.stereotype.Component;

import com.webapp.kohonen.model.EdgePoint;

@Component
public class ShellReinforcer {
	
	private static final int REINFORCEMENT_RANGE = 10;

	/**
	 * 
	 * @param shell NxM matrix with shell points, where N is number of columns and M rows
	 * @return NxM matrix with the original shell points and additional points in order to make the shell consistent
	 */
	public EdgePoint[][] reinforce(EdgePoint[][] shell) {
		
		EdgePoint[][] res;
		
		int width = shell.length;
		int height = shell[0].length;
		res = new EdgePoint[width][height];
		
		for (int y = 0; y < height - 1; y++) {
			int nextRow = y + 1;
			for (int x = 0; x < width; x++) {
				EdgePoint orig = shell[x][y];
				if (shell[x][y] != null) {
					res[x][y] = orig;					
					int scanStart = x - REINFORCEMENT_RANGE;
					int scanEnd = x + REINFORCEMENT_RANGE;
					if (scanStart < 0) {
						scanStart = 0;
					}
					if (scanEnd > width) {
						scanEnd = width;
					}
					boolean exists = false;
					for (int scanCol = scanStart; scanCol < scanEnd; scanCol++) {
						if (shell[scanCol][nextRow] != null) {
							exists = true;
							break;
						}
					}
					if (false == exists) {
						res[x][nextRow] = orig;
					}
				}
			}
		}
		
		return res;
	}
}
