package com.kohonen.service.distance;

import com.kohonen.model.FeatureSpace;

public interface DistanceMeasure {

	public float dist(FeatureSpace featureSpaceA, FeatureSpace featureSpaceB);
	
}
