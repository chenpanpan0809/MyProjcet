package com.pan.files.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
/*import org.springframework.web.multipart.MultipartFile;*/
 


public class ImportExcel  {

	private File  files; //上传的文件路径
	private String filesFileName; //文件名
	
	
	
	

	public File getFiles() {
		return files;
	}


	public void setFiles(File files) {
		this.files = files;
	}


	public String getFilesFileName() {
		return filesFileName;
	}


	public void setFilesFileName(String filesFileName) {
		this.filesFileName = filesFileName;
	}
  /*
	这个是jxl方式
	dir:文件地址
	
  */
	/*public static String excelToJson(File dir ) throws BiffException, IOException {  
                jxl.Workbook wb =jxl.Workbook.getWorkbook(dir); // 从文件流中获取Excel工作区对象（WorkBook）  
                jxl.Sheet sheet = wb.getSheet(0); // 从工作区中取得页（Sheet）  
                jxl.Cell[] header = sheet.getRow(0);  
                String json1 = "";
                String[] arry = new String[sheet.getRows()];
                for (int i = 1; i < sheet.getRows(); i++) { // 循环打印Excel表中的内容  
                    Map hashMap = new HashMap();
                    ArrayList List = new ArrayList();
                    for (int j = 0; j < sheet.getColumns(); j++) {  
                    	jxl.Cell cell = sheet.getCell(j, i);  
                        List.add(cell.getContents()); 
                    }  
                    // 这个json字符串就是我们想要的，实际应用中可以直接返回该字符串  
                   // String json = JSONObject.toJSONString(hashMap); 
                //    String json = JSONObject.toJSONString(List); 
                    String json = com.alibaba.fastjson.JSONArray.toJSONString(List); 
                    json1 += json+",";
                
                } 
  
              
                return "["+ json1.substring(0,json1.length()-1)+"]excel";
            } */
	
	  
	    public  void targetFile() throws Exception { 
	        //根据服务器的文件保存地址和原文件名创建目录文件全路径 
	        String dirs= ServletActionContext.getServletContext() 
                    .getRealPath("/image");
	        if (files != null){
	            File savedir = new File(dirs) ;
	            if (! savedir.exists()) {
	                savedir.mkdirs() ;
	            }
	        }
	       //将上传到服务器上的二进制文件写成实际文件
	           File savefile = new File(dirs, filesFileName); 
	           FileUtils.copyFile(files, savefile) ;
	       //      String paremt =  excelToJson(savefile);
	    LinkedHashMap<String, String> sjson =   excelTojson(savefile);
	    JSONObject js = (JSONObject) JSON.parse(sjson.get("Sheet1"));
     JSONArray    myJsonArray =JSONArray.fromObject(JSON.parse(sjson.get("Sheet1")));
   //  JSON.parse(sjson.get("Sheet1"));
	  //  myJsonArray  = new JSONArray(JSON.parseArray(sjson.get("Sheet1")));
	 JSONObject jsonExcel =   createExcel(dirs+"\\结果列表.xls",myJsonArray);
	           HttpServletResponse response = ServletActionContext.getResponse();
	           
	           response.setHeader("Content-type", "text/html;charset=UTF-8");  
	         //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859  
	         response.setCharacterEncoding("UTF-8");  
	           PrintWriter out = response.getWriter();
	
	     //    out.println(sjson);
      out.println(jsonExcel);
	    } 
	   
	    public static LinkedHashMap<String,String> excelTojson(File file) throws IOException, Exception {
	        // 返回的map
	        LinkedHashMap<String,String> excelMap = new LinkedHashMap<>();
	        // Excel列的样式，主要是为了解决Excel数字科学计数的问题
	        CellStyle cellStyle;
	        // 根据Excel构成的对象
	        Workbook wb;
	        // 如果是2007及以上版本，则使用想要的Workbook以及CellStyle
	        if(file.getName().endsWith("xlsx")){   //07及07以后版本
	            wb = new XSSFWorkbook(new FileInputStream(file));
	            XSSFDataFormat dataFormat = (XSSFDataFormat) wb.createDataFormat();
	            cellStyle = wb.createCellStyle();
	            // 设置Excel列的样式为文本
	            cellStyle.setDataFormat(dataFormat.getFormat("@"));
	        }else{    //03版本
	            POIFSFileSystem fs = new POIFSFileSystem(file);
	            wb = new HSSFWorkbook(fs);
	            HSSFDataFormat dataFormat = (HSSFDataFormat) wb.createDataFormat();
	            cellStyle = wb.createCellStyle();
	            // 设置Excel列的样式为文本
	            cellStyle.setDataFormat(dataFormat.getFormat("@"));
	        }
	 
	        // sheet表个数
	        int sheetsCounts = wb.getNumberOfSheets();
	        // 遍历每一个sheet
	        for (int i = 0; i < sheetsCounts; i++) {
	            Sheet sheet = wb.getSheetAt(i);
	            // 一个sheet表对于一个List
	            List list = new LinkedList();
	            // 将第一行的列值作为正个json的key
	            String[] cellNames;
	            // 取第一行列的值作为key
	            Row fisrtRow = sheet.getRow(0);
	            // 如果第一行就为空，则是空sheet表，该表跳过
	            if(null == fisrtRow){
	                continue;
	            }
	            // 得到第一行有多少列
	            int curCellNum = fisrtRow.getLastCellNum();
	            // 根据第一行的列数来生成列头数组
	            cellNames = new String[curCellNum];
	            // 单独处理第一行，取出第一行的每个列值放在数组中，就得到了整张表的JSON的key
	            for (int m = 0; m < curCellNum; m++) {
	                Cell cell = fisrtRow.getCell(m);
	                // 设置该列的样式是字符串
	                cell.setCellStyle(cellStyle);
	                cell.setCellType(Cell.CELL_TYPE_STRING);
	                // 取得该列的字符串值
	                cellNames[m] = cell.getStringCellValue();
	            }
	            for (String s : cellNames) {
	            }
	 
	            // 从第二行起遍历每一行
	            int rowNum = sheet.getLastRowNum();
	            System.out.println("总共有 " + rowNum + " 行");
	            for (int j = 1; j <= rowNum; j++) {
	                // 一行数据对于一个Map
	                LinkedHashMap rowMap = new LinkedHashMap();
	                // 取得某一行
	                Row row = sheet.getRow(j);
	                int cellNum = row.getLastCellNum();
	                // 遍历每一列
	                for (int k = 0; k < cellNum; k++) {
	                    Cell cell = row.getCell(k);
	 
	                    cell.setCellStyle(cellStyle);
	                    cell.setCellType(Cell.CELL_TYPE_STRING);
	                    // 保存该单元格的数据到该行中
	        rowMap.put(cellNames[k],cell.getStringCellValue());
	                }
	                // 保存该行的数据到该表的List中
	                list.add(rowMap);
	            }
	            // 将该sheet表的表名为key，List转为json后的字符串为Value进行存储
	            excelMap.put(sheet.getSheetName(),JSON.toJSONString(list,false));
	        }
	        wb.close();
	        return excelMap;
	    }
	 
	   /* 
	    src:定义下载的文件路径   
	*/
	    public static JSONObject createExcel(String src, JSONArray json) {
	        JSONObject result = new JSONObject(); // 用来反馈函数调用结果
	        try {
	            // 新建文件
	            File file = new File(src);
	            file.createNewFile();

	            OutputStream outputStream = new FileOutputStream(file);// 创建工作薄
	            jxl.write.WritableWorkbook writableWorkbook = jxl.Workbook.createWorkbook(outputStream);
	            jxl.write.WritableSheet sheet = writableWorkbook.createSheet("First sheet", 0);// 创建新的一页

//	            JSONArray jsonArray = json.getJSONArray("dt");// 得到data对应的JSONArray
	            JSONArray jsonArray = json;// 得到data对应的JSONArray
	            jxl.write.Label label; // 单元格对象
	            int column = 0; // 列数计数

	            // 将第一行信息加到页中。如：姓名、年龄、性别
	            JSONObject first = jsonArray.getJSONObject(0);
	            Iterator<String> iterator = first.keys(); // 得到第一项的key集合
	            while (iterator.hasNext()) { // 遍历key集合
	                String key = (String) iterator.next(); // 得到key
	                label = new jxl.write.Label(column++, 0, key); // 第一个参数是单元格所在列,第二个参数是单元格所在行,第三个参数是值
	                sheet.addCell(label); // 将单元格加到页
	            }

	            // 遍历jsonArray
	            for (int i = 0; i < jsonArray.size(); i++) {
	                JSONObject item = jsonArray.getJSONObject(i); // 得到数组的每项
	                iterator = item.keys(); // 得到key集合
	                column = 0;// 从第0列开始放
	                while (iterator.hasNext()) {
	                    String key = iterator.next(); // 得到key
	                    String value = item.getString(key); // 得到key对应的value
	                    label = new jxl.write.Label(column++, (i + 1), value); // 第一个参数是单元格所在列,第二个参数是单元格所在行,第三个参数是值
	                    sheet.addCell(label); // 将单元格加到页
	                }
	            }
	            writableWorkbook.write(); // 加入到文件中
	            writableWorkbook.close(); // 关闭文件，释放资源
	        } catch (Exception e) {
	            result.put("result", "failed"); // 将调用该函数的结果返回
	            result.put("reason", e.getMessage()); // 将调用该函数失败的原因返回
	            return result;
	        }

//	        result.put("result", "successed");
	        return result;
	    }
	      
	    public static void main(String [] args) throws FileNotFoundException{
	   // 	createJson();
	    	
	    }
	}   
	
	
	
	

