package com.kohonen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.test.context.junit4.SpringRunner;

import com.kohonen.model.Document;
import com.kohonen.model.EdgePoint;
import com.kohonen.model.FeatureSpace;
import com.kohonen.model.Network;
import com.kohonen.model.NetworkSpecification;
import com.kohonen.service.DocumentService;
import com.kohonen.service.FeatureExtractor;
import com.kohonen.service.KohonenEngine;
import com.kohonen.service.distance.DistanceMeasure;
import com.kohonen.service.edge.ContourExtractor;
import com.kohonen.service.edge.EdgePointExtractor;
import com.kohonen.service.edge.ShellExtractor;
import com.kohonen.service.edge.ShellReinforcer;
import com.kohonen.service.util.ImageUtil;
import com.kohonen.service.util.MatrixCropUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KohonenWebappApplicationTests {

	@Autowired
	DocumentService documentService;
	
	@Autowired
	ResourcePatternResolver resourcePatternResolver;

	@Autowired
	ImageUtil ImageUtil;

	@Autowired
	EdgePointExtractor sobelPointExtractor;
	
	@Autowired
	MatrixCropUtil matrixCropUtil;

	@Autowired
	ShellExtractor sobelShellExtractor;
	
	@Autowired
	ShellReinforcer sobelShellReinforcer;
	
	@Autowired
	ContourExtractor sobelContourExtractor;
	
	@Autowired
	FeatureExtractor featureExtractor;
	
	@Autowired
	KohonenEngine kohonenEngine;
	
	@Autowired
	DistanceMeasure distanceMeasure;
	
	@Before
	public void createsDirs() throws IOException {
		createDirs("test", "copies");
		createDirs("test", "contours");

	}

	@Test
	public void contextLoads() {

	}

	@Test
	public void checkFileNames() throws IOException, FileNotFoundException {
		Resource[] resources = resourcePatternResolver.getResources("classpath:static/images/*.jpg");
		for (Resource res : resources) {
			System.out.println(res.getFilename());
		}
	}

	private final String TARGET_DIR = "C:\\Users\\SRVR1\\Documents\\mqu\\";

	private String evalPath(String... pathNodes) {
		String res = TARGET_DIR;
		for (String node : pathNodes) {
			res += "//" + node;
		}
		return res;
	}

	private void createDirs(String... pathNodes) throws IOException {
		String target = evalPath(pathNodes);
		Path path = Paths.get(target);
		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}
	}
	
	@Test
	public void testWeights() throws Exception {
		
		List<Document> documents = documentService.getTrainingData();
		Collections.shuffle(documents);
		
		for(int i = 0; i < documents.size(); i++){
			Document d1 = documents.get(i);
			Document d2 = documents.get((i+1)%documents.size());
			float[] f1 = d1.getFeatures();
			float[] f2 = d2.getFeatures();
			float fullDist = distanceMeasure.dist(new FeatureSpace(f1), new FeatureSpace(f2));
			int from = 0;
			int to = from + FeatureSpace.HSB_BINS;
			float hDist = getDist(from, to, f1, f2);
			from = to;
			to = from + FeatureSpace.HSB_BINS;
			float sDist = getDist(from, to, f1, f2);
			from = to;
			to = from + FeatureSpace.HSB_BINS;
			float bDist = getDist(from, to, f1, f2);
			from = to;
			to = from + FeatureSpace.GRADIENT_BINS;
			float gradDist = getDist(from, to, f1, f2);
			float ratioDist = f1[FeatureSpace.FEATURES_COUNT - 1] - f2[FeatureSpace.FEATURES_COUNT - 1];
			ratioDist = Math.abs(ratioDist);
			System.out.println(d1.getPath());
			System.out.println(d2.getPath());
			System.out.println(fullDist + " : " 
					+ hDist + "|"  + sDist + "|"   + bDist + "|"  + gradDist + "|"   + ratioDist 
					+ " = " + (hDist/fullDist) + "|"  + (sDist/fullDist) + "|"  + (bDist/fullDist) + "|"  + (gradDist/fullDist) + "|" + (ratioDist/fullDist));
		}
	}
	
	private float getDist(int from, int to, float[] f1, float[] f2) {
		float[] cp1 = new float[to - from];
		float[] cp2 = new float[to - from];
		for(int i = from; i < to; i++) {
			cp1[i - from] = f1[i];
			cp2[i - from] = f2[i];
		}
		return distanceMeasure.dist(new FeatureSpace(cp1), new FeatureSpace(cp2));
	}
	
	@Test
	public void testImages() throws Exception {
		Resource[] resources = resourcePatternResolver.getResources("classpath:static/images/*.jpg");
		//processImage(resources[0]);
		long begin = System.currentTimeMillis();
		for(Resource res : resources) {
			System.out.println(res.getFilename()); 
			processImage(res);
		}
		long total = System.currentTimeMillis() - begin;
		System.out.println("=== " + total);
	}
	
	private void processImage(Resource original) throws Exception {
		
		BufferedImage image = ImageUtil.readImage(original.getFile().toPath());

		
		float[] values = featureExtractor.extractValues(image).getFeatures();

		StringBuilder sb = new StringBuilder(FeatureSpace.FEATURES_COUNT * 16);
		
		int idx = 0;
		for (int i = 0; i < FeatureSpace.HSB_BINS; i++) {
			sb.append(String.format("%.2f", values[i + idx]));
			sb.append(" ");
		}
		sb.append("    ");
		
		idx += FeatureSpace.HSB_BINS;
		for (int i = 0; i < FeatureSpace.HSB_BINS; i++) {
			sb.append(String.format("%.2f", values[i + idx]));
			sb.append(" ");
		}
		sb.append("    ");
		
		idx += FeatureSpace.HSB_BINS;
		for (int i = 0; i < FeatureSpace.HSB_BINS; i++) {
			sb.append(String.format("%.2f", values[i + idx]));
			sb.append(" ");
		}
		sb.append("    ");
		
		idx += FeatureSpace.HSB_BINS;
		for (int i = 0; i < FeatureSpace.GRADIENT_BINS; i++) {
			sb.append(String.format("%.2f", values[i + idx]));
			sb.append(" ");
		}
		sb.append("    ");
		
		idx += FeatureSpace.GRADIENT_BINS;
		sb.append(String.format("%.2f", values[idx]));
		sb.append("\n");
		
		System.out.println(sb);
		
		
		
		/*final Raster raster = image.getRaster();
		final int width = raster.getWidth();
		final int height = raster.getHeight();

		final int[] rgba = new int[4];

		final int[][] grayscale = new int[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				raster.getPixel(x, y, rgba);
				grayscale[x][y] = (int) (rgba[0] * 0.299 + rgba[1] * 0.587 + rgba[2] * 0.114);
			}
		}
		
		int imgType = image.getType();
		
		String fileName = original.getFilename();
		EdgePoint[][] sobelPoints = sobelPointExtractor.extractSobelPoints(grayscale);
		EdgePoint[][] croppedSobelPoints = (EdgePoint[][]) matrixCropUtil.cropMatrix(sobelPoints);
		EdgePoint[][] shell = sobelShellExtractor.findShell(croppedSobelPoints);
		EdgePoint[][] reinforcedShell = sobelShellReinforcer.reinforce(shell);
		
		
		
		makeImageContour(imgType, sobelPoints, fileName, "-base");
		makeImageContour(imgType, croppedSobelPoints, fileName, "-cropped");
		makeImageContour(imgType, shell, fileName, "-shell");
		makeImageContour(imgType, reinforcedShell, fileName, "-reinforced");
		
		Contour contour = sobelContourExtractor.extractSobelContour(grayscale);*/
	}

	private void makeImageContour(int imgType, EdgePoint[][] sobelPoints, String fileName, String marker) throws Exception {
		
		int width = sobelPoints.length;
		int height = sobelPoints[0].length;
		
		BufferedImage res = new BufferedImage(width, height, imgType);
		Graphics2D g = res.createGraphics();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		
		g.setColor(Color.BLACK);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (sobelPoints[x][y] != null) {
					g.drawLine(x, y, x, y);
				}	
			}
		}
		g.dispose();
		
		int idx = fileName.lastIndexOf('.');	
		String targetFileName = fileName.substring(0, idx) + marker + fileName.substring(idx);
		File outputfile = new File(evalPath("test", "contours", targetFileName));
	    ImageIO.write(res, "jpg", outputfile);
	}
}
