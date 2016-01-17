package com.zel.classfy.pojos;

import com.zel.classfy.pojos.status.EmotionPolarStatus;

/**
 * 博文语料的条目的pojo类
 * 
 * @author zel
 * 
 */
public class CorpusItemPojo {
	public CorpusItemPojo() {

	}

	public CorpusItemPojo(int docId, String article, String fromOrigin,
			EmotionPolarStatus emotionPolar) {
		this.docId = docId;
		this.article = article;
		this.fromOrigin = fromOrigin;
		this.emotionPolar = emotionPolar;
	}

	@Override
	public String toString() {
		return "CorpusItemPojo [article=" + article + ", docId=" + docId
				+ ", emotionPolar=" + emotionPolar + ", fromOrigin="
				+ fromOrigin + "]";
	}

	private int docId;

	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getFromOrigin() {
		return fromOrigin;
	}

	public void setFromOrigin(String fromOrigin) {
		this.fromOrigin = fromOrigin;
	}

	public EmotionPolarStatus getEmotionPolar() {
		return emotionPolar;
	}

	public void setEmotionPolar(EmotionPolarStatus emotionPolar) {
		this.emotionPolar = emotionPolar;
	}

	private String article;
	private String fromOrigin;
	private EmotionPolarStatus emotionPolar;

	public static EmotionPolarStatus getEmotionPolarStatus(String status) {
       if(status.equalsIgnoreCase(EmotionPolarStatus.Negative.toString())){
    	   return EmotionPolarStatus.Negative;
       }else {
    	   return EmotionPolarStatus.Positive;
       }
	}
}
