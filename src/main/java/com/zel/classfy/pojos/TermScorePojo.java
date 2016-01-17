package com.zel.classfy.pojos;

import java.io.Serializable;

import com.zel.classfy.pojos.status.EmotionPolarStatus;

/**
 * 每个词条的得分
 * 
 * @author zel
 * 
 */
public class TermScorePojo implements Serializable {
	// 为service中所添加，无其它用处
	public TermScorePojo() {

	}

	@Override
	public String toString() {
		return "TermScorePojo [natureValue=" + natureValue + ", polar=" + polar
				+ ", termScore=" + termScore + ", termValue=" + termValue + "]";
	}

	private String termValue;
	private EmotionPolarStatus polar;
	private int emotionTermIndex;

	// 某个分出来的term,在句子中的位移和词条的长度
	private int offset;
	private int term_value_length;

	public int getEmotionTermIndex() {
		return emotionTermIndex;
	}

	public void setEmotionTermIndex(int emotionTermIndex) {
		this.emotionTermIndex = emotionTermIndex;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getTerm_value_length() {
		return term_value_length;
	}

	public void setTerm_value_length(int termValueLength) {
		term_value_length = termValueLength;
	}

	public EmotionPolarStatus getPolar() {
		return polar;
	}

	public int getSign() {
		if (EmotionPolarStatus.Negative == polar) {
			return -1;
		}
		return 1;
	}

	public void setPolar(EmotionPolarStatus polar) {
		this.polar = polar;
	}

	public TermScorePojo(String termValue, String natureValue, double termScore) {
		this.termValue = termValue;
		this.natureValue = natureValue;
		this.termScore = termScore;
	}

	public TermScorePojo(String termValue, String natureValue,
			EmotionPolarStatus polar) {
		this.termValue = termValue;
		this.natureValue = natureValue;
		this.polar = polar;
	}

	public TermScorePojo(String termValue, String natureValue,
			EmotionPolarStatus polar, int emotionTermIndex, int offset,
			int term_length) {
		this.termValue = termValue;
		this.natureValue = natureValue;
		this.polar = polar;
		this.emotionTermIndex = emotionTermIndex;
		this.offset = offset;
		this.term_value_length = term_length;
	}

	public String getTermValue() {
		return termValue;
	}

	public void setTermValue(String termValue) {
		this.termValue = termValue;
	}

	public String getNatureValue() {
		return natureValue;
	}

	public void setNatureValue(String natureValue) {
		this.natureValue = natureValue;
	}

	public double getTermScore() {
		return termScore;
	}

	public void setTermScore(double termScore) {
		this.termScore = termScore;
	}

	private String natureValue;
	private double termScore;
}