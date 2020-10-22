package com.elvis.webDemo.core.util.pdf;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
/** @author 宋祥  @date 2014年2月25日 上午10:31:36
 * @function:pdf 帮助类
 */
public class PdfHelp {
	
	
	public static final int tableEmptyRows=1;
	public static final int lineHeight=18;
	
	public static BaseFont bfChinese;
	static{
		try {
			bfChinese=BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED,false);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Font getFont(float size, int style, BaseColor color){
		float size1=10;
		int style1=Font.NORMAL;
		BaseColor color1=BaseColor.BLACK;
		if(size>0)size1=size;
		if(style>0)style1=style;
		if(color!=null)color1=color;
		Font f=new Font(PdfHelp.bfChinese, size1,style1,color1);
		return f;
	}
	public Font getNormalFont(){
	  return this.getFont(0, 0, null);
	}
	
	public Font getOneLevelFont(){
		return this.getFont(14,Font.BOLD, BaseColor.BLACK);
	}
	
	public Font getTwoLeveFont(){
		return this.getFont(13,Font.BOLD,  BaseColor.BLACK);
	}
	
	public Font getThreeLeveFont(){
		return this.getFont(11,Font.BOLD,  BaseColor.BLACK);
	}
	
   public PdfPTable createTable(float[] relativeWidths) throws Exception{
	   PdfPTable table=new PdfPTable(relativeWidths);
	   table.setSpacingBefore(8f);
	   table.setLockedWidth(true);
	   table.setTotalWidth(500);
	   table.getDefaultCell().setFixedHeight(20);
	   table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
	   table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中
	   return table;
   }
   
   public PdfPCell createCell(Object title) throws Exception{
	   if(title==null){title="";}
	   PdfPCell cell=new PdfPCell(new Phrase(title.toString(),this.getNormalFont()));
	   cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	   cell.setMinimumHeight(20f);
	   cell.setPaddingLeft(2f);
	   cell.setPaddingRight(2f);
	   return cell;
   }
   
   public PdfPCell createTextArea(Object title) throws Exception{
	   if(title==null){title="";}
	   PdfPCell cell=new PdfPCell(new Phrase(title.toString(),this.getNormalFont()));
	   cell.setVerticalAlignment(Element.ALIGN_TOP);
	   cell.setMinimumHeight(100f);
	   cell.setPaddingLeft(2f);
	   cell.setPaddingRight(2f);
	   return cell;
   }
   
   public  Paragraph createParagraph(Object lable,Font font) throws Exception{
	   if(lable==null){ lable="";}
	   Paragraph p=new Paragraph(lable.toString(),font);
	   p.setAlignment(Element.ALIGN_CENTER);
	   return p;
   }
   
   public PdfPCell createLable(Object lable) throws Exception{
	   if(lable==null){lable="";}
	   PdfPCell cell=new PdfPCell(new Phrase(lable.toString(),this.getThreeLeveFont()));
	   cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	   cell.setMinimumHeight(20f);
	   cell.setPaddingLeft(2f);
	   cell.setPaddingRight(2f);
	   return cell;
   }
   /***
    * @autor 宋祥  @date 2014年3月5日 下午4:57:54
    * @function :将table 转化为标准格式
    * @param ot
    * @return
    * @throws Exception
    */
   public PdfPTable convertNormTable(PdfPTable ot) throws Exception{
	   PdfPTable nt=this.createTable(ot.getAbsoluteWidths());
	   nt.setSpacingBefore(2f);
	   ArrayList<PdfPRow> rows=ot.getRows();
	   if(rows!=null){
		   for(PdfPRow row:rows){
			 PdfPCell[] cells=row.getCells();
			 for(PdfPCell cell:cells){
				 if(cell!=null){
					 PdfPCell ncell=new PdfPCell();
					 ncell.setRowspan(cell.getRowspan());
					 ncell.setColspan(cell.getColspan());
					 ncell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					 ncell.setMinimumHeight(20f);
					 List<Element> els=cell.getCompositeElements();
					 if(els!=null){
						 for(Element el:els){
							 List<Chunk> cks=el.getChunks();
							 if(cks!=null){
								 for(Chunk ck:cks){
									 if(ck!=null){
										 if(ck.getFont().getSize()<0){
											 ck.getFont().setSize(this.getNormalFont().getSize());
										 }
									 }
								 }
							 }
							 ncell.addElement(el);
						 }
					 }
					 nt.addCell(ncell);
				 }
			 }
		   }
	   }
	   return nt;
   }
   
}
