package com.kohonen.service.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

@Component
public class ImageUtil {

	public BufferedImage scaleImage(Path path, int maxH, int maxW) throws IOException {
		BufferedImage originalImage = readImage(path);
		return scaleImage(originalImage, maxH, maxW);
	}
	
	public BufferedImage scaleImage(BufferedImage originalImage, int maxH, int maxW) throws IOException {

		BufferedImage res = null;

		double imgH = originalImage.getHeight();
		double imgW = originalImage.getWidth();

		if (imgH > maxH || imgW > maxW) {
			double coeffH = maxH / imgH;
			double coeffW = maxW / imgW;
			double coeff = coeffH < coeffW ? coeffH : coeffW;
			res = new BufferedImage((int) (imgW * coeff), (int) (imgH * coeff), originalImage.getType());
			Graphics2D g = res.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			AffineTransform at = AffineTransform.getScaleInstance(coeff, coeff);
			g.drawRenderedImage(originalImage, at);
		} else {
			res = originalImage;
		}
		return res;
	}

	public BufferedImage readImage(Path path) throws IOException {
		return ImageIO.read(path.toFile());
	}
	
	public int[][] toGrayScale(BufferedImage image) {
		Raster raster = image.getRaster();
		int width = raster.getWidth();
		int height = raster.getHeight();
		int[][] grayScale = new int[width][height];
		
		int[] rgba = new int[4];
		for(int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				raster.getPixel(col, row, rgba);
				grayScale[col][row] = (int) (rgba[0]*0.299 + rgba[1]*0.587 + rgba[2]*0.114);
			}
		}
		return grayScale;
	}
	
}
