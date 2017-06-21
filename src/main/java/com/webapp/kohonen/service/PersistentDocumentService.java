package com.webapp.kohonen.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.webapp.kohonen.model.Document;
import com.webapp.kohonen.model.FeatureSpace;
import com.webapp.kohonen.persistence.DocumentStorage;
import com.webapp.kohonen.service.util.FileUtil;
import com.webapp.kohonen.service.util.ImageUtil;

@Service
public class PersistentDocumentService implements DocumentService {

	final Logger logger = LoggerFactory.getLogger(PersistentDocumentService.class);

	private FileUtil fileUtil;
	private ImageUtil imageUtil;
	private FeatureExtractor featureExtractor;
	private DocumentStorage storage;

	private List<Document> trainingDataCache = new ArrayList<>();
	
	public PersistentDocumentService(FileUtil fileUtil, ImageUtil imageUtil, FeatureExtractor featureExtractor, DocumentStorage storage) {
		this.fileUtil = fileUtil;
		this.imageUtil = imageUtil;
		this.featureExtractor = featureExtractor;
		this.storage = storage;
	}

	public List<Document> getTrainingData() {
		
		if(trainingDataCache == null || trainingDataCache.isEmpty()) {
			try {
				String[] paths = { "classpath:static/images/*.jpg", "classpath:static/images/*.png" };
				for (String path : paths) {
					Resource[] resources = fileUtil.getAllFiles(path);
					for (Resource resource : resources) {
						Document doc = extractDocument(resource);
						trainingDataCache.add(doc);
					}
				}
			} catch (IOException eIO) {
				logger.warn("Failed to collect resources.", eIO);
			}
		}
		
		return trainingDataCache;
	}
	
	private Document extractDocument(Resource resource) throws IOException {
		String path = resource.getFilename();
		Document doc = storage.getDocument(path);
		if (doc == null) {
			BufferedImage image = imageUtil.readImage(resource.getFile().toPath());
			FeatureSpace featureSpace = featureExtractor.extractValues(image);
			doc = new Document(path, featureSpace);
			storage.saveDocument(doc);
		}
		return doc;
	}
}
