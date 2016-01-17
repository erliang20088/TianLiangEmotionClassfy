package com.zel.classfy.util;

import java.util.regex.Pattern;

public class RegexUtil {
	public Pattern pattern = null;

	public void init(String regex) {
		pattern=Pattern.compile(regex);
	}

	public boolean isMatch(String source) {
		return pattern.matcher(source).find();
	}
}
