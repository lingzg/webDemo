package com.elvis.webDemo.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.pdf.BaseFont;
import com.lowagie.text.DocumentException;

public class HtmlToPdf {

	public static void main(String[] args) {
		String str = htmlFileToString("d:/doc/html/analysisReportProduction_report_1.html");
		htmlStringToPdf(str, "d:/doc/pdf/analysisReportProduction_report_1.pdf");
	}
	
	public static String htmlFileToString(String filePath){
		StringBuilder strline = new StringBuilder("");
    	File fin = new File(filePath);
		try (RandomAccessFile accessFile = new RandomAccessFile(fin, "r");
			FileChannel fcin = accessFile.getChannel();
		){
			Charset charset = Charset.forName("UTF-8");
			int bufSize = 100000; 
		    ByteBuffer rBuffer = ByteBuffer.allocate(bufSize); 
			String enterStr = "\n";
			byte[] bs = new byte[bufSize];
			
			StringBuilder strBuf = new StringBuilder("");
			while (fcin.read(rBuffer) != -1) {
				int rSize = rBuffer.position();
				rBuffer.rewind();
				rBuffer.get(bs);
				rBuffer.clear();
				String tempString = new String(bs, 0, rSize,charset);
				tempString = tempString.replaceAll("\r", "");
 
				int fromIndex = 0;
				int endIndex = 0;
				while ((endIndex = tempString.indexOf(enterStr, fromIndex)) != -1) {
					String line = tempString.substring(fromIndex, endIndex);
					line = strBuf.toString() + line;
					strline.append(line.trim());
					
					strBuf.delete(0, strBuf.length());
					fromIndex = endIndex + 1;
				}
				if (rSize > tempString.length()) {
					strline.append(tempString.substring(fromIndex, tempString.length()));
					strBuf.append(tempString.substring(fromIndex, tempString.length()));
				} else {
					strline.append(tempString.substring(fromIndex, rSize));
					strBuf.append(tempString.substring(fromIndex, rSize));
				}
			}
//			System.out.println(strline.toString().replaceAll("\"", "'"));
		} catch (Exception e) {
			
		}
		return strline.toString();
	}
	
	public static void htmlStringToPdf(String strline,String output){
//		String htmlString = strline.toString().replaceAll("\"", "'").replaceAll("<style>", "<style>body{font-family:SimSun;font-size:14px;}");    //注意这里为啥要写这个，主要是替换成这样的字体，如果不设置中文有可能显示不出来。
		String htmlString = strline.toString().replaceAll("\"", "'");
		try {
			OutputStream os = new FileOutputStream(output);
			ITextRenderer renderer = new ITextRenderer();
			ITextFontResolver font = renderer.getFontResolver();
			font.addFont("C:/WINDOWS/Fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);//添加中文识别，这里是设置的宋体，Linux下要换成对应的字体
			renderer.setDocumentFromString(htmlString.toString());
			renderer.layout();
			renderer.createPDF(os);
			renderer.finishPDF();
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
	}
}
