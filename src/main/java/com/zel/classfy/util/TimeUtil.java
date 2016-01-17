package com.zel.classfy.util;

public class TimeUtil {
	/**
	 * 将ms转化成minutes和seconds的形式
	 *  
	 * @param millionSecond
	 * @return
	 */
	public static String getMinuteAndSecond(long millionSecond) {
		long seconds = millionSecond / 1000;// 得到多少s
		long minutes = seconds / 60;
		seconds = seconds % 60;
		String ms = "" + (millionSecond % 1000);
		int temp_length = ms.length();
		for (; temp_length < 3; temp_length++) {
			ms = "0" + ms;
		}
		return minutes + "分" + seconds + "." + ms + "秒";
	}

	public final static double million = 1000 * 1.0;

	// 得到所用的的ms
	public static double getSecond(long millionSecond) {
		double seconds = millionSecond / million;// 得到多少s
		return seconds;
	}

	public static String getMinuteAndSecond(long content_length,
			long millionSecond) {
		long seconds = millionSecond / 1000;// 得到多少s
		long minutes = seconds / 60;
		seconds = seconds % 60;
		String ms = "" + (millionSecond % 1000);
		int temp_length = ms.length();
		for (; temp_length < 3; temp_length++) {
			ms = "0" + ms;
		}
		System.out.println("处理速度为---"
				+ (content_length / Double.parseDouble(seconds + "." + ms))
				+ " 字符/秒");
		return minutes + "分" + seconds + "." + ms + "秒";
	}

	/**
	 * 计算分词的速率
	 */
	public static int getSplitSpeed(int length, long millionSeconds) {
		return (int) (length / getSecond(millionSeconds));
	}
}
