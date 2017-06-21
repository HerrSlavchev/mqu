package com.webapp.kohonen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapp.kohonen.model.Document;
import com.webapp.kohonen.model.Network;
import com.webapp.kohonen.model.NetworkSpecification;
import com.webapp.kohonen.persistence.NetworkStorage;

@Service
public class PersistentNetworkService implements NetworkService {

	private KohonenEngine engine;
	private NetworkStorage storage;

	private Network network;

	@Autowired
	public PersistentNetworkService(KohonenEngine engine, NetworkStorage storage) {
		this.engine = engine;
		this.storage = storage;
	}

	public void prepareNetwork(NetworkSpecification networkSpec, List<Document> trainingData) {
		network = storage.getNetwork(networkSpec);
		if (network == null) {
			network = engine.createNetwork(networkSpec, trainingData);
			storage.saveNetwork(network, networkSpec);
		}
	}

	public Network getNetwork() {
		return network;
	}
}
