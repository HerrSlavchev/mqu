package com.webapp.kohonen.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.kohonen.model.Document;
import com.webapp.kohonen.model.FeatureSpace;
import com.webapp.kohonen.model.Network;
import com.webapp.kohonen.model.Node;
import com.webapp.kohonen.model.QueryResult;
import com.webapp.kohonen.service.FeatureExtractor;
import com.webapp.kohonen.service.KohonenEngine;
import com.webapp.kohonen.service.NetworkService;
import com.webapp.kohonen.service.distance.DistanceMeasure;

@RestController
public class QueryController {

	final Logger logger = LoggerFactory.getLogger(QueryController.class);
	
	private FeatureExtractor featureExtractor;
	private NetworkService networkService;
	private KohonenEngine kohonenEngine;
	private DistanceMeasure distanceMeasure;
	
	public QueryController(FeatureExtractor featureExtractor, NetworkService networkService, KohonenEngine kohonenEngine, DistanceMeasure distanceMeasure) {
		this.featureExtractor = featureExtractor;
		this.networkService = networkService;
		this.kohonenEngine = kohonenEngine;
		this.distanceMeasure = distanceMeasure;
	}
	
	@PostMapping("/search")
	public QueryResult search(@RequestParam("file") MultipartFile file) {
		QueryResult res = new QueryResult();
		String error = "";
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(file.getBytes());
			BufferedImage image = ImageIO.read(in);
			FeatureSpace features = featureExtractor.extractValues(image);
			Document queryDoc = new Document("query", features);
			res = search(queryDoc);
		} catch (IOException eIO) {
			error = "Query resulted in IOException";
			logger.warn(error, eIO);
		} finally {
			if( in != null) {
				try {
					in.close();
				} catch (IOException eIO) {
					error = "Query resulted in IOException";
					logger.warn(error, eIO);
				}
			}
		}
		
		res.setError(error);
		return res;
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
