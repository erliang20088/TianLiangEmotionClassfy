package com.zel.classfy.manager.analyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zel.classfy.pojos.CorpusItemPojo;
import com.zel.classfy.util.StaticValue;
import com.zel.entity.TermUnit;
import com.zel.entity.nature.NatureItem;
import com.zel.manager.ComplexToSimpleManager;

/**
 * 特征向量提取管理器
 * 
 * @author zel
 * 
 */
public class FeatureVecExtManager {
	private SkyLightAnalyzerManager skyLightAnalyzerManager;
	// 训练语料的所有文档数量，用来计算IDF
	private int totalDocNumber = 0;
	// word与其命令的doc的数量，亦是计算IDF
	private Map<String, Integer> wordDocFreqMap = new HashMap<String, Integer>();

	public int getTotalDocNumber() {
		return totalDocNumber;
	}

	public void setTotalDocNumber(int totalDocNumber) {
		this.totalDocNumber = totalDocNumber;
	}

	public Map<String, Integer> getWordDocFreqMap() {
		return wordDocFreqMap;
	}

	public void setWordDocFreqMap(Map<String, Integer> wordDocFreqMap) {
		this.wordDocFreqMap = wordDocFreqMap;
	}

	public FeatureVecExtManager(SkyLightAnalyzerManager skyLightAnalyzerManager) {
		this.skyLightAnalyzerManager = skyLightAnalyzerManager;
	}

	private Set<String> keyIdIndentySet = new HashSet<String>();

	// 构建词频的向量空间,为计算权重做准备
	public void addValueToMap(String key, long docId) {
		if (keyIdIndentySet.contains(key + docId)) {
			return;
		}
		keyIdIndentySet.add(key + docId);

		if (wordDocFreqMap.containsKey(key)) {
			wordDocFreqMap.put(key, wordDocFreqMap.get(key) + 1);
		} else {
			wordDocFreqMap.put(key, 1);
		}
	}

	// isAll指是否是综合的emotion训练，而非正、负向的训练
	public void extFeature4SentenceList(List<CorpusItemPojo> sentenceList,
			Set<String> emotionPolarSet, boolean isAll) {
		if (sentenceList == null) {
			return;
		}
		for (CorpusItemPojo corpusItemPojo : sentenceList) {
			extFeature4Sentence(corpusItemPojo.getDocId(), corpusItemPojo
					.getArticle(), emotionPolarSet, isAll);
		}
	}

	public void extFeature4Sentence(long docId, String sentence,
			Set<String> emotionPolarSet, boolean isAll) {
		if (sentence == null || sentence.trim().length() == 0) {
			return;
		}
		List<TermUnit> termUnitList = skyLightAnalyzerManager
				.getSplitPOSResult(sentence);
		NatureItem natureItem = null;
		String temp_value = null;
		String natureString = null;
		if (isAll) {
			for (TermUnit termUnit : termUnitList) {
				natureItem = termUnit.getNatureTermUnit().getTermNatureItem()
						.getNatureItem();
				temp_value = termUnit.getValue();
				natureString = natureItem.getName();

				// 不做判断，只要在情感词典中的词都加到向量空间中去
				if (StaticValue.emotion_featureNatureSet_must
						.contains(natureString)) {
					addValueToMap(temp_value, docId);
				} else if (emotionPolarSet.contains(ComplexToSimpleManager
						.getSimpleFont(temp_value))) {
					addValueToMap(temp_value, docId);
				}
			}
		} else {
			for (TermUnit termUnit : termUnitList) {
				natureItem = termUnit.getNatureTermUnit().getTermNatureItem()
						.getNatureItem();
				temp_value = termUnit.getValue();
				natureString = natureItem.getName();
				if (StaticValue.featureNatureSet_must.contains(natureString)) {
					// addValueToMap(temp_value, docId);
					emotionPolarSet.add(temp_value);// 将语气词加入情感集合中
				}
			}
		}

		totalDocNumber++;
	}
}
