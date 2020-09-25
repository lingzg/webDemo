package com.elvis.webDemo.core.util.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PdfToHtml {

	private final static Logger log = Logger.getLogger(PdfToHtml.class);
	
    public static void toHtml(String pdfPath, String htmlPath){
        try{
        	//PDF转换成HTML保存的文件夹
        	FileUtil.fileExists(htmlPath);
        	FileUtil.fileExists(htmlPath+"/images/pdf");
        	
        	StringBuffer buffer = new StringBuffer();
            buffer.append("<!doctype html>\r\n");
            buffer.append("<head>\r\n");
            buffer.append("<meta charset=\"UTF-8\">\r\n");
            buffer.append("</head>\r\n");
            buffer.append("<body style=\"background-color:gray;\">\r\n");
            buffer.append("<style>\r\n");
            buffer.append("img {background-color:#fff; text-align:center; width:100%; max-width:100%;margin-top:6px;}\r\n");
            buffer.append("</style>\r\n");
            
            //pdf文件
            File pdfFile = new File(pdfPath);
            long start = System.currentTimeMillis();
            PDDocument document = PDDocument.load(pdfFile, (String) null);
            int size = document.getNumberOfPages();
            log.info("===>pdf : " + pdfFile.getName() +" , size : " + size);
            String name = FileUtil.getFileName(pdfFile);
            PDFRenderer reader = new PDFRenderer(document);
            for(int i=0 ; i < size; i++){
                //image = new PDFRenderer(document).renderImageWithDPI(i,130,ImageType.RGB);
            	BufferedImage image = reader.renderImage(i, 1.5f);
                //生成图片,保存位置
            	FileOutputStream out = new FileOutputStream(htmlPath + "/"+ "images/pdf/" + name+"_" + i + ".jpg");
                ImageIO.write(image, "png", out); //使用png的清晰度
                //将图片路径追加到网页文件里
                buffer.append("<img src=\"images/pdf/" + name+"_" + i + ".jpg\"/>\r\n");
                image = null; 
                out.flush(); 
                out.close(); 
            }
            reader = null;
            document.close();
            buffer.append("</body>\r\n");
            buffer.append("</html>");
            long end = System.currentTimeMillis() - start;
            log.info("===> Reading pdf times: " + (end/1000));
            //生成网页文件
            String htmlName = htmlPath+"/"+name+".html";
            log.info(htmlName);
            FileUtils.writeStringToFile(new File(htmlName), buffer.toString(), "utf-8"); 
        }catch(Exception e){
            log.error("===>Reader parse pdf to jpg error : " + e.getMessage(), e);
        }
    }

}
