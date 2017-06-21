package com.webapp.kohonen.service;

import java.util.List;

import com.webapp.kohonen.model.Document;
import com.webapp.kohonen.model.Network;
import com.webapp.kohonen.model.NetworkSpecification;

public interface NetworkService {

	public Network getNetwork();
	
	public void prepareNetwork(NetworkSpecification networkSpec, List<Document> trainingData);
	
}
