package com.hy.zookeeper.config.util;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExportExcelUtil {
	
	private ExportExcelUtil(){}
	
	private static Logger logger = LoggerFactory.getLogger(ExportExcelUtil.class);
	
	 /**
     * 导出excel
     * @param title  表格标题名
     * @param headers  表格列名数组
     * @param columnNames  填充的字段名
     * @param dataset  表格数据
     * @param out      与输出设备关联的流对象
     * @param pattern  设定日期输出格式。默认为"yyy-MM-dd"
     */
    public static void exportExcel(String title, String[] headers, String[] columnNames, List<Map<String,Object>> dataset, OutputStream out, String pattern) {
    	if(StringUtils.isEmpty(pattern)){
			pattern = "yyy-MM-dd";
		}
    	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    	try(HSSFWorkbook workbook = new HSSFWorkbook()){
    		// 声明一个工作薄
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet(title);
            // 设置表格默认列宽度为15个字节
            sheet.setDefaultColumnWidth((short) 25);
            // 生成一个样式
            HSSFCellStyle style = workbook.createCellStyle();
            // 设置这些样式
            style.setWrapText(true);  
            // 生成一个字体
            HSSFFont font = workbook.createFont();
            font.setFontHeightInPoints((short) 12);
            font.setFontName("宋体");
            // 把字体应用到当前的样式
            style.setFont(font);
            // 生成并设置另一个样式
            HSSFCellStyle style2 = workbook.createCellStyle();
            style2.setWrapText(true);  
            // 生成另一个字体
            HSSFFont font2 = workbook.createFont();
            font2.setColor(HSSFColor.BLACK.index);
            font2.setFontHeightInPoints((short) 12);
            font2.setFontName("宋体");
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            // 把字体应用到当前的样式
            style2.setFont(font2);
            //产生表格标题行
            HSSFRow row = sheet.createRow(0);
            for (short i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }
            int index = 0;
            HSSFCell cell;
            // 遍历集合数据，产生数据行
            for (int i=0;i<dataset.size();i++) {
            	index++;
            	row = sheet.createRow(index);
                Map<String,Object> map = dataset.get(i);
    			int cellIndex=0;
    			for(String columnName : columnNames) {
    				cell = row.createCell(cellIndex);
    	            cell.setCellStyle(style2);
    				Object value = map.get(columnName);
    	            String textValue = formatObject(value, sdf, sheet,
    		               index, cellIndex, patriarch, workbook);
    	            //判断textValue是否全部由数字组成
    	            if (textValue != null) {
    	                Pattern p = Pattern.compile("^//d+(//.//d+)?{1}quot;");
    	                Matcher matcher = p.matcher(textValue);
    	                if (matcher.matches()) {
    	                    // 是数字当作double处理
    	                    cell.setCellValue(Double.parseDouble(textValue));
    	                }else {
    	                    HSSFRichTextString richString = new HSSFRichTextString(textValue);
    	                    richString.applyFont(font2);
    	                    cell.setCellValue(richString);
    	                }
    	            }
    	            cellIndex++;
    			}
            }
            workbook.write(out);
    	}catch (IOException e) {
    		logger.error("导出{}  excel表格异常", title, e);
        }

    }

    private static String formatObject(Object value, SimpleDateFormat sdf, HSSFSheet sheet
    		, int rowIndex, int columnIndex, HSSFPatriarch patriarch, HSSFWorkbook workbook){
    	String textValue = null;
    	if (value instanceof Date) {
             Date date = (Date) value;
             textValue = sdf.format(date);
         }else if (value instanceof byte[]) {
             // 有图片时，设置行高为60px
        	 sheet.getRow(rowIndex).setHeightInPoints(60);
             // 设置图片所在列宽度为80px,注意这里单位的一个换算
             sheet.setColumnWidth(columnIndex, (short) (35.7 * 80));
             byte[] bsValue = (byte[]) value;
             HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,1023, 255, (short) 6, rowIndex, (short) 6, rowIndex);
             patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
         } else {
             // 其它数据类型都当作字符串简单处理
             textValue =value!=null?value.toString():"";
         }
    	
    	return textValue;
    }
}
