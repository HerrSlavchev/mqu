package com.kohonen.model;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private int row;
	private int col;
	
	private FeatureSpace featureSpace = null;
	private List<Document> documents;
    
    public Node(FeatureSpace featureSpace, int row, int col) {
        this.featureSpace = featureSpace;
        this.row = row;
        this.col = col;
        documents = new ArrayList<>();
    }
    
    public float[] getFeatures() {
        return featureSpace.getFeatures();
    }
    
    public int getRow() {
    	return row;
    }
    
    public int getCol() {
    	return col;
    }
    
    public void addDocument(Document doc) {
    	documents.add(doc);
    }
    
    public List<Document> getDocuments() {
    	return documents;
    }
}
