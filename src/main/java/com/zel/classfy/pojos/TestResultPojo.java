package com.zel.classfy.pojos;

/**
 * 测试参数的pojo类,得到的测试结果的关键参数
 * 
 * @author zel
 * 
 */
public class TestResultPojo {
	@Override
	public String toString() {
		return "TestResultPojo [precessionRate=" + precessionRate + "]";
	}

	// 准确率
	private double precessionRate;

	public double getPrecessionRate() {
		return precessionRate;
	}

	public void setPrecessionRate(double precessionRate) {
		this.precessionRate = precessionRate;
	}
}
