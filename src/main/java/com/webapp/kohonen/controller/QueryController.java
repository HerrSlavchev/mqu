package com.webapp.kohonen.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.kohonen.model.QueryResult;
import com.webapp.kohonen.service.QueryService;

@RestController
public class QueryController {

	final Logger logger = LoggerFactory.getLogger(QueryController.class);
	
	private QueryService service;
	
	public QueryController(QueryService service) {
		this.service = service;
	}
	
	@PostMapping("/search")
	public QueryResult search(@RequestParam("file") MultipartFile file) {
		QueryResult res = new QueryResult();
		String error = "";
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(file.getBytes());
			BufferedImage image = ImageIO.read(in);
			res = service.findSimilarImages(image);
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
	
}
