package com.webapp.kohonen.service;

import java.awt.image.BufferedImage;

import com.webapp.kohonen.model.QueryResult;

public interface QueryService {

	public QueryResult findSimilarImages(BufferedImage image);

}
