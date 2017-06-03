package com.kohonen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kohonen.model.Document;
import com.kohonen.model.Network;
import com.kohonen.model.NetworkSpecification;

@Service
public class NetworkService {

	@Autowired
	private KohonenEngine engine;

	private Network network;

	public NetworkService(KohonenEngine engine) {
		this.engine = engine;
	}

	public void prepareNetwork(NetworkSpecification networkSpec, List<Document> trainingData) {
		network = engine.createNetwork(networkSpec, trainingData);
	}

	public Network getNetwork() {
		return network;
	}
}
