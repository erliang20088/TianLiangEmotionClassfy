package com.zel.classfy.pojos;

/**
 * 特征向量的pojo类
 * 
 * @author zel
 * 
 */
public class FeatureVecPojo {
	// 特征向量的值，即name
	private String featureName;
	// 特征向量的权重
	private double weight;

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

}
