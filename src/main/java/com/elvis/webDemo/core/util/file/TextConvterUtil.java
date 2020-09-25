package com.elvis.webDemo.core.util.file;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class TextConvterUtil {

	private final static Logger log = Logger.getLogger(TextConvterUtil.class);
	
    /**
     * 将文本文件里文字，写入到图片中保存
     * @return boolean  true，写入成功；false，写入失败
     * @throws FileNotFoundException 
     */
    public static boolean toImage(String textPath,String output) {
        //图片中文本行高
        final int Y_LINE_HEIGHT = 15;
        final int X_WIDTH_ZN = 15; //中文字符宽度
        final int X_WIDTH_EN = 9; //英文字符宽度
        FileUtil.fileExists(output);
        String name = FileUtil.getFileName(textPath);
        try (
        		//BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(textPath),fileEncode));
        		//BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlPosition), "UTF-8"));
        		FileOutputStream fos = new FileOutputStream(output+"/"+name+".jpg");
        	){
        	String fileEncode = EncodingDetect.getCharset(textPath);
        	List<String> lines = FileUtils.readLines(new File(textPath), fileEncode);
        	int width = 0;
        	int height = (lines.size()+1)*Y_LINE_HEIGHT;
        	for(String line: lines){
        		int enNum = line.replaceAll("[\u4e00-\u9fa5]", "").length(); //英文字符个数
        		int znNum = line.replaceAll("[^\u4e00-\u9fa5]", "").length(); //中文字符个数
        		int w = (enNum+1)*X_WIDTH_EN+(znNum+1)*X_WIDTH_ZN;
        		width = Math.max(width, w);
            }
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            //获取图像上下文
            Graphics g = image.createGraphics();
            g.setColor(null); //设置背景色
            g.fillRect(0, 0, width, height);//绘制背景
            g.setColor(Color.BLACK); //设置前景色
            g.setFont(new Font("微软雅黑", Font.PLAIN, 15)); //设置字体
            int lineNum = 1;
            for(String str: lines){
            	g.drawString(str, 0, lineNum * Y_LINE_HEIGHT);
            	lineNum++;
            }
            g.dispose();

            //保存为jpg图片
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
            encoder.encode(image);
        } catch (IOException e) {
        	log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }
    
    /*
     * txt文档转html
       filePath:txt原文件路径
       htmlPosition:转化后生成的html路径
     
    */
    public static void toHtml(String filePath, String output) {
        try {
        	FileUtil.fileExists(output);
        	String name = FileUtil.getFileName(filePath);
        	String encoding = EncodingDetect.getCharset(filePath);
        	List<String> lines = FileUtils.readLines(new File(filePath), encoding);
        	String content = StringUtils.join(lines, "</br>");
        	FileUtil.writeHtmlFile(content, output+"/"+name+".html");
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
    }
}
