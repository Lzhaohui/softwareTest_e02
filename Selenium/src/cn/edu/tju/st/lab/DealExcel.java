package cn.edu.tju.st.lab;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class DealExcel {
	
	//excel工作表
	private final Workbook wb;
	//数据个数
	private int numOfStudent;
	//数据内容
	private String[] ids;
	private String[] names;
	private String[] urls;
	
	//工厂模式
	public static DealExcel create(String filepath)
			throws EncryptedDocumentException, InvalidFormatException, IOException{
		//根据文件路径读取文件
    	File file=new File(filepath);
    	InputStream in = new FileInputStream(file);
    	//使用poi创建工作表
        Workbook wb = WorkbookFactory.create(in);
        //调用私有构造函数
        return new DealExcel(wb);
	}
	
	private DealExcel(Workbook wb){
        if (wb == null) {
            throw new NullPointerException("Workbook is null, please check path or file.");
        }
        //如果读取出来excel文件，那么就用该实例存储他
        this.wb = wb;
        //初始化数据
        init();
	}
	
	//将excel文件数据转化到成员变量上
	private void init(){
		//第一个sheet
		Sheet sheet = wb.getSheetAt(0);
		//获取数据项个数
		this.numOfStudent = sheet.getLastRowNum() - 1;
		//初始化成员变量
		ids = new String[this.numOfStudent];
		names = new String[this.numOfStudent];
		urls = new String[this.numOfStudent];
		//用于放置读取出的excel数据为科学计数法的形式
		DecimalFormat df = new DecimalFormat("0");
		//逐行读取
		for(int i = 0; i < this.numOfStudent; i++){
			Row row = sheet.getRow(i+2);
			//存到成员变量
			ids[i] = df.format(row.getCell(1).getNumericCellValue());
			names[i] = row.getCell(2).getStringCellValue();
			String url = row.getCell(3).getStringCellValue();
			//删除空格，防止与html的url出现因空格导致的不一致
			urls[i] = url.replaceAll(" ", "");
		}
	}
	
	//获得数据个数
	public int getNum(){
		return this.numOfStudent;
	}
	
	//根据索引获取学号
	public String getId(int i){
		if(0 <= i && i < numOfStudent)
			return ids[i];
		return null;
	}
	
	//根据索引获取密码，为学号的后6位
	public String getPassword(int i){
		String id = this.getId(i);
		return (id == null) ? null : id.substring(4, 10);
	}
	
	//根据索引获取姓名
	public String getName(int i){
		if(0 <= i && i < numOfStudent)
			return names[i];
		return null;
	}
	
	//根据索引获取git地址
	public String getUrl(int i){
		if(0 <= i && i < numOfStudent)
			return urls[i];
		return null;
	}
}
