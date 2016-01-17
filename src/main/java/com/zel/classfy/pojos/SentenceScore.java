package com.zel.classfy.pojos;

import java.util.Iterator;
import java.util.LinkedList;

import com.zel.classfy.manager.analyzer.vsm.VsmManager;
import com.zel.classfy.pojos.status.EmotionClassfyDetailStatus;
import com.zel.classfy.pojos.status.EmotionPolarStatus;
import com.zel.classfy.util.StaticValue;

/**
 * 句子的得分
 * 
 * @author zel
 * 
 */
public class SentenceScore implements Comparable<SentenceScore> {
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("sentenceScore=" + sentenceScore + ",polar:"
				+ this.getEmotionPolar() + StaticValue.separator_space);
		sb.append("sentenceScore=" + sentenceScore + ",polarDetail:"
				+ this.getEmotionPolarDetail() + StaticValue.separator_space);
		for (TermScorePojo temp : this.termScoreList) {
			sb.append(StaticValue.separator_space + temp.getTermValue()
					+ StaticValue.separator_space + temp.getNatureValue()
					+ StaticValue.separator_space + temp.getTermScore());
		}
		return sb.toString();
	}

	private LinkedList<TermScorePojo> termScoreList;

	public LinkedList<TermScorePojo> allTermScoreList = new LinkedList<TermScorePojo>();

	public SentenceScore() {
		this.termScoreList = new LinkedList<TermScorePojo>();
	}

	public void addTermScore(TermScorePojo termScorePojo) {
		this.termScoreList.add(termScorePojo);
	}

	public LinkedList<TermScorePojo> getTermScoreList() {
		return termScoreList;
	}

	public void setTermScoreList(LinkedList<TermScorePojo> termScoreList) {
		this.termScoreList = termScoreList;
	}

	public double getSentenceScore() {
		return sentenceScore;
	}

	public void setSentenceScore(double sentenceScore) {
		this.sentenceScore = sentenceScore;
	}

	public int getSource_char_length() {
		return source_char_length;
	}

	public void setSource_char_length(int source_char_length) {
		this.source_char_length = source_char_length;
	}

	public boolean isFind_nagative() {
		return find_nagative;
	}

	public void setFind_nagative(boolean find_nagative) {
		this.find_nagative = find_nagative;
	}

	public boolean isFind_positive() {
		return find_positive;
	}

	public void setFind_positive(boolean find_positive) {
		this.find_positive = find_positive;
	}

	private double sentenceScore;

	// 默认极性为空
	private EmotionPolarStatus emotionPolar = null;

	// 具体的细分类属于哪一个
	private EmotionClassfyDetailStatus emotionPolarDetail = null;

	public EmotionClassfyDetailStatus getEmotionPolarDetail() {
		return emotionPolarDetail;
	}

	public void setEmotionPolarDetail(
			EmotionClassfyDetailStatus emotionPolarDetail) {
		this.emotionPolarDetail = emotionPolarDetail;
	}

	private boolean isMiddle = true;

	public boolean isMiddle() {
		return isMiddle;
	}

	public void setMiddle(boolean isMiddle) {
		this.isMiddle = isMiddle;
	}

	public EmotionPolarStatus getEmotionPolar() {
		return emotionPolar;
	}

	public void setEmotionPolar(EmotionPolarStatus emotionPolar) {
		this.emotionPolar = emotionPolar;
	}

	@Override
	public int compareTo(SentenceScore o) {
		if (this.getSentenceScore() > o.getSentenceScore()) {
			return 1;
		}
		return 0;
	}

	public double getNatureMulti(String nature) {
		if (StaticValue.addWeightNatureSet.contains(nature)) {
			return StaticValue.add_weight_rate;
		}
		if (StaticValue.subWeightNatureSet.contains(nature)) {
			return StaticValue.sub_weight_rate;
		}
		return 1.0;
	}

	public double getEmotionTermIndexMulti(int location) {
		// if (location > StaticValue.deny_emotion_length_max) {
		if (location > StaticValue.emotion_offset_max) {
			return location;
		}
		return StaticValue.deny_emotion_length_default;
	}

	// 得到四小分类的具体类别
	public EmotionClassfyDetailStatus getSentEmotionPolarDetail() {
		if (termScoreList.isEmpty() || isMiddle) {
			emotionPolarDetail = EmotionClassfyDetailStatus.Middle;
			return emotionPolarDetail;
		}
		double temp_sum = this.getSentenceScore();
		if (emotionPolar == EmotionPolarStatus.Positive) {
			if (temp_sum > StaticValue.positive_classfy_threshold) {
				return EmotionClassfyDetailStatus.Positive_Best;
			} else {
				return EmotionClassfyDetailStatus.Positive_Normal;
			}
		} else {
			// 因为负向的阀值为负数，故用小于号
			if (temp_sum < StaticValue.negative_classfy_threshold) {
				return EmotionClassfyDetailStatus.Negative_Angry;
			} else {
				return EmotionClassfyDetailStatus.Negative_Bad;
			}
		}
	}

	private int source_char_length;
	private boolean find_nagative = false;
	private boolean find_positive = false;

	// 得到正负极性值
	public EmotionPolarStatus getSentEmotionPolar() {
		if (termScoreList.isEmpty() || isMiddle) {
			emotionPolar = EmotionPolarStatus.Middle;
		} else {
			Iterator<TermScorePojo> iterator = termScoreList.iterator();
			TermScorePojo termScorePojo = null;

			boolean currentSign = true;// 代表+号,false代表-号,前边是否有否定前缀的判断
			double sum_score = 0;
			double temp_term_score = 0;
			// 主要是为了解决问句,当出现问句时,将该变量置反,简单处理方法
			double block_sum_score = 0;

			String temp_nature = null;

			// 用来计算否定与所修饰词的位置的
			int deny_term_offsetAddLength = 0;

			boolean last_currentSign = true;
			EmotionPolarStatus last_emotionPolarStatus = null;
			int last_emotion_term_index = 0;// 上一个情感词性的位置，用来计算算否定词后边的连续性
			int last_emotion_term_offset = 0;
			String temp_term_value = null;

			while (iterator.hasNext()) {
				termScorePojo = iterator.next();

				temp_nature = termScorePojo.getNatureValue();
				temp_term_value = termScorePojo.getTermValue();

				// 句子中，分段计分，当出现special_nature_set中的元素时，位置的权重影响将取消
				if (StaticValue.special_nature_set.contains(temp_nature)) {
					if (!currentSign) {
						currentSign = true;
					}

					// 对特殊的分隔符加强调
					if (StaticValue.special_sign_tag_add_set
							.contains(temp_term_value)) {
						sum_score += (block_sum_score * StaticValue.add_weight_rate_4_special_tag);
					} else if (StaticValue.special_tag_reverse_set
							.contains(temp_term_value)) {// 在这里对问句的处理
						sum_score += (-1 * block_sum_score);
					} else {
						// 如果没有问号,则正常作和
						sum_score += block_sum_score;
					}
					// 作和之后,清空重计值
					block_sum_score = 0;

					continue;
				}
				// 在continue后边做polar的赋值
				emotionPolar = termScorePojo.getPolar();

				if (emotionPolar == EmotionPolarStatus.Deny) {// 改变下符号
					if (termScorePojo.getOffset() - (deny_term_offsetAddLength) > StaticValue.deny_emotion_length_max) {
						// 为连续的否定符号做变换
						currentSign = (currentSign == false);
						// 得到否定词所在的位移位置
						deny_term_offsetAddLength = termScorePojo.getOffset()
								+ termScorePojo.getTerm_value_length();
					} else {// 说明上一个否定义没有修饰情感词
						// 直接false
						currentSign = false;
						// 得到否定词所在的位移位置
						deny_term_offsetAddLength = termScorePojo.getOffset()
								+ termScorePojo.getTerm_value_length();
					}
					continue;
				} else {
					if ((!currentSign)
							&& termScorePojo.getOffset()
									- (deny_term_offsetAddLength) > StaticValue.deny_emotion_length_max) {
						currentSign = true;
					}
					if (currentSign) {// 正号
						temp_term_score = VsmManager
								.calcTermScore(termScorePojo.getTermValue())
								* termScorePojo.getSign()
								* getNatureMulti(termScorePojo.getNatureValue())
								* getEmotionTermIndexMulti(termScorePojo
										.getEmotionTermIndex());

						// 做否定词的后续情感词的连续词
						if (termScorePojo.getEmotionTermIndex()
								- last_emotion_term_index == 1
								&& last_emotionPolarStatus == termScorePojo
										.getPolar()
								&& (!last_currentSign)
								&& termScorePojo.getOffset()
										- last_emotion_term_offset < 2) {
							temp_term_score = (-1) * temp_term_score;
						}

					} else {// 负号计算
						temp_term_score = (-1)
								* VsmManager.calcTermScore(termScorePojo
										.getTermValue())
								* termScorePojo.getSign()
								* getNatureMulti(termScorePojo.getNatureValue())
								* getEmotionTermIndexMulti(termScorePojo
										.getEmotionTermIndex());
					}
					block_sum_score += temp_term_score;

					deny_term_offsetAddLength = 0;// 将deny_term_offsetAddLength位置还原
				}

				last_emotion_term_index = termScorePojo.getEmotionTermIndex();// 得到上一个情感实词的位置
				// 对上一个单元的currentSign、polar作备份，为了否定词作用于连续的情感词汇
				last_currentSign = currentSign;
				last_emotionPolarStatus = emotionPolar;
				last_emotion_term_offset = termScorePojo.getOffset()
						+ termScorePojo.getTerm_value_length();

				currentSign = true;// 对符号还原为正
			}

			// 追加一下最后可能未加的块值
			sum_score += block_sum_score;

			// 设置下总得分
			this.setSentenceScore(sum_score);

			if (this.source_char_length>150 && find_positive == true && find_nagative == true) {
				if (sum_score > 0) {
					emotionPolar = EmotionPolarStatus.Positive;
				} else {
					emotionPolar = EmotionPolarStatus.Negative;
				}
				if (Math.abs(sum_score) < 10) {
					emotionPolar = EmotionPolarStatus.Middle;
//					this.setSentenceScore(0);
				}
			} else {
				if (sum_score > 0) {
					emotionPolar = EmotionPolarStatus.Positive;
				} else {
					emotionPolar = EmotionPolarStatus.Negative;
				}
			}
		}
		return emotionPolar;
	}
}
