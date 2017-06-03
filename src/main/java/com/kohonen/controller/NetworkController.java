package com.kohonen.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kohonen.model.Document;
import com.kohonen.model.NetworkSpecification;
import com.kohonen.service.DocumentService;
import com.kohonen.service.NetworkService;

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
	public void prepareNetwork(@RequestBody NetworkSpecification networkSpec) {
		List<Document> trainingData = documentService.getTrainingData();
		networkService.prepareNetwork(networkSpec, trainingData);
	}

}
