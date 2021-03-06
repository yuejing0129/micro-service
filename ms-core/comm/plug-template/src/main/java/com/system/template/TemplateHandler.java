package com.system.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.system.template.core.FreemarkerTemplate;
import com.system.template.core.TemplateCore;
import com.system.template.enums.TemplateType;

/**
 * 模板处理入口类
 * @author yuejing
 * @date 2017年10月27日 下午2:42:07
 */
public class TemplateHandler {
	
	private TemplateCore templateCore;

	/**
	 * 保存的数据
	 */
	private Map<String, Object> dataMap = new HashMap<String, Object>();

	/**
	 * 创建Freemarker的模板
	 * @return
	 */
	public static TemplateHandler createFreemarker() {
		return new TemplateHandler(TemplateType.FREEMARKER);
	}
	private TemplateHandler(TemplateType templateType) {
		super();
		if(TemplateType.FREEMARKER == templateType) {
			templateCore = new FreemarkerTemplate();
		}
	}
	
	/**
	 * 创建模版文件到指定路径
	 * @param templatePath
	 * @param is
	 * @return
	 */
	public boolean createTemplateFile(String templatePath, InputStream is) {
		OutputStream os = null;
		try {
			File file = new File(templatePath);
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(os != null) {
					os.close();
				}
				if(is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/** 
	 * 根据模板生成word[后缀为：.doc]
	 * @param templatePath 	模板文件的地址
	 * @param createPath	 生成的文档输出地址
	 * @return
	 */
	public File generateWord(String templatePath, String createPath) {
		if(!createPath.endsWith(".doc") && !createPath.endsWith(".docx")) {
			throw new RuntimeException("生成了非word文件[" + createPath + "]");
		}
		return templateCore.process(dataMap, templatePath, createPath);
	}
	/** 
	 * 根据模板生成excel[后缀为：.xls]
	 * @param templatePath 	模板文件的地址
	 * @param createPath	 生成的文档输出地址
	 * @return
	 */
	public File generateExcel(String templatePath, String createPath) {
		if(!createPath.endsWith(".xls") && !createPath.endsWith(".xlsx")) {
			throw new RuntimeException("生成了非Excel文件[" + createPath + "]");
		}
		return templateCore.process(dataMap, templatePath, createPath);
	}
	
	/** 
	 * 根据模板生成文件，不受格式限制
	 * @param templatePath 	模板文件的地址
	 * @param createPath	 生成的文档输出地址
	 * @return
	 */
	public File generate(String templatePath, String createPath) {
		return templateCore.process(dataMap, templatePath, createPath);
	}
	
	/**
	 * 添加数据
	 * @param key	键
	 * @param value	值
	 */
	public void addValue(String key, Object value) {
		dataMap.put(key, value);
	}

	
}