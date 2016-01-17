package test.zel;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.zel.classfy.pojos.CorpusItemPojo;
import com.zel.classfy.pojos.status.EmotionPolarStatus;
import com.zel.classfy.util.DBUtils;
import com.zel.classfy.util.StaticValue;
import com.zel.classfy.util.SystemParas;
import com.zel.classfy.util.io.IOUtil;

/**
 * 将File形式的微博语料库导入到数据库中
 * 
 * @author zel
 * 
 */
public class ImportCorpusToDB {
	public static DBUtils dbUtil = null;
	public static Statement stat = null;
	static {
		dbUtil = new DBUtils(SystemParas.remote_ip,"3306","entityevalplatform",
				SystemParas.remote_db_username,
				SystemParas.remote_db_userpassword);
		stat = dbUtil.getStat();
	}

	public static EmotionPolarStatus getPolarStatus(String status) {
		if (status.equals("高兴") || status.equals("2")) {
			return EmotionPolarStatus.Positive;
		} else if (status.equals("中性")) {
			return EmotionPolarStatus.Middle;
		} else {
			return EmotionPolarStatus.Negative;
		}
	}

	public static String getFormatString(String article) {
		if (article.contains("@")) {
			return null;
		} else {
			return article.replaceAll("(#.*#)", "");
		}
	}

	/**
	 * 正向、负向、中性语料都导入
	 * 
	 * @throws Exception
	 */
	public static void importCorpusToDb_1() throws Exception {
		String root_path = "D:/micro_corpus_repair/1/";
		String corpusFile_status = root_path + "status.txt";
		String corpusFile_raw = root_path + "raw.txt";
		int all_line_number = 20000;
		String[][] status_array = new String[all_line_number][4];
		String[] content_array = new String[all_line_number];

		List<CorpusItemPojo> itemsArray = new LinkedList<CorpusItemPojo>();

		String srcString = IOUtil.readFile(corpusFile_status,
				StaticValue.default_encoding);

		StringReader sr = new StringReader(srcString);
		BufferedReader br = new BufferedReader(sr);

		String temp_line = null;
		String[] temp_array = null;
		int line_count = 0;
		while ((temp_line = br.readLine()) != null) {
			temp_array = temp_line.split(StaticValue.separator_space);
			status_array[line_count] = temp_array;
			line_count++;
		}

		srcString = IOUtil.readFile(corpusFile_raw,
				StaticValue.default_encoding);

		sr = new StringReader(srcString);
		br = new BufferedReader(sr);
		line_count = 0;// 还原一下计数器
		while ((temp_line = br.readLine()) != null) {
			// System.out.println("***"+temp_line);
			content_array[line_count] = temp_line;
			line_count++;
		}

		// 组合参数进入数据库
		CorpusItemPojo itemPojo = null;
		for (int i = 0; i < line_count; i++) {
			// if (status_array[i][2].equals("Y")) {
			itemPojo = new CorpusItemPojo();
			itemPojo.setDocId(i + 1);
			itemPojo.setFromOrigin("1");
			itemPojo.setArticle(content_array[i]);
			itemPojo.setEmotionPolar(getPolarStatus(status_array[i][1]));

			itemsArray.add(itemPojo);
			// }
		}

		// System.out.println(srcString);
		System.out.println("line_count---" + line_count);
		System.out.println("itemsArray.size()---" + itemsArray.size());

		String query_line = "insert into corpus(docid,article,fromorigin,polar) value (?,?,?,?)";

		line_count = 0;
		String format_article = null;
		PreparedStatement preStat = null;

		preStat = dbUtil.getPreparedStat(query_line);
		for (CorpusItemPojo tempItemPojo : itemsArray) {
			preStat.setLong(1, tempItemPojo.getDocId());
			format_article = getFormatString(tempItemPojo.getArticle());
			if (format_article != null) {
				preStat.setString(2, format_article);
			} else {
				continue;
			}
			preStat.setString(3, tempItemPojo.getFromOrigin());
			preStat.setString(4, tempItemPojo.getEmotionPolar() + "");

			preStat.addBatch();
			// preStat.execute();
			line_count++;
		}
		int[] retNumber = preStat.executeBatch();

		br.close();

		System.out.println("应插入line_count---" + line_count);
		System.out.println("实插入-------------" + retNumber.length);
	}

	public static void importCorpusToDb_2() throws Exception {
		String root_path = "D:/micro_corpus_repair/2/";
		String corpusFile_status = root_path + "status.txt";
		String corpusFile_raw = root_path + "raw.txt";
		int all_line_number = 20000;
		String[][] status_array = new String[all_line_number][4];
		String[] content_array = new String[all_line_number];

		List<CorpusItemPojo> itemsArray = new LinkedList<CorpusItemPojo>();

		String srcString = IOUtil.readFile(corpusFile_status,
				StaticValue.default_encoding);

		StringReader sr = new StringReader(srcString);
		BufferedReader br = new BufferedReader(sr);

		String temp_line = null;
		String[] temp_array = null;
		int line_count = 0;
		while ((temp_line = br.readLine()) != null) {
			temp_array = temp_line.split(StaticValue.separator_space);
			status_array[line_count] = temp_array;
			line_count++;
		}

		srcString = IOUtil.readFile(corpusFile_raw,
				StaticValue.default_encoding);

		sr = new StringReader(srcString);
		br = new BufferedReader(sr);
		line_count = 0;// 还原一下计数器
		while ((temp_line = br.readLine()) != null) {
			// System.out.println("***"+temp_line);
			content_array[line_count] = temp_line;
			line_count++;
		}

		// 组合参数进入数据库
		CorpusItemPojo itemPojo = null;
		int have_emotion_num = 0;
		int none_emotion_num = 0;
		for (int i = 0; i < line_count; i++) {
			if (status_array[i][0].equals("1")) {
				itemPojo = new CorpusItemPojo();
				itemPojo.setDocId(i + 1);
				itemPojo.setFromOrigin("2");
				itemPojo.setArticle(content_array[i]);
				// if(i+1==8924){
				// System.out.println("***********"+content_array[i]);
				// }
				itemPojo.setEmotionPolar(getPolarStatus(status_array[i][1]));

				itemsArray.add(itemPojo);

				have_emotion_num++;
			} else {// 无情感为中性
				itemPojo = new CorpusItemPojo();
				itemPojo.setDocId(i + 1);
				itemPojo.setFromOrigin("2");
				itemPojo.setArticle(content_array[i]);
				// if(i+1==8924){
				// System.out.println("***********"+content_array[i]);
				// }
				itemPojo.setEmotionPolar(EmotionPolarStatus.Middle);
				itemsArray.add(itemPojo);

				none_emotion_num++;
			}
		}
		System.out.println("有情感共---" + have_emotion_num);
		System.out.println("无情感共---" + none_emotion_num);

		// System.out.println(srcString);
		System.out.println("共有数据记录line_count---" + line_count);
		System.out.println("可入库记录itemsArray.size()---" + itemsArray.size());

		String query_line = "insert into corpus(docid,article,fromorigin,polar) value (?,?,?,?)";

		line_count = 0;
		String format_article = null;
		PreparedStatement preStat = null;

		preStat = dbUtil.getPreparedStat(query_line);
		for (CorpusItemPojo tempItemPojo : itemsArray) {
			preStat.setLong(1, tempItemPojo.getDocId());
			format_article = getFormatString(tempItemPojo.getArticle());
			if (format_article != null) {
				preStat.setString(2, format_article);
			} else {
				System.out.println(tempItemPojo.getDocId() + "*****"
						+ format_article);
				continue;
			}
			preStat.setString(3, tempItemPojo.getFromOrigin());
			preStat.setString(4, tempItemPojo.getEmotionPolar() + "");

			preStat.addBatch();
			line_count++;
		}
		int[] retNumber = preStat.executeBatch();

		br.close();

		System.out.println("应插入line_count---" + line_count);
		System.out.println("实插入-------------" + retNumber.length);
	}

	public static void main(String[] args) throws Exception {
		importCorpusToDb_1();
		importCorpusToDb_2();
	}
}
