package com.elvis.webDemo.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;

import com.elvis.webDemo.core.util.pdf.PdfHelp;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFTest {

	public static void main(String[] args) {
		try {
			Document document = new Document(PageSize.A4, 0, 0, 0, 0);
			File pdfFile=new File("d:/doc/pdf/限停减排方案执行分析报告.pdf");
			File dir = pdfFile.getParentFile();
			if(!dir.exists()){
				dir.mkdirs();
			}
			//创建PDF
		    PdfWriter writer =  PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
	        // 设置页面布局
	        writer.setViewerPreferences(PdfWriter.PageLayoutOneColumn);
	        // 页码
	        //writer.setPageEvent(new PageXofYTest());
			document.open();
			PdfHelp help = new PdfHelp();
			
			Paragraph paragraph = new Paragraph(220);//段落的间距
			//1 2 3  中右左
			paragraph.setAlignment(1);  //对齐方式
			paragraph.setFont(help.getFont(24f, 0, null));//设置段落字体
			paragraph.add(new Chunk("限停减排方案执行分析报告"));  
			document.add(paragraph);
			
			paragraph = new Paragraph(280);//段落的间距
			paragraph.setAlignment(1);  //对齐方式
			paragraph.setFont(help.getFont(12f, 0, null));//设置段落字体
			paragraph.add(new Chunk(LocalDate.now().toString()));  
			document.add(paragraph);
			
			paragraph = new Paragraph(50);//段落的间距
			paragraph.setAlignment(1);  //对齐方式
			paragraph.setFont(help.getFont(15f, 0, null));//设置段落字体
			paragraph.add(new Chunk("中电科安"));  
			document.add(paragraph);
			
			document.newPage(); //换页
			
			paragraph = new Paragraph(50);//段落的间距
			paragraph.setAlignment(3);  //对齐方式
			paragraph.setFont(help.getFont(20f, 0, null));//设置段落字体
			paragraph.add(new Chunk("1.基本信息"));  
			document.add(paragraph);
			
			document.close();
			writer.close();
		} catch (DocumentException | FileNotFoundException e) {
			e.printStackTrace();
		}
        
	}
}
