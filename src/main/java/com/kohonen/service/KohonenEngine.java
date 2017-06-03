package com.kohonen.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kohonen.model.Document;
import com.kohonen.model.FeatureSpace;
import com.kohonen.model.Network;
import com.kohonen.model.NetworkSpecification;
import com.kohonen.model.Node;
import com.kohonen.service.distance.DistanceMeasure;
import com.kohonen.service.distance.EuclidianDistance;

@Service
public class KohonenEngine {

	private DistanceMeasure measure = new EuclidianDistance();

	@Autowired
	public KohonenEngine(DistanceMeasure measure) {
		this.measure = measure;
	}

	public Network createNetwork(NetworkSpecification networkSpec, List<Document> trainingData) {

		Collections.shuffle(trainingData);
		double learningRate = networkSpec.getLearningRate();
		double radius = networkSpec.getRadius();

		Network network = new Network(networkSpec.getRows(), networkSpec.getCols());
		for (int i = 0; i < networkSpec.getEpochs(); i++) {
			double coeff = Math.exp(-i / 10);
			learningRate *= coeff;
			if (learningRate < 0.1) {
				learningRate = 0.1;
			}
			radius *= coeff;
			if (radius < 0.1) {
				radius = 0.1;
			}
			for (Document doc : trainingData) {
				processDocument(doc, network, learningRate, radius);
			}
		}
		for (Document doc : trainingData) {
			Node node = findClosestNode(doc, network);
			node.addDocument(doc);
		}

		return network;
	}

	public List<Node> findNodes(Document doc, Network network) {
		List<Node> res = new ArrayList<>();
		Map<Node, Float> mapDist = new HashMap<>();
		for (Node[] arr : network.getNodes()) {
			for (Node node : arr) {
				if (!node.getDocuments().isEmpty()) {
					float d = distDocToNode(doc, node);
					mapDist.put(node, d);
					res.add(node);
				}
			}
		}

		Collections.sort(res, new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				float d1 = mapDist.get(o1);
				float d2 = mapDist.get(o2);
				return d1 > d2 ? 1 : -1;
			}
		});

		return res;
	}

	private void processDocument(Document doc, Network network, double learningRate, double radius) {
		Node[][] nodes = network.getNodes();

		Node closestNode = findClosestNode(doc, network);
		int row = closestNode.getRow();
		int col = closestNode.getCol();
		for (int rowOffset = (int) (-radius); rowOffset < radius; rowOffset++) {
			for (int colOffset = (int) (-radius); colOffset < radius; colOffset++) {
				int futureRow = row + rowOffset;
				int futureCol = col + colOffset;
				int totalOffset = Math.abs(rowOffset) + Math.abs(colOffset);
				if (totalOffset < radius && futureRow >= 0 && futureRow < nodes.length && futureCol >= 0
						&& futureCol < nodes[futureRow].length) {
					int distSq = rowOffset * rowOffset + colOffset * colOffset;
					double correctionCoeff = Math.exp(-distSq / (2 * radius));
					applyCorrections(nodes[futureRow][futureCol], doc, learningRate * correctionCoeff);
				}
			}
		}
	}

	private Node findClosestNode(Document doc, Network network) {
		Node[][] structure = network.getNodes();

		Node closestNode = structure[0][0];
		float bestDist = distDocToNode(doc, closestNode);

		float currDist = -1;
		for (int i = 0; i < structure.length; i++) {
			for (int j = 0; j < structure[i].length; j++) {
				Node currNode = structure[i][j];
				currDist = distDocToNode(doc, currNode);
				if (currDist < bestDist) {
					bestDist = currDist;
					closestNode = currNode;
				}
			}
		}

		return closestNode;
	}

	private float distDocToNode(Document doc, Node node) {
		return measure.dist(new FeatureSpace(doc.getFeatures()), new FeatureSpace(node.getFeatures()));
	}

	private void applyCorrections(Node n, Document doc, double rate) {
		if (rate >= 0.1) {
			double old = 1 - rate;
			float[] featuresNode = n.getFeatures();
			float[] featuresDoc = doc.getFeatures();
			for (int i = 0; i < featuresNode.length; i++) {
				featuresNode[i] = (float) (old * featuresNode[i] + rate * featuresDoc[i]);
			}
		}
	}

}
