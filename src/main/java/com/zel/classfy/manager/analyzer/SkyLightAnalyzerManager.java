package com.zel.classfy.manager.analyzer;

import java.util.LinkedList;
import java.util.List;

import com.zel.classfy.manager.DB.DbOperatorManager;
import com.zel.classfy.pojos.CorpusItemPojo;
import com.zel.classfy.pojos.status.EmotionPolarStatus;
import com.zel.core.analyzer.StandardAnalyzer;
import com.zel.entity.TermUnit;
import com.zel.interfaces.analyzer.Analyzer;

/**
 * 分词管理器
 * 
 * @author zel
 * 
 */
public class SkyLightAnalyzerManager {
	public static Analyzer analyzer = new StandardAnalyzer();

	public static List<TermUnit> getSplitResult(String src) {
		return analyzer.getSplitResult(src);
	}

	public static List<TermUnit> getSplitPOSResult(String src) {
		if(src==null || src.trim().length()==0){
			return null;
		}
		return analyzer.getSplitMergePOSResult(src);
	}
	
	public static List<TermUnit> getSplitMergePOSResult(String src) {
		return analyzer.getSplitMergePOSResult(src);
	}

	public static void main(String[] args) {
		// 提取数据库中的资料
		DbOperatorManager dbOperatorManager = new DbOperatorManager();
		List<CorpusItemPojo> testCorpusList = dbOperatorManager
				.getCorpusList(EmotionPolarStatus.Positive);

		List<String> srcList = new LinkedList<String>();

		// srcList.add("你真的很好");
		// srcList.add("你真的很不错");
		// srcList.add("哈哈哈哈!");
		// srcList.add("我爱你");
		// srcList.add("我受不了了");
		// srcList.add("你真的太坏了...");
		// srcList.add("你真很让人不爽,我要干你");
		// srcList.add("太无赖了");
		// srcList.add("在中国，效率最低的事，最赔钱的事，是养干部。但又不能不养，人家有枪。");

		// srcList.add("看节目看到哭泪");
		// srcList.add("笑死了，真搞笑");
		// srcList.add("安心走了");
		// srcList.add("啊我日");
		// srcList.add("都是泪!");
		// srcList.add("变态");
		// srcList.add("我爱你,但我更恨你");
		// srcList.add("真證的愛情須要個辦法,感興趣嗎");
		// srcList.add("愛人");
		// srcList.add("妻子");
		// srcList.add("好差劲");
		// srcList.add("笑哈哈");
		// srcList.add("是真的还是假的 ");
		// srcList.add("是太无赖了!");
		// srcList.add("无耻");
		// srcList.add("受不了");
		// srcList.add("不可");
		// srcList.add("操蛋");
		
//		srcList.add("22日");
//		srcList.add("万能的百度   ");
		srcList.add("你很好");

		// List<TermUnit> list = getSplitResult(src);
		// for (String str : srcList) {
		int topN = 20;
		int begin = 0;
		for (String str : srcList) {
			begin++;
			if (begin == topN) {
				break;
			}
			// List<TermUnit> list = getSplitPOSResult(str);
			List<TermUnit> list = getSplitPOSResult(str);
			if (list != null) {
				for (TermUnit term : list) {
					// System.out.print(term.getValue() + ",");
					System.out.print(term.getValue()
							+ "/"
							+ term.getNatureTermUnit().getTermNatureItem()
									.getNatureItem().getName() + ",");
				}
				System.out.println();
			} else {
				System.out.println("分词结果为null");
			}
		}
	}
}
