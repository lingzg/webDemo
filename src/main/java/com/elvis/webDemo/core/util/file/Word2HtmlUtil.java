package com.elvis.webDemo.core.util.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;

public class Word2HtmlUtil {
	/**
	 * 兼容doc和docx的word转html方法
	 * @param inFile 需要转换的word文件
	 * @param outPath 输出文件路径
	 * @param outName 输出文件名
	 */
	public static void word2Html(String inFile, String outPath) throws Exception{
		FileInputStream fis=new FileInputStream(inFile);
		String suffix=inFile.substring(inFile.lastIndexOf("."));
		String outName = FileUtil.getFileName(inFile);
		if (suffix.equalsIgnoreCase(".docx")) {
			docx2Html(fis, outPath, outName);
		} else {
			doc2Html(fis, outPath, outName);
		}
	}
	
	public static void docx2Html(InputStream fis, String outPath ,String outName) throws Exception{
		// 加载word文档生成 XWPFDocument对象
		XWPFDocument document = new XWPFDocument(fis);
		// 解析 XHTML配置
		String imageFolder = outPath + "/images/docx/"+outName+"/";//图片存放路径
		File imageFolderFile = new File(imageFolder);
		XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(imageFolderFile));
		options.setExtractor(new FileImageExtractor(imageFolderFile));
		options.setIgnoreStylesIfUnused(false);
		options.setFragment(true);
		options.URIResolver(new BasicURIResolver("images/docx/"+outName+"/"));//html中img的src前缀
		// 将 XWPFDocument转换成XHTML
		OutputStream out = new FileOutputStream(new File(outPath + "/"+outName+".html"));
		XHTMLConverter.getInstance().convert(document, out, options);
	}
	
	public static void doc2Html(InputStream fis, String outPath, String outName) throws Exception {
		HWPFDocument wordDocument = new HWPFDocument(fis);
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
		wordToHtmlConverter.setPicturesManager(new PicturesManager() {
			public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
				return "images/doc/"+outName+"/" + suggestedName;// html中img的src值
			}
		});
	    wordToHtmlConverter.processDocument(wordDocument);
	    //保存图片
	    List <Picture> pics = wordDocument.getPicturesTable().getAllPictures();
	    if (pics != null) {
	        for (int i = 0; i < pics.size(); i++) {
	            Picture pic = (Picture) pics.get(i);
	            try {
	            	String imageFolder = outPath + "/images/doc/"+outName+"/";
	            	File dir=new File(imageFolder);//图片保存路径
	        		if(!dir.exists()) {
	        			dir.mkdirs();
	        		}
	                pic.writeImageContent(new FileOutputStream(imageFolder + pic.suggestFullFileName()));
	            } catch (FileNotFoundException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    Document htmlDocument = wordToHtmlConverter.getDocument();
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    DOMSource domSource = new DOMSource(htmlDocument);
	    StreamResult streamResult = new StreamResult(out);
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer serializer = tf.newTransformer();
	    serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
	    serializer.setOutputProperty(OutputKeys.INDENT, "yes");
	    serializer.setOutputProperty(OutputKeys.METHOD, "html");
	    serializer.transform(domSource, streamResult);
	    out.close();
	    FileUtils.writeStringToFile(new File(outPath+"/"+ outName+".html"), new String(out.toByteArray()), "utf-8");
	}
	
}
