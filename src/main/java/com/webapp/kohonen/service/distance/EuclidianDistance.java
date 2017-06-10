package com.webapp.kohonen.service.distance;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.webapp.kohonen.model.FeatureSpace;

@Component
@Primary
public class EuclidianDistance implements DistanceMeasure {

	public float dist(FeatureSpace featureSpaceA, FeatureSpace featureSpaceB) {
        float res = 0;
        float diff = 0;
        for (int i = 0; i < featureSpaceA.getFeatures().length; i++) {
            diff = featureSpaceA.getFeatures()[i] - featureSpaceB.getFeatures()[i];
            res += diff * diff;
        }
        return (float) Math.sqrt(res);
    }
}
