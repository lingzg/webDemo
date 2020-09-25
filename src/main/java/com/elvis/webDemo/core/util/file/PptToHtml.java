package com.elvis.webDemo.core.util.file;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hslf.usermodel.HSLFShape;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.hslf.usermodel.HSLFTextRun;
import org.apache.poi.hslf.usermodel.HSLFTextShape;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

public class PptToHtml {

	private final static Logger log = Logger.getLogger(PptToHtml.class);

	/**
	 * ppt03转html 
	 * fileName: ppt文件全路径名称
	 * output: html存放路径
	 */
	public static void ppt03ToHtml(String fileName, String output) throws TransformerException {
		try {
			FileUtil.fileExists(output);
			FileUtil.fileExists(output + "/images/ppt");
			String name = FileUtil.getFileName(fileName);
			String htmlname = name + ".html";
			FileInputStream is = new FileInputStream(fileName);
			HSLFSlideShow ppt = new HSLFSlideShow(is);
			is.close();
			Dimension pgsize = ppt.getPageSize();
			log.info(pgsize.width + "--" + pgsize.height);
			List<HSLFSlide> slides = ppt.getSlides();
			String imghtml = "";
			int i = 0;
			for (HSLFSlide slide : slides) {
				for (HSLFShape shape : slide.getShapes()) {
					if (shape instanceof HSLFTextShape) {
						HSLFTextShape tsh = (HSLFTextShape) shape;
						for (HSLFTextParagraph p : tsh) {
							for (HSLFTextRun r : p) {
								r.setFontFamily("宋体");
							}
						}
					}
				}
				BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = img.createGraphics();
				graphics.setPaint(Color.BLUE);
				graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
				slide.draw(graphics);

				// 这里设置图片的存放路径和图片的格式(jpeg,png,bmp等等),注意生成文件路径
				String imgs = "images/ppt/" + name + "_" + (i + 1) + ".jpg";
				FileOutputStream out = new FileOutputStream(output + "/" + imgs);
				javax.imageio.ImageIO.write(img, "jpeg", out);
				out.close();
				// 图片在html加载路径
				imghtml += "<img src=\'" + imgs
						+ "\' style=\'width:1200px;height:830px;vertical-align:text-bottom;\'><br><br><br><br>";
				i++;
			}
			String ppthtml = "<html><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body>"
					+ imghtml + "</body></html>";
			FileUtils.writeStringToFile(new File(output+"/"+ htmlname), ppthtml, "utf-8");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * ppt07转html 
	 * fileName: ppt文件全路径名称 
	 * output: html存放路径
	 * 
	 */
	public static void ppt07ToHtml(String fileName, String output) throws TransformerException {
		try {
			FileUtil.fileExists(output);
			FileUtil.fileExists(output + "/images/pptx");
			String name = FileUtil.getFileName(fileName);
			String htmlname = name + ".html";
			FileInputStream is = new FileInputStream(fileName);
			XMLSlideShow ppt = new XMLSlideShow(is);
			is.close();
			Dimension pgsize = ppt.getPageSize();
			log.info(pgsize.width + "--" + pgsize.height);
			List<XSLFSlide> slides = ppt.getSlides();
			String imghtml = "";
			int i = 0;
			for (XSLFSlide slide : slides) {
				for (XSLFShape shape : slide.getShapes()) {
					if (shape instanceof XSLFTextShape) {
						XSLFTextShape tsh = (XSLFTextShape) shape;
						for (XSLFTextParagraph p : tsh) {
							for (XSLFTextRun r : p) {
								r.setFontFamily("宋体");
							}
						}
					}
				}
				BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = img.createGraphics();
				// clear the drawing area
				graphics.setPaint(Color.white);
				graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
				// render
				slide.draw(graphics);
				
				String imgs = "images/pptx/" + name + "_" + (i + 1) + ".jpg";
				FileOutputStream out = new FileOutputStream(output + "/" + imgs);
				javax.imageio.ImageIO.write(img, "jpeg", out);
				out.close();
				// 图片在html加载路径
				imghtml += "<img src=\'" + imgs
						+ "\' style=\'width:1200px;height:830px;vertical-align:text-bottom;\'><br><br><br><br>";
				i++;
			}
			
			ppt.close();
			String ppthtml = "<html><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body>"
					+ imghtml + "</body></html>";
			FileUtils.writeStringToFile(new File(output, htmlname), ppthtml, "utf-8");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
