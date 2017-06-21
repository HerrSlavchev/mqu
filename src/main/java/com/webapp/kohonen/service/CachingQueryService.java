package com.webapp.kohonen.service;

import java.awt.image.BufferedImage;
import java.util.List;

import org.springframework.stereotype.Service;

import com.webapp.kohonen.model.Document;
import com.webapp.kohonen.model.FeatureSpace;
import com.webapp.kohonen.model.Network;
import com.webapp.kohonen.model.Node;
import com.webapp.kohonen.model.QueryResult;
import com.webapp.kohonen.service.distance.DistanceMeasure;

@Service
public class CachingQueryService implements QueryService {
	
	private FeatureExtractor featureExtractor;
	private NetworkService networkService;
	private KohonenEngine kohonenEngine;
	private DistanceMeasure distanceMeasure;
	
	
	public CachingQueryService(FeatureExtractor featureExtractor, NetworkService networkService, KohonenEngine kohonenEngine, DistanceMeasure distanceMeasure) {
		this.featureExtractor = featureExtractor;
		this.networkService = networkService;
		this.kohonenEngine = kohonenEngine;
		this.distanceMeasure = distanceMeasure;
	}
	
	
	public QueryResult findSimilarImages(BufferedImage image) {
		FeatureSpace features = featureExtractor.extractValues(image);
		Document queryDoc = new Document("query", features);
		return search(queryDoc);
	}
	
	private QueryResult search(Document queryDoc) {
		QueryResult res = new QueryResult();
		Network network = networkService.getNetwork();
		List<Node> nodes = kohonenEngine.findNodes(queryDoc, network);
		for(Node node : nodes) {
			for (Document doc : node.getDocuments()) {
				float dist = distanceMeasure.dist(doc.getFeatureSpace(), queryDoc.getFeatureSpace());
				doc.setDistanceToExample(dist);
			}
		}
		res.setExampleFeatures(queryDoc.getFeatureSpace());
		res.setNodes(nodes);
		return res;
	}

}
