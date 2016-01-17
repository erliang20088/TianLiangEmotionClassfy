package test.zel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.zel.classfy.manager.analyzer.vsm.VsmManager;
import com.zel.classfy.pojos.CorpusItemPojo;
import com.zel.classfy.pojos.SentenceScore;
import com.zel.classfy.pojos.TermScorePojo;
import com.zel.classfy.pojos.TestParasPojo;
import com.zel.classfy.pojos.TestResultPojo;
import com.zel.classfy.pojos.status.EmotionPolarStatus;
import com.zel.entity.TermUnit;
import com.zel.util.io.IOUtil;

public class VsmTest {
	/**
	 * isMultiClass是指是否要分多个类,还是要分两个类
	 * 
	 * @param polarStatus
	 * @param testParasPojo
	 * @param isMultiClass
	 * @return
	 */
	public static TestResultPojo closeTest(EmotionPolarStatus polarStatus,
			TestParasPojo testParasPojo, boolean isMultiClass) {
		VsmManager vsmManager = new VsmManager();
		vsmManager.initVSM();

		TestResultPojo testResultPojo = new TestResultPojo();

		// List<CorpusItemPojo> testCorpusList =
		// vsmManager.getDbOperatorManager()
		// .getCorpusList(polarStatus);
		List<CorpusItemPojo> testCorpusList = vsmManager.getDbOperatorManager()
				.getCorpusList(EmotionPolarStatus.Positive,
						EmotionPolarStatus.Negative);

		List<TermUnit> termUnitList = null;

		double doc_number = 0;
		double right_doc_number = 0;

		List<SentenceScore> positiveList = new LinkedList<SentenceScore>();
		SentenceScore temp_SentScore = null;

		for (CorpusItemPojo corpusItem : testCorpusList) {
			if (corpusItem.getArticle() == null
					|| corpusItem.getArticle().trim().length() == 0) {
				continue;
			}
			termUnitList = vsmManager.getSkyLightAnalyzerManager()
					.getSplitPOSResult(corpusItem.getArticle());

			temp_SentScore = vsmManager.getSentencePolar(termUnitList);

			// if (temp_SentScore.getEmotionPolar() == polarStatus) {
			// right_doc_number++;
			// }
			if (temp_SentScore.getEmotionPolar() == corpusItem
					.getEmotionPolar()) {
				right_doc_number++;
			}
			doc_number++;
			positiveList.add(temp_SentScore);
		}
		Collections.sort(positiveList);

		int line_count = 0;

		int out_top = testParasPojo.getTopN();

		for (SentenceScore d : positiveList) {
			line_count++;
			if (line_count > out_top) {
				break;
			}
			// for (TermScorePojo termScore : d.allTermScoreList) {
			// System.out.print(termScore.getTermValue());
			// }
			// System.out.println();
			// System.out.println(line_count + "***    " + d);
		}

		testResultPojo.setPrecessionRate(1.0 * right_doc_number / doc_number);

		return testResultPojo;
	}

	public static void openTest(List<String> srcList) {
		VsmManager vsmManager = new VsmManager();
		vsmManager.initVSM();

		List<TermUnit> termUnitList = null;
		SentenceScore temp_SentScore = null;
		for (String corpusItem : srcList) {
			// termUnitList = vsmManager.getSkyLightAnalyzerManager()
			// .getSplitPOSResult(corpusItem);
			// // temp_SentScore = vsmManager.getEmotionScore(termUnitList);
			// temp_SentScore = vsmManager.getSentencePolar(termUnitList);
			// // System.out.println("情感得分    " + positiveSentScore);
			// // System.out.println("原始    " +
			// temp_SentScore.allTermScoreList);
			// // for (TermScorePojo termScore :
			// temp_SentScore.allTermScoreList) {
			// for (TermScorePojo termScore : temp_SentScore.getTermScoreList())
			// {
			// System.out.print(termScore.getTermValue() + ":"
			// + termScore.getTermScore());
			// }

			temp_SentScore = vsmManager.getSentencePolarV2(corpusItem);

			System.out.println();
			// System.out.println(temp_SentScore);
			System.out.println(temp_SentScore.getEmotionPolar());
			System.out.println(temp_SentScore.getSentenceScore());

			// System.out.println();
		}
	}

	public static void singlePolarCloseTest() {
		// 封闭测试
		TestParasPojo testParasPojo = new TestParasPojo();
		int test_count = 10;
		TestResultPojo testResultPojo = null;
		for (int i = 0; i < test_count; i++) {
			System.out.println("test paras " + testParasPojo);

			testResultPojo = closeTest(EmotionPolarStatus.Negative,
					testParasPojo, false);
			System.out.println("负向   " + testResultPojo);

			testResultPojo = closeTest(EmotionPolarStatus.Positive,
					testParasPojo, false);
			System.out.println("正向   " + testResultPojo);

			testParasPojo.reset();
		}

	}

	public static void main(String[] args) {
		// 封闭测试
		// TestParasPojo testParasPojo = new TestParasPojo();
		// int test_count = 15;
		// TestResultPojo testResultPojo = null;
		// for (int i = 0; i < test_count; i++) {
		// System.out.println("test paras " + testParasPojo);
		//
		// testResultPojo = closeTest(EmotionPolarStatus.Negative,
		// testParasPojo, false);
		//
		// System.out.println("正确率为   " + testResultPojo);
		//
		// testParasPojo.reset();
		// }

		// 开放测试
		List<String> srcList = new LinkedList<String>();
		// srcList.add("你真的很好");
		// srcList.add("你真的很不错");
		// srcList.add("这老师讲课真的很差!");
		// String article = "这老师讲课真的很差很不错!";
		String article = "学生对他很不牛逼；";
		// String article = "张老师讲课条理清晰，学生对他很满意；";
		// String article = IOUtil.readFile("d://test.txt", "UTF-8");
		// String article = IOUtil.readFile("d://test2.txt", "UTF-8");
		srcList.add(article);
		
		openTest(srcList);
	}
}
