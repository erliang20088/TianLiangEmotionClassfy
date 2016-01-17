package com.zel.classfy.util;

import com.zel.classfy.util.io.ReadConfigUtil;

/**
 * 系统参数配置
 * 
 * @author zel
 * 
 */
public class SystemParas {
	public static ReadConfigUtil readConfigUtil = new ReadConfigUtil(
			"emotion_classfy_config.properties", true);

	/**
	 * 20M大小的一个字节数组,在加载缓存文件时，用字节数组的方式，一次性读取进内存，大大加块了IO速度,不再是FileInputStream去直接读取
	 */
	public static int cache_trie_init_load_bytes_length = Integer
			.parseInt(readConfigUtil
					.getValue("cache.trie.init.load.bytes.length"));
	/**
	 * 日志管理
	 */
	public static boolean log_output_enable = Boolean
			.parseBoolean(readConfigUtil.getValue("log_output_enable"));

	/**
	 * data import,remote db config paras
	 */
	public static String remote_ip = readConfigUtil.getValue("remote_ip");
	public static String remote_db_username = readConfigUtil
			.getValue("remote_db_username");
	public static String remote_db_userpassword = readConfigUtil
			.getValue("remote_db_userpassword");

	/**
	 * 读取情感词典的路径
	 */
	public static String dic_system_emotion_path = readConfigUtil
			.getValue("dic.system.emotion.path");// 词典所在目录

	public static String dic_system_positive_path = readConfigUtil
			.getValue("dic.system.positive.path");// 词典所在目录

	public static String dic_system_negative_path = readConfigUtil
			.getValue("dic.system.negative.path");// 词典所在目录

	public static String dic_system_deny_path = readConfigUtil
			.getValue("dic.system.deny.path");// 词典所在目录

	/**
	 * 缓存生成的空间向量，即map集合
	 */
	public static String cache_vsm_vector_path = readConfigUtil
			.getValue("cache.vsm.vector.path");// 词典所在目录
	public static boolean cache_vsm_vector_enable = Boolean
			.parseBoolean(readConfigUtil.getValue("cache.vsm.vector.enable"));

}
