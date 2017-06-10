package com.webapp.kohonen.service.distance;

import com.webapp.kohonen.model.FeatureSpace;

public interface DistanceMeasure {

	public float dist(FeatureSpace featureSpaceA, FeatureSpace featureSpaceB);
	
}
