package com.zel.classfy.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 配置文件读取工具类
 * 
 * @author zel
 * 
 */
public class ReadConfigUtil {
	public InputStream in = null;
	public BufferedReader br = null;
	private Properties config = null;

	/**
	 * 处理非property文件,如文本
	 * 
	 * @param configFilePath
	 */
	public ReadConfigUtil(String configFilePath) {
		try {
			in = ReadConfigUtil.class.getClassLoader().getResourceAsStream(
					configFilePath);
			br = new BufferedReader(new InputStreamReader(in));
		} catch (Exception e) {
			System.out.println("加载slaves文件时，出现问题!");
		}
	}

	// 加载properties格式的配置文件
	public ReadConfigUtil(String configFilePath, boolean flag) {
		in = ReadConfigUtil.class.getClassLoader().getResourceAsStream(
				configFilePath);
		config = new Properties();
		try {
			config.load(in);
			in.close();
		} catch (IOException e) {
			System.out.println("load配置文件出错!");
		}
	}

	public String getValue(String key) {
		try {
			String value = config.getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("ConfigInfoError" + e.toString());
			return null;
		}
	}

	public String getTextLines() {
		StringBuilder sb = new StringBuilder();
		String temp = null;
		try {
			while ((temp = br.readLine()) != null) {
				if (temp.trim().length() > 0 && (!temp.trim().startsWith("#"))) {
					sb.append(temp);
					sb.append("\n");
				}
			}
			br.close();
			in.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("读取slaves文件时，出现问题!");
		}
		return sb.toString();
	}

	public static void main(String args[]) {
		// ReadConfigUtil readConfig=new ReadConfigUtil("slaves");
		// System.out.println(readConfig.getTextLines());
	}
}
