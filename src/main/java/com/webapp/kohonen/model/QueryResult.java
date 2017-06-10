package com.webapp.kohonen.model;

import java.util.List;

public class QueryResult {

	private String error;
	
	private FeatureSpace exampleFeatures;
	
	private List<Node> nodes;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public FeatureSpace getExampleFeatures() {
		return exampleFeatures;
	}

	public void setExampleFeatures(FeatureSpace exampleFeatures) {
		this.exampleFeatures = exampleFeatures;
	}

}
