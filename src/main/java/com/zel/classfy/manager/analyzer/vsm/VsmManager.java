package com.zel.classfy.manager.analyzer.vsm;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zel.classfy.manager.DB.DbOperatorManager;
import com.zel.classfy.manager.analyzer.FeatureVecExtManager;
import com.zel.classfy.manager.analyzer.SkyLightAnalyzerManager;
import com.zel.classfy.pojos.CorpusItemPojo;
import com.zel.classfy.pojos.SentenceScore;
import com.zel.classfy.pojos.TermScorePojo;
import com.zel.classfy.pojos.status.EmotionPolarStatus;
import com.zel.classfy.util.MathCalcUtil;
import com.zel.classfy.util.StaticValue;
import com.zel.classfy.util.SystemParas;
import com.zel.classfy.util.log.MyLogger;
import com.zel.entity.TermUnit;
import com.zel.util.io.ObjectIoUtil;

/**
 * 向量空间模型的构建管理器
 * 
 * @author zel
 * 
 */
public class VsmManager {
	MyLogger logger = new MyLogger(VsmManager.class);

	private SkyLightAnalyzerManager skyLightAnalyzerManager;
	// 正向空间向量
	// private Map<String, Double> positiveVectorMap = new HashMap<String,
	// Double>();
	// 负向空间向量
	// private Map<String, Double> negativeVectorMap = new HashMap<String,
	// Double>();
	// 情感向量，对正、负向量的综合,主要是用来判断某条语句是否有情感倾感
	private static Map<String, Double> emotionVectorMap = new HashMap<String, Double>();
	// 代表是否发现正向、负向的情感词汇
	private boolean find_nagative = false;
	private boolean find_positive = false;

	public Map<String, Double> getEmotionVectorMap() {
		return emotionVectorMap;
	}

	public void setEmotionVectorMap(Map<String, Double> emotionVectorMap) {
		this.emotionVectorMap = emotionVectorMap;
	}

	// 四个阀值的设置
	private double positive_threshold_max;
	private double positive_threshold_min;

	private double negative_threshold_max;
	private double negative_threshold_min;

	public static double calcTermScore(String key) {
		if (emotionVectorMap.containsKey(key)) {
			return emotionVectorMap.get(key);
		}
		return StaticValue.default_emotion_term_score;
	}

	public double getPositive_threshold_max() {
		return positive_threshold_max;
	}

	public void setPositive_threshold_max(double positiveThresholdMax) {
		positive_threshold_max = positiveThresholdMax;
	}

	public double getPositive_threshold_min() {
		return positive_threshold_min;
	}

	public void setPositive_threshold_min(double positiveThresholdMin) {
		positive_threshold_min = positiveThresholdMin;
	}

	public double getNegative_threshold_max() {
		return negative_threshold_max;
	}

	public void setNegative_threshold_max(double negativeThresholdMax) {
		negative_threshold_max = negativeThresholdMax;
	}

	public double getNegative_threshold_min() {
		return negative_threshold_min;
	}

	public void setNegative_threshold_min(double negativeThresholdMin) {
		negative_threshold_min = negativeThresholdMin;
	}

	public SkyLightAnalyzerManager getSkyLightAnalyzerManager() {
		return skyLightAnalyzerManager;
	}

	public void setSkyLightAnalyzerManager(
			SkyLightAnalyzerManager skyLightAnalyzerManager) {
		this.skyLightAnalyzerManager = skyLightAnalyzerManager;
	}

	public DbOperatorManager getDbOperatorManager() {
		return dbOperatorManager;
	}

	public void setDbOperatorManager(DbOperatorManager dbOperatorManager) {
		this.dbOperatorManager = dbOperatorManager;
	}

	private DbOperatorManager dbOperatorManager;

	public VsmManager() {
		this.skyLightAnalyzerManager = new SkyLightAnalyzerManager();
		this.initVSM();
	}

	public void initVSM() {
		// 以下这个方法，主要是对语气词(o)的计算
		// trainNegative();
		// trainPositive();
		// 在此统一训练情感度
		if (SystemParas.cache_vsm_vector_enable
				&& new File(SystemParas.cache_vsm_vector_path).exists()) {
			emotionVectorMap = (Map<String, Double>) ObjectIoUtil
					.readObject(SystemParas.cache_vsm_vector_path);
			logger.info("成功--加载空间向量集合emotionVectorMap!");
		} else {
			// 首先初始化数据库中的语料集合
			this.dbOperatorManager = new DbOperatorManager();
			trainAll();
			if (SystemParas.cache_vsm_vector_enable) {
				ObjectIoUtil.writeObject(SystemParas.cache_vsm_vector_path,
						VsmManager.emotionVectorMap);
				logger.info("成功--缓存空间向量集合emotionVectorMap!");
			}
		}
	}

	// 封闭式测试
	public void closeTest() {
		List<CorpusItemPojo> testCorpusList = this.dbOperatorManager
				.getCorpusList(EmotionPolarStatus.Positive);
		// 每个句子的分词结果集合
		List<TermUnit> termUnitList = null;
		// 帮助得到threshold值的集合类
		List<SentenceScore> scoreSortList = new LinkedList<SentenceScore>();
		SentenceScore temp_SentScore = null;
		for (CorpusItemPojo corpusItem : testCorpusList) {
			if (corpusItem.getArticle() == null
					|| corpusItem.getArticle().trim().length() == 0) {
				continue;
			}
			termUnitList = this.skyLightAnalyzerManager
					.getSplitPOSResult(corpusItem.getArticle());

			scoreSortList.add(temp_SentScore);
		}
		Collections.sort(scoreSortList);

		positive_threshold_min = scoreSortList.get(0).getSentenceScore();
		positive_threshold_max = scoreSortList.get(scoreSortList.size() - 1)
				.getSentenceScore();

		scoreSortList.clear();
		testCorpusList = this.dbOperatorManager
				.getCorpusList(EmotionPolarStatus.Negative);
		for (CorpusItemPojo corpusItem : testCorpusList) {
			if (corpusItem.getArticle() == null
					|| corpusItem.getArticle().trim().length() == 0) {
				continue;
			}
			termUnitList = this.skyLightAnalyzerManager
					.getSplitPOSResult(corpusItem.getArticle());

			scoreSortList.add(temp_SentScore);
		}
		Collections.sort(scoreSortList);

		negative_threshold_min = scoreSortList.get(0).getSentenceScore();
		negative_threshold_max = scoreSortList.get(scoreSortList.size() - 1)
				.getSentenceScore();

	}

	// 统一在语料中去
	public void trainAll() {
		List<CorpusItemPojo> corpusList = dbOperatorManager.getCorpusList(
				EmotionPolarStatus.Positive, EmotionPolarStatus.Negative);

		FeatureVecExtManager featureVecExtManager = new FeatureVecExtManager(
				this.skyLightAnalyzerManager);
		// 真正去训练
		featureVecExtManager.extFeature4SentenceList(corpusList,
				StaticValue.emotionWordSet, true);

		Map<String, Integer> emotionMap = featureVecExtManager
				.getWordDocFreqMap();

		Set<String> keySet = emotionMap.keySet();
		for (String key : keySet) {
			emotionVectorMap.put(key, MathCalcUtil.getWeigth(
					featureVecExtManager.getTotalDocNumber(),
					emotionMap.get(key)));
		}
	}

	// 得到句子的情感度评分，不分正负，只看该句子有无情感
	public SentenceScore getEmotionScore(List<TermUnit> termUnitList) {
		if (termUnitList == null || termUnitList.isEmpty()) {
			return null;
		}
		// 每个句子的得分
		SentenceScore sentScore = new SentenceScore();
		TermScorePojo termScorePojo = null;

		double score = 0;
		String termValue = null;
		String natureValue = null;
		boolean isMiddle = true;

		// 用来指示某个情感词在句子中的位置，越往后权重越高,乘以该位置
		int emotion_word_multi = 1;
		for (TermUnit termUnit : termUnitList) {
			termValue = termUnit.getValue();
			natureValue = termUnit.getNatureTermUnit().getTermNatureItem()
					.getNatureItem().getName();
			// System.out.println("natureValue***" + natureValue);
			// 只要这三种词，正、负、否定词
			if (StaticValue.denyWordSet.contains(termValue)) {
				termScorePojo = new TermScorePojo(termValue, natureValue,
						EmotionPolarStatus.Deny, emotion_word_multi,
						termUnit.getOffset(), termUnit.getLength());
				// System.out.println("取得否");
				sentScore.addTermScore(termScorePojo);

				emotion_word_multi++;
			} else if (StaticValue.negativeWordSet.contains(termValue)) {
				isMiddle = false;
				termScorePojo = new TermScorePojo(termValue, natureValue,
						EmotionPolarStatus.Negative, emotion_word_multi,
						termUnit.getOffset(), termUnit.getLength());

				sentScore.addTermScore(termScorePojo);
				emotion_word_multi++;
			} else if (StaticValue.positiveWordSet.contains(termValue)) {
				isMiddle = false;
				termScorePojo = new TermScorePojo(termValue, natureValue,
						EmotionPolarStatus.Positive, emotion_word_multi,
						termUnit.getOffset(), termUnit.getLength());
				sentScore.addTermScore(termScorePojo);

				emotion_word_multi++;
			} else if (StaticValue.special_nature_set.contains(natureValue)) {
				termScorePojo = new TermScorePojo(termValue, natureValue,
						EmotionPolarStatus.Middle);
				sentScore.addTermScore(termScorePojo);
			}
			// 为了测试，发布时，不需要这里
			termScorePojo = new TermScorePojo(termValue, natureValue, 0.0);
			sentScore.allTermScoreList.add(termScorePojo);
		}
		sentScore.setSentenceScore(score);
		sentScore.setMiddle(isMiddle);
		// 提前计算极性，不需要让上层去再调用这边
		sentScore.setEmotionPolar(sentScore.getSentEmotionPolar());
		sentScore.setEmotionPolarDetail(sentScore.getSentEmotionPolarDetail());

		return sentScore;
	}

	// 第二个版本
	public SentenceScore getEmotionScoreV2(int txt_length,
			List<TermUnit> termUnitList) {
		if (termUnitList == null || termUnitList.isEmpty()) {
			return null;
		}
		// 每个句子的得分
		SentenceScore sentScore = new SentenceScore();
		TermScorePojo termScorePojo = null;

		double score = 0;
		String termValue = null;
		String natureValue = null;
		boolean isMiddle = true;

		// 重置find状态位
		find_nagative = false;
		find_positive = false;

		// 用来指示某个情感词在句子中的位置，越往后权重越高,乘以该位置
		int emotion_word_multi = 1;
		for (TermUnit termUnit : termUnitList) {
			termValue = termUnit.getValue();
			natureValue = termUnit.getNatureTermUnit().getTermNatureItem()
					.getNatureItem().getName();
			// System.out.println("natureValue***" + natureValue);
			// 只要这三种词，正、负、否定词
			if (StaticValue.denyWordSet.contains(termValue)) {
				termScorePojo = new TermScorePojo(termValue, natureValue,
						EmotionPolarStatus.Deny, emotion_word_multi,
						termUnit.getOffset(), termUnit.getLength());
				// System.out.println("取得否");
				sentScore.addTermScore(termScorePojo);

				emotion_word_multi++;
			} else if (StaticValue.negativeWordSet.contains(termValue)) {
				isMiddle = false;
				termScorePojo = new TermScorePojo(termValue, natureValue,
						EmotionPolarStatus.Negative, emotion_word_multi,
						termUnit.getOffset(), termUnit.getLength());

				sentScore.addTermScore(termScorePojo);
				emotion_word_multi++;
				find_nagative = true;
			} else if (StaticValue.positiveWordSet.contains(termValue)) {
				isMiddle = false;
				termScorePojo = new TermScorePojo(termValue, natureValue,
						EmotionPolarStatus.Positive, emotion_word_multi,
						termUnit.getOffset(), termUnit.getLength());
				sentScore.addTermScore(termScorePojo);

				emotion_word_multi++;
				find_positive = true;
			} else if (StaticValue.special_nature_set.contains(natureValue)) {
				termScorePojo = new TermScorePojo(termValue, natureValue,
						EmotionPolarStatus.Middle);
				sentScore.addTermScore(termScorePojo);
			}
			// 为了测试，发布时，不需要这里
			termScorePojo = new TermScorePojo(termValue, natureValue, 0.0);
			sentScore.allTermScoreList.add(termScorePojo);
		}
		sentScore.setSentenceScore(score);
		sentScore.setMiddle(isMiddle);
		
		sentScore.setSource_char_length(txt_length);
		sentScore.setFind_nagative(find_nagative);
		sentScore.setFind_positive(find_positive);
		
		// 提前计算极性，不需要让上层去再调用这边
		sentScore.setEmotionPolar(sentScore.getSentEmotionPolar());
		sentScore.setEmotionPolarDetail(sentScore.getSentEmotionPolarDetail());

		return sentScore;
	}

	// 得到某条句子的极性
	public SentenceScore getSentencePolar(List<TermUnit> termUnitList) {
		SentenceScore sentenceScore = getEmotionScore(termUnitList);
		return sentenceScore;
	}

	public SentenceScore getSentencePolar(String sentence) {
		List<TermUnit> termUnitList = this.skyLightAnalyzerManager
				.getSplitPOSResult(sentence);
		SentenceScore sentenceScore = getEmotionScore(termUnitList);
		return sentenceScore;
	}

	public SentenceScore getSentencePolarV2(String sentence) {
		List<TermUnit> termUnitList = this.skyLightAnalyzerManager
				.getSplitPOSResult(sentence);
		int txt_length = 0;
		if (termUnitList != null) {
			txt_length = sentence.length();
		}
		SentenceScore sentenceScore = getEmotionScoreV2(txt_length,
				termUnitList);
		return sentenceScore;
	}

	public static void main(String[] args) throws Exception {
		VsmManager vsmManager = new VsmManager();
		vsmManager.initVSM();

		Map<String, Double> emotionVectorMap = vsmManager.getEmotionVectorMap();
		Set<String> emotionKeySet = emotionVectorMap.keySet();
		for (String tmp : emotionKeySet) {
			System.out.println(tmp + "   " + emotionVectorMap.get(tmp));
		}
		System.out.println("emotionKeySet size---" + emotionKeySet.size());

	}
}
