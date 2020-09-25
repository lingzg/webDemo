package com.elvis.webDemo.core.util.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class FileUtil {

	public static void fileExists(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
	
	//判断TXT编码格式方法
	public static  String getTxtCharset(String sourceFile) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try (
        		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
        	){
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                return charset; //文件编码为 ANSI
            } else if (first3Bytes[0] == (byte) 0xFF
                    && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE"; //文件编码为 Unicode
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE"; //文件编码为 Unicode big endian
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; //文件编码为 UTF-8
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
	}
	
	/**
	 * @param content 生成的html内容
	 * @param htmlPath 生成的html文件地址
	 */
	public static void writeHtmlFile(String content, String htmlPath) {
		File file = new File(htmlPath);
		StringBuilder sb = new StringBuilder();
		try (
				PrintStream printStream = new PrintStream(new FileOutputStream(file),false, "utf-8");
			){
			file.createNewFile();// 创建文件
			sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UFT-8\"><title>Html Test</title></head><body>");
			sb.append("<div>");
			sb.append(content);
			sb.append("</div>");
			sb.append("</body></html>");
			printStream.println(sb.toString());// 将字符串写入文件
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static String getFileName(String filePath){
		File file = new File(filePath);
		return getFileName(file);
	}
	
	public static String getFileName(File file){
		String name = file.getName();
		name = name.substring(0, name.lastIndexOf("."));
		return name;
	}
}
