package com.zel.classfy.pojos;

import com.zel.classfy.util.StaticValue;

/**
 * 测试参数的pojo类,主要是加、减权重等的参数
 * 
 * @author zel
 * 
 */
public class TestParasPojo {
	public TestParasPojo() {
		this.sub_weight_rate = StaticValue.sub_weight_rate;
		this.add_weight_rate = StaticValue.add_weight_rate;
		this.default_emotion_term_score = StaticValue.default_emotion_term_score;
		this.deny_emotion_length_max = StaticValue.deny_emotion_length_max;
		this.emotion_offset_max = StaticValue.emotion_offset_max;
		this.add_weight_rate_4_special_tag = StaticValue.add_weight_rate_4_special_tag;

		this.topN = StaticValue.output_top_n;
	}

	// 减少的权重的相乘因子
	private double sub_weight_rate;

	@Override
	public String toString() {
		return "TestParasPojo [add_weight_rate=" + add_weight_rate
				+ ", add_weight_rate_4_special_tag="
				+ add_weight_rate_4_special_tag
				+ ", default_emotion_term_score=" + default_emotion_term_score
				+ ", deny_emotion_length_max=" + deny_emotion_length_max
				+ ", emotion_offset_max=" + emotion_offset_max
				+ ", sub_weight_rate=" + sub_weight_rate + ", topN=" + topN
				+ "]";
	}

	// 增加的权重的相乘因子
	private double add_weight_rate;
	// 在emotion特征向量空间中，当没有找到某个特征向量时，给它的默认权重
	private double default_emotion_term_score;
	// 当否定词与实体情感词共现的时候，最大的距离限制
	private int deny_emotion_length_max;
	// 负责情感词偏移量权值的调整
	private int emotion_offset_max;

	public int getEmotion_offset_max() {
		return emotion_offset_max;
	}

	public void setEmotion_offset_max(int emotionOffsetMax) {
		emotion_offset_max = emotionOffsetMax;
	}

	// 输出到console中的多少个结果集
	private int topN;

	private double add_weight_rate_4_special_tag;

	public double getAdd_weight_rate_4_special_tag() {
		return add_weight_rate_4_special_tag;
	}

	public void setAdd_weight_rate_4_special_tag(
			double addWeightRate_4SpecialTag) {
		add_weight_rate_4_special_tag = addWeightRate_4SpecialTag;
	}

	public int getDeny_emotion_length_max() {
		return deny_emotion_length_max;
	}

	public void setDeny_emotion_length_max(int denyEmotionLengthMax) {
		deny_emotion_length_max = denyEmotionLengthMax;
	}

	public int getTopN() {
		return topN;
	}

	public void setTopN(int topN) {
		this.topN = topN;
	}

	public double getSub_weight_rate() {
		return sub_weight_rate;
	}

	public void setSub_weight_rate(double subWeightRate) {
		sub_weight_rate = subWeightRate;
	}

	public double getAdd_weight_rate() {
		return add_weight_rate;
	}

	public void setAdd_weight_rate(double addWeightRate) {
		add_weight_rate = addWeightRate;
	}

	public double getDefault_emotion_term_score() {
		return default_emotion_term_score;
	}

	public void setDefault_emotion_term_score(double defaultEmotionTermScore) {
		default_emotion_term_score = defaultEmotionTermScore;
	}

	public void reset() {
		this.setSub_weight_rate(this.getSub_weight_rate());
		// this.setSub_weight_rate(this.getSub_weight_rate() * 0.9);

		this.setAdd_weight_rate(this.getAdd_weight_rate());
		// this.setAdd_weight_rate(this.getAdd_weight_rate() * 1.1);

		this
				.setDefault_emotion_term_score(this
						.getDefault_emotion_term_score());
		// this.setDefault_emotion_term_score(this.getDefault_emotion_term_score()*1.5);

		this.setDeny_emotion_length_max(StaticValue.deny_emotion_length_max);
//		this.setDeny_emotion_length_max(this.getDeny_emotion_length_max() + 1);

		this.setAdd_weight_rate_4_special_tag(this
				.getAdd_weight_rate_4_special_tag());
//		this.setAdd_weight_rate_4_special_tag(this
//				.getAdd_weight_rate_4_special_tag() * 1.1);

		this.setEmotion_offset_max(this.getEmotion_offset_max());
		// this.setEmotion_offset_max(this.getEmotion_offset_max() + 1);

		resetStaticValue();
	}

	public void resetStaticValue() {
		StaticValue.sub_weight_rate = this.sub_weight_rate;
		StaticValue.add_weight_rate = this.add_weight_rate;
		StaticValue.default_emotion_term_score = this.default_emotion_term_score;
		StaticValue.deny_emotion_length_max = this.deny_emotion_length_max;
		StaticValue.add_weight_rate_4_special_tag = this.add_weight_rate_4_special_tag;
		StaticValue.emotion_offset_max = this.emotion_offset_max;
	}
}
