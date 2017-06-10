package com.webapp.kohonen.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.kohonen.model.Document;
import com.webapp.kohonen.model.NetworkSpecification;
import com.webapp.kohonen.model.Node;
import com.webapp.kohonen.service.DocumentService;
import com.webapp.kohonen.service.NetworkService;

@RestController
public class NetworkController {
	
	private NetworkService networkService;
	private DocumentService documentService;
	
	@Autowired
	public NetworkController(NetworkService networkService, DocumentService documentService) {
		this.networkService = networkService;
		this.documentService = documentService;
	}
	
	@PutMapping("/configure")
	public List<Node> prepareNetwork(@RequestBody NetworkSpecification networkSpec) {
		List<Document> trainingData = documentService.getTrainingData();
		networkService.prepareNetwork(networkSpec, trainingData);
		List<Node> nodes = new ArrayList<>();
		for(Node[] arr : networkService.getNetwork().getNodes()){
			nodes.addAll(Arrays.asList(arr));
		}
		return nodes;
	}

}
