package com.kohonen.service.util;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

@Component
public class FileUtil {
	
	ResourcePatternResolver resourcePatternResolver;

	@Autowired
	public FileUtil(ResourcePatternResolver resourcePatternResolver) {
		this.resourcePatternResolver = resourcePatternResolver;
	}
	
	public Resource[] getAllFiles(String path) throws IOException{
		return resourcePatternResolver.getResources(path);
	}
}
