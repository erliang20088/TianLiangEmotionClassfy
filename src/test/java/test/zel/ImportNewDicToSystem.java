package test.zel;

import com.zel.classfy.util.RegexPaserUtil;
import com.zel.classfy.util.StaticValue;
import com.zel.classfy.util.io.IOUtil;

/**
 * 将情感词典加入到分词器的词库当中
 * 
 * @author zel
 * 
 */
public class ImportNewDicToSystem {

	public static void importNegativeDic(int freq) {
		String begin = "<PHRASE>";
		String end = "</PHRASE>";
		String text = RegexPaserUtil.TEXTEGEXANDNRT;
		RegexPaserUtil regexPaserUtil = new RegexPaserUtil(begin, end, text);

		String rootPath = "D:/micro_corpus_repair/analyzer/";
		String srcPath = rootPath + "negative.xml";

		String destPath = rootPath + "negative.dic";

		String srcString = IOUtil.readFile(srcPath,
				StaticValue.default_encoding);

		regexPaserUtil.reset(srcString);

		int line_count = 0;
		StringBuilder sb = new StringBuilder();

		while (regexPaserUtil.hasNext()) {
			sb.append(regexPaserUtil.getText());
			sb.append(StaticValue.separator_tab);
			sb.append(2);// 代表已成词
			sb.append(StaticValue.separator_tab);
			sb.append("{emotion=1}");
			sb.append(StaticValue.separator_next_line);

			line_count++;
		}

		IOUtil.writeFile(destPath, sb.toString());

		System.out.println("finish!");
	}

	public static void importPositiveDic(int freq) {
		String begin = "<PHRASE>";
		String end = "</PHRASE>";
		String text = RegexPaserUtil.TEXTEGEXANDNRT;
		RegexPaserUtil regexPaserUtil = new RegexPaserUtil(begin, end, text);

		String rootPath = "D:/micro_corpus_repair/analyzer/";
		String srcPath = rootPath + "positive.xml";

		String destPath = rootPath + "positive.dic";

		String srcString = IOUtil.readFile(srcPath,
				StaticValue.default_encoding);

		// System.out.println(srcString);
		regexPaserUtil.reset(srcString);

		int line_count = 0;
		StringBuilder sb = new StringBuilder();

		while (regexPaserUtil.hasNext()) {
			sb.append(regexPaserUtil.getText());
			sb.append(StaticValue.separator_tab);
			sb.append(2);// 代表已成词
			sb.append(StaticValue.separator_tab);
			sb.append("{emotion=1}");
			sb.append(StaticValue.separator_next_line);

			line_count++;
		}

		IOUtil.writeFile(destPath, sb.toString());

		System.out.println("finish!");
	}

	public static void main(String[] args) throws Exception {
		int freq = 50;

		importNegativeDic(freq);
		importPositiveDic(freq);
	}
}
