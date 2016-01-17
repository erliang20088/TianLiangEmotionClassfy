package com.zel.classfy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import com.zel.classfy.util.io.IOUtil;
import com.zel.manager.ComplexToSimpleManager;

/**
 * 静态变量配置
 *  
 * @author zel
 * 
 */
public class StaticValue {
	public static String default_encoding = "utf-8";
	public static String separator_space = " ";
	public static String separator_tab = "	";
	public static String separator_next_line = "\n";

	/**
	 * 新统计数据
	 */
	public static double sub_weight_rate = 1f;
	public static double add_weight_rate = 1.61f;
	public static int emotion_offset_max = 2;
	public static int deny_emotion_length_max = 1;

	/**
	 * 老统计数据
	 */
	// public static double sub_weight_rate = 0.4f;
	// public static double add_weight_rate = 4.0f;

	public static double add_weight_rate_4_special_tag = 2.6f;
	// public static double default_emotion_term_score = 5.0625;
	//
	// public static int deny_emotion_length_max = 3;
	// // public static int deny_emotion_length_max=0;
	// public static int deny_emotion_length_default = 1;

	/**
	 * paper测试
	 */
	// public static double sub_weight_rate = 1f;
	// public static double add_weight_rate = 1f;

	// public static double add_weight_rate_4_special_tag =1f;

	public static double default_emotion_term_score = 1;

	// public static int emotion_offset_max = 50;
	// public static int emotion_offset_max = 0;

	// public static int deny_emotion_length_max = 50;
	// public static int deny_emotion_length_max = 0;

	public static int deny_emotion_length_default = 1;

	public static int output_top_n = 10;

	/**
	 * 分多个类别时阀值设置，暂定为4类，即超赞 喜欢 不爽 愤怒
	 */
	public static double positive_classfy_threshold = 100;// 正面分类中的小两类，暂定为两类,超赞、喜欢
	public static double negative_classfy_threshold = -100;// 负面分类中的小两类，暂定为两类,讨厌、愤怒

	/**
	 * 在进行特征词提取时，要提取的特征词的词性--无条件提取的词性集合
	 */
	public static Set<String> featureNatureSet_must = new HashSet<String>();
	static {
		// String[] natureArray = { "a", "an", "ag", "e", "o", "y", "d", "ad",
		// "l", "i", "emotion", "unknown" };
		String[] natureArray = { "o" };
		for (String nature : natureArray) {
			featureNatureSet_must.add(nature);
		}
	}

	public static Set<String> special_nature_set = new HashSet<String>();
	static {
		String[] natureArray = { "w", "unknown" };
		for (String nature : natureArray) {
			special_nature_set.add(nature);
		}
	}

	public static Set<String> special_tag_reverse_set = new HashSet<String>();
	static {
		String[] tagArray = { "?", "？" };
		for (String nature : tagArray) {
			special_tag_reverse_set.add(nature);
		}
	}

	public static Set<String> special_sign_tag_add_set = new HashSet<String>();
	static {
		String[] tagArray = { "!", "！" };
		for (String nature : tagArray) {
			special_sign_tag_add_set.add(nature);
		}
	}

	/**
	 * 综合训练时的必须包含词性
	 */
	public static Set<String> emotion_featureNatureSet_must = new HashSet<String>();
	static {
		String[] natureArray = { "e", "o", "emotion" };
		for (String nature : natureArray) {
			emotion_featureNatureSet_must.add(nature);
		}
	}

	/**
	 * 综合训练时的必须包含词性--选择性提取的，跟情感词典中的词作对比
	 */
	public static Set<String> emotion_featureNatureSet_maybe = new HashSet<String>();
	static {
		String[] natureArray = { "a", "an", "n", "nr", "vn", "v", "j", "z",
				"l", "i", "en", "y" };
		for (String nature : natureArray) {
			emotion_featureNatureSet_maybe.add(nature);
		}
	}

	/**
	 * 在进行特征词提取时，要提取的特征词的词性--选择性提取的，跟情感词典中的词作对比
	 */
	public static Set<String> featureNatureSet_maybe = new HashSet<String>();
	static {
		String[] natureArray = { "n", "nr", "r", "vn", "v", "j", "c", "en" };
		for (String nature : natureArray) {
			featureNatureSet_maybe.add(nature);
		}
	}

	/**
	 * 弱化一些非关键的修饰词，主要是修正权重，如副词等
	 */
	public static Set<String> subWeightNatureSet = new HashSet<String>();
	static {
		String[] natureArray = { "n", "o", "y", "an" };
		for (String nature : natureArray) {
			subWeightNatureSet.add(nature);
		}
	}

	/**
	 * 强化一些非关键的修饰词，主要是修正权重，如形容器，动词，形容词名词用
	 */
	public static Set<String> addWeightNatureSet = new HashSet<String>();
	static {
		String[] natureArray = { "v", "a" };
		for (String nature : natureArray) {
			addWeightNatureSet.add(nature);
		}
	}

	/**
	 * 提取情感词集合的情感词条，加入到set集合中，对可选的词性集合进行筛选
	 */
	public static Set<String> negativeWordSet = new HashSet<String>();
	public static Set<String> positiveWordSet = new HashSet<String>();
	/**
	 * 存放所有的情感词,主要是对名词、动词等的筛选
	 */
	public static Set<String> emotionWordSet = new HashSet<String>();
	static {
		String emotion_string = IOUtil.readDirOrFile(
				SystemParas.dic_system_positive_path,
				StaticValue.default_encoding);
		BufferedReader br = new BufferedReader(new StringReader(emotion_string));
		String temp_line = null;
		try {
			while ((temp_line = br.readLine()) != null) {
				temp_line = ComplexToSimpleManager.getSimpleFont(temp_line
						.trim().split(StaticValue.separator_tab)[0]);
				positiveWordSet.add(temp_line);

				emotionWordSet.add(temp_line);
			}
			emotion_string = IOUtil.readDirOrFile(
					SystemParas.dic_system_negative_path,
					StaticValue.default_encoding);
			br = new BufferedReader(new StringReader(emotion_string));
			while ((temp_line = br.readLine()) != null) {
				temp_line = ComplexToSimpleManager.getSimpleFont(temp_line
						.trim().split(StaticValue.separator_tab)[0]);
				negativeWordSet.add(temp_line);

				emotionWordSet.add(temp_line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 否定词存放的词条set集合
	 */
	public static Set<String> denyWordSet = new HashSet<String>();
	static {
		String emotion_string = IOUtil.readDirOrFile(
				SystemParas.dic_system_deny_path, StaticValue.default_encoding);
		BufferedReader br = new BufferedReader(new StringReader(emotion_string));
		String temp_line = null;
		try {
			while ((temp_line = br.readLine()) != null) {
				// System.out.println("***" + temp_line);
				temp_line = ComplexToSimpleManager.getSimpleFont(temp_line
						.trim());
				// temp_line = temp_line.trim();
				denyWordSet.add(temp_line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		int i = 0;
		for (String temp : special_nature_set) {
			i++;
			System.out.println(i + "*****" + temp);
		}
		System.out.println(special_nature_set.contains("unknown"));
	}
}