package com.zel.classfy.util;

import java.text.NumberFormat;

/**
 * 数值型操作运算符
 * 
 * @author zel
 * 
 */
public class NumberOperatorUtil {
	private NumberFormat doubleFormat = NumberFormat.getNumberInstance();

	public NumberOperatorUtil() {
		doubleFormat.setMaximumFractionDigits(6);
	}

	public Double format(double srcDouble) {
		return Double.parseDouble(doubleFormat.format(srcDouble));
	}
}
