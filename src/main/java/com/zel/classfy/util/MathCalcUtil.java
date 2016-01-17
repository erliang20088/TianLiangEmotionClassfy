package com.zel.classfy.util;

/**
 * 数学类函数计算工具类
 * 
 * @author zel
 * 
 */
public class MathCalcUtil {
	/**
	 * IDF,得到每个词的逆向最大文档的值,即每个词的权重,通过TF*IDF得到每个句子的情感极性得分
	 * 
	 * @param totalDoc4Corpus
	 * @param hitDoc4Sentence
	 * @return
	 */
	public static NumberOperatorUtil numberOperatorUtil = new NumberOperatorUtil();

	public static double getWeigth(double totalDoc4Corpus,
			double hitDoc4Sentence) {
		return numberOperatorUtil.format(Math.log(totalDoc4Corpus
				/ hitDoc4Sentence));
//		return numberOperatorUtil.format(hitDoc4Sentence / totalDoc4Corpus);
	}
}
