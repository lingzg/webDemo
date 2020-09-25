package com.elvis.webDemo.core.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel2HtmlUtil {

	private final static Logger log = Logger.getLogger(Excel2HtmlUtil.class);
	
	/**
	 * @param filePath
	 *            excel源文件文件的路径
	 * @param htmlPositon
	 *            生成的html文件的路径
	 * @param isWithStyle
	 *            是否需要表格样式 包含 字体 颜色 边框 对齐方式
	 * 
	 */
	public static void readExcelToHtml(String filePath, String output) {
		try (
				InputStream is = new FileInputStream(filePath);
			){
			Workbook wb = WorkbookFactory.create(is);
			String htmlExcel = "";
			if (wb instanceof XSSFWorkbook) { // 版excel处理方法
				XSSFWorkbook xWb = (XSSFWorkbook) wb;
				htmlExcel = getExcelInfo(xWb, true);
			} else if (wb instanceof HSSFWorkbook) { // 07及10版以后的excel处理方法
				HSSFWorkbook hWb = (HSSFWorkbook) wb;
				htmlExcel = getExcelInfo(hWb, true);
			}
			String htmlPositon = output +"/" + FileUtil.getFileName(filePath)+".html";
			FileUtils.writeStringToFile(new File(htmlPositon), htmlExcel, "utf-8");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private static String getExcelInfo(Workbook wb, boolean isWithStyle) {
		StringBuffer sb = new StringBuffer();
		sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UFT-8\"><title>Html Test</title></head>\r\n<style>");
		sb.append(".tab {margin: 10px 0;text-align: left!important;}");
		sb.append(".tab-title {position: relative;left: 0;height: 40px;white-space: nowrap;font-size: 0;border-bottom-width: 1px;border-bottom-style: solid;transition: all .2s;-webkit-transition: all .2s;}");
		sb.append(".tab-title li {border-radius: 30px;border: 1px solid #1c76ff;height: 20px;line-height: 20px;padding: 0 20px;font-size: 12px;margin-right: 15px;display: inline-block;}");
		sb.append(".tab-title li.tab-this {background-color: #1c76ff;color: #fff;}.tab-title li:hover{cursor:pointer}.hide{display:none;}");
		sb.append("</style>\r\n<body>");
		sb.append("<div class='tab'><ul class='tab-title'>");
		int size = wb.getNumberOfSheets();
		for(int i=0;i<size;i++){
			String name = wb.getSheetName(i);
			sb.append("<li id='li_").append(i).append("'").append(i==0?" class='tab-this'>":">").append(name).append("</li>");
		}
		sb.append("</ul></div>\r\n<div>");
		for(int i=0;i<size;i++){
			Sheet sheet = wb.getSheetAt(i);
			int lastRowNum = sheet.getLastRowNum();
			Map<String, String> map[] = getRowSpanColSpanMap(sheet);
			sb.append("<table id='table_").append(i).append("'").append(i>0?" class='hide'":"").append(" style='border-collapse:collapse;' width='100%'>");
			Row row = null; // 兼容
			Cell cell = null; // 兼容
			
			for (int rowNum = sheet.getFirstRowNum(); rowNum <= lastRowNum; rowNum++) {
				row = sheet.getRow(rowNum);
				if (row == null) {
					sb.append("<tr><td ><nobr> </nobr></td></tr>");
					continue;
				}
				sb.append("<tr>");
				int lastColNum = row.getLastCellNum();
				for (int colNum = 0; colNum < lastColNum; colNum++) {
					cell = row.getCell(colNum);
					if (cell == null) { // 特殊情况 空白的单元格会返回null
						sb.append("<td> </td>");
						continue;
					}
					
					String stringValue = getCellValue(cell);
					if (map[0].containsKey(rowNum + "," + colNum)) {
						String pointString = map[0].get(rowNum + "," + colNum);
						map[0].remove(rowNum + "," + colNum);
						int bottomeRow = Integer.valueOf(pointString.split(",")[0]);
						int bottomeCol = Integer.valueOf(pointString.split(",")[1]);
						int rowSpan = bottomeRow - rowNum + 1;
						int colSpan = bottomeCol - colNum + 1;
						sb.append("<td rowspan= '" + rowSpan + "' colspan= '" + colSpan + "' ");
					} else if (map[1].containsKey(rowNum + "," + colNum)) {
						map[1].remove(rowNum + "," + colNum);
						continue;
					} else {
						sb.append("<td ");
					}
					
					// 判断是否需要样式
					if (isWithStyle) {
						dealExcelStyle(wb, sheet, cell, sb);// 处理单元格样式
					}
					
					sb.append("><nobr>");
					if (stringValue == null || "".equals(stringValue.trim())) {
						sb.append("   ");
					} else {
						// 将ascii码为0的空格转换为html下的空格（ ）
						sb.append(stringValue.replace(String.valueOf((char) 0), " "));
					}
					sb.append("</nobr></td>");
				}
				sb.append("</tr>");
			}
			
			sb.append("</table>\r\n");
		}
		sb.append("</div></body>\r\n<script>");
		sb.append("var ul = document.querySelector('ul');");
		sb.append("ul.addEventListener('click',function(event){");
		sb.append("if(event.target.nodeName == 'LI'){");
		sb.append("document.querySelector('.tab-this').className='';");
		sb.append("event.target.className='tab-this';");
		sb.append("var id = event.target.id.replace('li','table');");
		sb.append("document.querySelector('table:not(.hide)').className='hide';");
		sb.append("document.getElementById(id).className='';");
		sb.append("} });</script>\r\n</html>");
		return sb.toString();
	}

	private static Map<String, String>[] getRowSpanColSpanMap(Sheet sheet) {

		Map<String, String> map0 = new HashMap<String, String>();
		Map<String, String> map1 = new HashMap<String, String>();
		int mergedNum = sheet.getNumMergedRegions();
		CellRangeAddress range = null;
		for (int i = 0; i < mergedNum; i++) {
			range = sheet.getMergedRegion(i);
			int topRow = range.getFirstRow();
			int topCol = range.getFirstColumn();
			int bottomRow = range.getLastRow();
			int bottomCol = range.getLastColumn();
			map0.put(topRow + "," + topCol, bottomRow + "," + bottomCol);
			// System.out.println(topRow + "," + topCol + "," + bottomRow + ","
			// + bottomCol);
			int tempRow = topRow;
			while (tempRow <= bottomRow) {
				int tempCol = topCol;
				while (tempCol <= bottomCol) {
					map1.put(tempRow + "," + tempCol, "");
					tempCol++;
				}
				tempRow++;
			}
			map1.remove(topRow + "," + topCol);
		}
		Map[] map = { map0, map1 };
		return map;
	}

	/**
	 * 获取表格单元格Cell内容
	 * 
	 * @param cell
	 * @return
	 */
	private static String getCellValue(Cell cell) {

		String result = new String();
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:// 数字类型
			if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
				SimpleDateFormat sdf = null;
				if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
					sdf = new SimpleDateFormat("HH:mm");
				} else {// 日期
					sdf = new SimpleDateFormat("yyyy-MM-dd");
				}
				Date date = cell.getDateCellValue();
				result = sdf.format(date);
			} else if (cell.getCellStyle().getDataFormat() == 58) {
				// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				double value = cell.getNumericCellValue();
				Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
				result = sdf.format(date);
			} else {
				double value = cell.getNumericCellValue();
				CellStyle style = cell.getCellStyle();
				DecimalFormat format = new DecimalFormat();
				String temp = style.getDataFormatString();
				// 单元格设置成常规
				if (temp.equals("General")) {
					format.applyPattern("#");
				}
				result = format.format(value);
			}
			break;
		case Cell.CELL_TYPE_STRING:// String类型
			result = cell.getRichStringCellValue().toString();
			break;
		case Cell.CELL_TYPE_BLANK:
			result = "";
			break;
		default:
			result = "";
			break;
		}
		return result;
	}

	/**
	 * 处理表格样式
	 * 
	 * @param wb
	 * @param sheet
	 * @param sb
	 */
	private static void dealExcelStyle(Workbook wb, Sheet sheet, Cell cell, StringBuffer sb) {

		CellStyle cellStyle = cell.getCellStyle();
		if (cellStyle != null) {
			short alignment = cellStyle.getAlignment();
			// sb.append("align='" + convertAlignToHtml(alignment) + "'
			// ");//单元格内容的水平对齐方式
			short verticalAlignment = cellStyle.getVerticalAlignment();
			sb.append("valign='" + convertVerticalAlignToHtml(verticalAlignment) + "' ");// 单元格中内容的垂直排列方式

			if (wb instanceof XSSFWorkbook) {

				XSSFFont xf = ((XSSFCellStyle) cellStyle).getFont();
				short boldWeight = xf.getBoldweight();
				String align = convertAlignToHtml(alignment);
				sb.append("style='");
				sb.append("font-weight:" + boldWeight + ";"); // 字体加粗
				sb.append("font-size: " + xf.getFontHeight() / 2 + "%;"); // 字体大小
				int columnWidth = sheet.getColumnWidth(cell.getColumnIndex());
				sb.append("width:" + columnWidth + "px;");
				sb.append("text-align:" + align + ";");// 表头排版样式
				XSSFColor xc = xf.getXSSFColor();
				if (xc != null && !"".equals(xc)) {
					sb.append("color:#" + xc.getARGBHex().substring(2) + ";"); // 字体颜色
				}

				XSSFColor bgColor = (XSSFColor) cellStyle.getFillForegroundColorColor();
				if (bgColor != null && !"".equals(bgColor)) {
					sb.append("background-color:#" + bgColor.getARGBHex().substring(2) + ";"); // 背景颜色
				}
				sb.append(getBorderStyle(0, cellStyle.getBorderTop(),
						((XSSFCellStyle) cellStyle).getTopBorderXSSFColor()));
				sb.append(getBorderStyle(1, cellStyle.getBorderRight(),
						((XSSFCellStyle) cellStyle).getRightBorderXSSFColor()));
				sb.append(getBorderStyle(2, cellStyle.getBorderBottom(),
						((XSSFCellStyle) cellStyle).getBottomBorderXSSFColor()));
				sb.append(getBorderStyle(3, cellStyle.getBorderLeft(),
						((XSSFCellStyle) cellStyle).getLeftBorderXSSFColor()));

			} else if (wb instanceof HSSFWorkbook) {

				HSSFFont hf = ((HSSFCellStyle) cellStyle).getFont(wb);
				short boldWeight = hf.getBoldweight();
				short fontColor = hf.getColor();
				sb.append("style='");
				HSSFPalette palette = ((HSSFWorkbook) wb).getCustomPalette(); // 类HSSFPalette用于求的颜色的国际标准形式
				HSSFColor hc = palette.getColor(fontColor);
				sb.append("font-weight:" + boldWeight + ";"); // 字体加粗
				sb.append("font-size: " + hf.getFontHeight() / 2 + "%;"); // 字体大小
				String align = convertAlignToHtml(alignment);
				sb.append("text-align:" + align + ";");// 表头排版样式
				String fontColorStr = convertToStardColor(hc);
				if (fontColorStr != null && !"".equals(fontColorStr.trim())) {
					sb.append("color:" + fontColorStr + ";"); // 字体颜色
				}
				int columnWidth = sheet.getColumnWidth(cell.getColumnIndex());
				sb.append("width:" + columnWidth + "px;");
				short bgColor = cellStyle.getFillForegroundColor();
				hc = palette.getColor(bgColor);
				String bgColorStr = convertToStardColor(hc);
				if (bgColorStr != null && !"".equals(bgColorStr.trim())) {
					sb.append("background-color:" + bgColorStr + ";"); // 背景颜色
				}
				sb.append(getBorderStyle(palette, 0, cellStyle.getBorderTop(), cellStyle.getTopBorderColor()));
				sb.append(getBorderStyle(palette, 1, cellStyle.getBorderRight(), cellStyle.getRightBorderColor()));
				sb.append(getBorderStyle(palette, 3, cellStyle.getBorderLeft(), cellStyle.getLeftBorderColor()));
				sb.append(getBorderStyle(palette, 2, cellStyle.getBorderBottom(), cellStyle.getBottomBorderColor()));
			}

			sb.append("' ");
		}
	}

	/**
	 * 单元格内容的水平对齐方式
	 * 
	 * @param alignment
	 * @return
	 */
	private static String convertAlignToHtml(short alignment) {

		String align = "center";
		switch (alignment) {
		case CellStyle.ALIGN_LEFT:
			align = "left";
			break;
		case CellStyle.ALIGN_CENTER:
			align = "center";
			break;
		case CellStyle.ALIGN_RIGHT:
			align = "right";
			break;
		default:
			break;
		}
		return align;
	}

	/**
	 * 单元格中内容的垂直排列方式
	 * 
	 * @param verticalAlignment
	 * @return
	 */
	private static String convertVerticalAlignToHtml(short verticalAlignment) {

		String valign = "middle";
		switch (verticalAlignment) {
		case CellStyle.VERTICAL_BOTTOM:
			valign = "bottom";
			break;
		case CellStyle.VERTICAL_CENTER:
			valign = "center";
			break;
		case CellStyle.VERTICAL_TOP:
			valign = "top";
			break;
		default:
			break;
		}
		return valign;
	}

	private static String convertToStardColor(HSSFColor hc) {

		StringBuffer sb = new StringBuffer("");
		if (hc != null) {
			if (HSSFColor.AUTOMATIC.index == hc.getIndex()) {
				return null;
			}
			sb.append("#");
			for (int i = 0; i < hc.getTriplet().length; i++) {
				sb.append(fillWithZero(Integer.toHexString(hc.getTriplet()[i])));
			}
		}

		return sb.toString();
	}

	private static String fillWithZero(String str) {
		if (str != null && str.length() < 2) {
			return "0" + str;
		}
		return str;
	}

	static String[] bordesr = { "border-top:", "border-right:", "border-bottom:", "border-left:" };
	static String[] borderStyles = { "solid ", "solid ", "solid ", "solid ", "solid ", "solid ", "solid ", "solid ",
			"solid ", "solid", "solid", "solid", "solid", "solid" };

	private static String getBorderStyle(HSSFPalette palette, int b, short s, short t) {

		if (s == 0)
			return bordesr[b] + borderStyles[s] + "#d0d7e5 1px;";
		;
		String borderColorStr = convertToStardColor(palette.getColor(t));
		borderColorStr = borderColorStr == null || borderColorStr.length() < 1 ? "#000000" : borderColorStr;
		return bordesr[b] + borderStyles[s] + borderColorStr + " 1px;";

	}

	private static String getBorderStyle(int b, short s, XSSFColor xc) {

		if (s == 0)
			return bordesr[b] + borderStyles[s] + "#d0d7e5 1px;";
		;
		if (xc != null && !"".equals(xc)) {
			String borderColorStr = xc.getARGBHex();// t.getARGBHex();
			borderColorStr = borderColorStr == null || borderColorStr.length() < 1 ? "#000000"
					: borderColorStr.substring(2);
			return bordesr[b] + borderStyles[s] + borderColorStr + " 1px;";
		}

		return "";
	}

}
