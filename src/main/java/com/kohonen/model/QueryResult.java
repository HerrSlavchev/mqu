package com.kohonen.model;

import java.util.List;

public class QueryResult {

	private String error;
	
	private FeatureSpace exampleFeatures;
	
	private List<Document> documents;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public FeatureSpace getExampleFeatures() {
		return exampleFeatures;
	}

	public void setExampleFeatures(FeatureSpace exampleFeatures) {
		this.exampleFeatures = exampleFeatures;
	}

}
