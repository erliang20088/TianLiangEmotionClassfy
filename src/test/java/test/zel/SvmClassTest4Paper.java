package test.zel;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.zel.classfy.manager.analyzer.vsm.VsmManager;
import com.zel.classfy.pojos.CorpusItemPojo;
import com.zel.classfy.pojos.SentenceScore;
import com.zel.classfy.pojos.status.EmotionPolarStatus;
import com.zel.classfy.util.DBUtils;
import com.zel.entity.TermUnit;
import com.zel.util.io.IOUtil;

/**
 * 为论文测试数据与实验专用类
 * 
 * @author zel
 * 
 */
public class SvmClassTest4Paper {
	/**
	 * 封闭测试时候的各种不同极性数据的准确率测试
	 */
	public static void testAllKindsRate() {
		VsmManager vsmManager = new VsmManager();
		vsmManager.initVSM();

		EmotionPolarStatus emotionStatus = EmotionPolarStatus.Positive;
		EmotionPolarStatus emotionStatus_reverse = EmotionPolarStatus.Negative;

		EmotionPolarStatus emotionStatus_middle = EmotionPolarStatus.Middle;

		// List<CorpusItemPojo> list = vsmManager.getDbOperatorManager()
		// .getCorpusList(emotionStatus);
		// List<CorpusItemPojo> list = vsmManager.getDbOperatorManager()
		// .getCorpusList(EmotionPolarStatus.Positive);
		// List<CorpusItemPojo> list = vsmManager.getDbOperatorManager()
		// .getCorpusList();
		List<CorpusItemPojo> list = vsmManager.getDbOperatorManager()
				.getCorpusList(EmotionPolarStatus.Positive,
						EmotionPolarStatus.Negative);

		List<TermUnit> termUnitList = null;
		SentenceScore temp_SentScore = null;

		int total_doc_number = 0;

		int total_doc_right_number = 0;

		int total_doc_positive_right_test = 0;
		int total_doc_negative_right_test = 0;
		int total_doc_middle_right_test = 0;

		int total_doc_positive_result_test = 0;
		int total_doc_negative_result_test = 0;
		int total_doc_middle_result_test = 0;

		int total_doc_positive_should = 0;
		int total_doc_negative_should = 0;
		int total_doc_middle_should = 0;

		/**
		 * 构造新集合,用来分批做测试
		 */
		int total_corpus_size = list.size();
		List<CorpusItemPojo> newList = new LinkedList<CorpusItemPojo>();

		int begin = 000;
		int length = 1000000;
		// int length = 200;
		int end = 0;
		end = Math.min(begin + length, list.size());
		for (int i = begin; i < end; i++) {
			newList.add(list.get(i));
		}

		for (CorpusItemPojo corpusItem : newList) {
			// System.out.println(corpusItem.getArticle());
			termUnitList = vsmManager.getSkyLightAnalyzerManager()
					.getSplitPOSResult(corpusItem.getArticle());
			// temp_SentScore = vsmManager.getEmotionScore(termUnitList);
			temp_SentScore = vsmManager.getSentencePolar(termUnitList);

			if (temp_SentScore == null) {
				continue;
			}
			if (temp_SentScore.getEmotionPolar() == corpusItem
					.getEmotionPolar()) {
				if (temp_SentScore.getEmotionPolar() == EmotionPolarStatus.Positive) {
					total_doc_positive_right_test++;
				}
				if (temp_SentScore.getEmotionPolar() == EmotionPolarStatus.Negative) {
					total_doc_negative_right_test++;
				}
				if (temp_SentScore.getEmotionPolar() == EmotionPolarStatus.Middle) {
					total_doc_middle_right_test++;
				}
				total_doc_right_number++;
			}

			if (temp_SentScore.getEmotionPolar() == EmotionPolarStatus.Positive) {
				total_doc_positive_result_test++;
			} else if (temp_SentScore.getEmotionPolar() == EmotionPolarStatus.Negative) {
				total_doc_negative_result_test++;
			} else if (temp_SentScore.getEmotionPolar() == EmotionPolarStatus.Middle) {
				total_doc_middle_result_test++;
			}

			if (corpusItem.getEmotionPolar() == EmotionPolarStatus.Positive) {
				total_doc_positive_should++;
			} else if (corpusItem.getEmotionPolar() == EmotionPolarStatus.Negative) {
				total_doc_negative_should++;
			} else if (corpusItem.getEmotionPolar() == EmotionPolarStatus.Middle) {
				total_doc_middle_should++;
			}
			total_doc_number++;
		}

		double precision = 1.0 * total_doc_right_number / total_doc_number;
		double recall_positive = 1.0 * total_doc_positive_right_test
				/ total_doc_positive_should;
		double recall_negative = 1.0 * total_doc_negative_right_test
				/ total_doc_negative_should;
		double recall_middle = 1.0 * total_doc_middle_right_test
				/ total_doc_middle_should;

		/**
		 * 正确的判定结果个数
		 */
		System.out.println("正确的判定结果----");
		System.out.println("total_doc_positive_right_test---"
				+ total_doc_positive_right_test);
		System.out.println("total_doc_negative_right_test---"
				+ total_doc_negative_right_test);
		System.out.println("total_doc_middle_right_test---"
				+ total_doc_middle_right_test);
		System.out.println("----------------------");

		System.out.println("计算后实际的判定结果----");
		System.out.println("total_doc_positive_result_test---"
				+ total_doc_positive_result_test);
		System.out.println("total_doc_negative_result_test---"
				+ total_doc_negative_result_test);
		System.out.println("total_doc_middle_result_test---"
				+ total_doc_middle_result_test);
		System.out.println("----------------------");

		System.out.println("语料中应该判定结果----");
		System.out.println("total_doc_positive_should---"
				+ total_doc_positive_should);
		System.out.println("total_doc_negative_should---"
				+ total_doc_negative_should);
		System.out.println("total_doc_middle_should---"
				+ total_doc_middle_should);
		System.out.println("----------------------");

		System.out.println("total_doc_number---" + total_doc_number);

		System.out.println("precision---" + precision);
		System.out.println("recall_positive---" + recall_positive);
		System.out.println("recall_negative---" + recall_negative);
		System.out.println("recall_middle---" + recall_middle);
	}

	public static void main(String[] args) throws Exception {
		String serverIp = "127.0.0.1";
		String userName = "root";
		String userPwd = "zhouking";
		String port = "3306";
		String dbName = "weibospider";

		DBUtils dbUtils = new DBUtils(serverIp, port, dbName, userName, userPwd);

		Statement stat = dbUtils.getStat();
		// String sql="select count(*) as cnt from sina_person";
//		String sql = "select * from doc where person_id=1197161814";
		String sql = "select * from doc where person_id=1266321801";
		
		ResultSet rs = stat.executeQuery(sql);

		int rowNumber = 0;
		String cnt = null;

		int positive_count = 0;
		int negative_count = 0;
		int middle_count = 0;

		VsmManager vsmManager = new VsmManager();
		List<TermUnit> termUnitList = null;
		SentenceScore temp_SentScore=null;
		
		StringBuilder sb=new StringBuilder();
		
		while (rs.next()) {
			String id = rs.getString("id");
			String sendurl = rs.getString("sendurl");
			String docurl = rs.getString("docurl");
			String article = rs.getString("article");
			String publishtime = rs.getString("publishtime");

			sb.append(article);
			sb.append("\n");
			// System.out.println("id---" + id);
			// System.out.println("sendurl---" + sendurl);
			// System.out.println("docurl---" + docurl);
			// System.out.println("article---" + article);
			// System.out.println("publishtime---" + publishtime);

//			termUnitList = vsmManager.getSkyLightAnalyzerManager()
//					.getSplitPOSResult(article);
//			// temp_SentScore = vsmManager.getEmotionScore(termUnitList);
//			temp_SentScore = vsmManager.getSentencePolar(termUnitList);
//
//			if(temp_SentScore.getEmotionPolar()==EmotionPolarStatus.Positive){
//				positive_count++;
//			}else if(temp_SentScore.getEmotionPolar()==EmotionPolarStatus.Negative){
//				negative_count++;
//			}else if(temp_SentScore.getEmotionPolar()==EmotionPolarStatus.Middle){
//				middle_count++;
//			}
			
//			rowNumber++;
		}
//		System.out.println("rowNumber---" + rowNumber);
//		System.out.println("positive_count---" + positive_count);
//		System.out.println("negative_count---" + negative_count);
//		System.out.println("middle_count---" + middle_count);
		
		IOUtil.writeFile("d://article.txt",sb.toString());
		// System.out.println(dbUtils);
	}
}
