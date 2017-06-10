package com.webapp.kohonen.model;

public class Network {

	private Node[][] nodes = new Node[0][0];
    
    public Network(int rows, int cols){
        nodes = new Node[rows][cols];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                float[] features = new float[FeatureSpace.FEATURES_COUNT];
                FeatureSpace fs = new FeatureSpace(features);
                nodes[i][j] = new Node(fs, i, j);
            }
        }
    }
    
    public Node[][] getNodes(){
        return nodes;
    }
}
