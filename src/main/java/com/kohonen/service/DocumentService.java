package com.kohonen.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.kohonen.model.Document;
import com.kohonen.model.FeatureSpace;
import com.kohonen.service.util.FileUtil;
import com.kohonen.service.util.ImageUtil;

@Service
public class DocumentService {

	final Logger logger = LoggerFactory.getLogger(DocumentService.class);

	private FileUtil fileUtil;
	private ImageUtil imageUtil;
	private FeatureExtractor featureExtractor;

	private List<Document> trainingDataCache = new ArrayList<>();
	
	public DocumentService(FileUtil fileUtil, ImageUtil imageUtil, FeatureExtractor featureExtractor) {
		this.fileUtil = fileUtil;
		this.imageUtil = imageUtil;
		this.featureExtractor = featureExtractor;
	}

	public List<Document> getTrainingData() {
		
		if(trainingDataCache == null || trainingDataCache.isEmpty()) {
			try {
				String[] paths = { "classpath:static/images/*.jpg", "classpath:static/images/*.png" };
				for (String path : paths) {
					Resource[] resources = fileUtil.getAllFiles(path);
					for (Resource resource : resources) {
						BufferedImage image = imageUtil.readImage(resource.getFile().toPath());
						FeatureSpace featureSpace = featureExtractor.extractValues(image);
						Document doc = new Document(resource.getFilename(), featureSpace);
						trainingDataCache.add(doc);
					}
				}
			} catch (IOException eIO) {
				logger.warn("Failed to collect resources.", eIO);
			}
		}
		
		return trainingDataCache;
	}
}
