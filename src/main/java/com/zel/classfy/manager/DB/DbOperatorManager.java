package com.zel.classfy.manager.DB;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.zel.classfy.pojos.CorpusItemPojo;
import com.zel.classfy.pojos.status.EmotionPolarStatus;
import com.zel.classfy.util.DBUtils;
import com.zel.classfy.util.SystemParas;

/**
 * 数据库操作管理类
 * 
 * @author zel
 * 
 */
public class DbOperatorManager {
	/**
	 * 数据库中语料的加载
	 */
	public DBUtils dbUtil = null;
	public Statement stat = null;

	public DbOperatorManager(){
		dbUtil = new DBUtils(SystemParas.remote_ip,"3306","entityevalplatform",
				SystemParas.remote_db_username,
				SystemParas.remote_db_userpassword);
		stat = dbUtil.getStat();		
	}
	
	/**
	 * 得到正负面的数据库中的语料
	 * 
	 * @param emotionPolarStatus
	 * @return
	 */
	public List<CorpusItemPojo> getCorpusList(
			EmotionPolarStatus emotionPolarStatus) {
		String corpuSql = "select * from corpus where polar='"
				+ emotionPolarStatus.toString() + "'";
		ResultSet rs = null;
		List<CorpusItemPojo> corpusList = null;
		try {
			rs = stat.executeQuery(corpuSql);
			corpusList = new LinkedList<CorpusItemPojo>();
			int docId = 0;
			String article = null;
			String fromOrigin = null;
			String polar = null;

			while (rs.next()) {
				docId = rs.getInt("docid");
				article = rs.getString("article");
				fromOrigin = rs.getString("fromOrigin");
				polar = rs.getString("polar");

				corpusList.add(new CorpusItemPojo(docId, article, fromOrigin,
						CorpusItemPojo.getEmotionPolarStatus(polar)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return corpusList;
	}
	
	public List<CorpusItemPojo> getCorpusList(
			EmotionPolarStatus emotionPolarStatus,EmotionPolarStatus emotionPolarStatus2) {
		String corpuSql = "select * from corpus where polar='"
				+ emotionPolarStatus.toString() + "' or polar='"+emotionPolarStatus2.toString()+"'";
		ResultSet rs = null;
		List<CorpusItemPojo> corpusList = null;
		try {
			rs = stat.executeQuery(corpuSql);
			corpusList = new LinkedList<CorpusItemPojo>();
			int docId = 0;
			String article = null;
			String fromOrigin = null;
			String polar = null;

			while (rs.next()) {
				docId = rs.getInt("docid");
				article = rs.getString("article");
				fromOrigin = rs.getString("fromOrigin");
				polar = rs.getString("polar");

				corpusList.add(new CorpusItemPojo(docId, article, fromOrigin,
						CorpusItemPojo.getEmotionPolarStatus(polar)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return corpusList;
	}
	
	public List<CorpusItemPojo> getCorpusList() {
		String corpuSql = "select * from corpus";
		ResultSet rs = null;
		List<CorpusItemPojo> corpusList = null;
		try {
			rs = stat.executeQuery(corpuSql);
			corpusList = new LinkedList<CorpusItemPojo>();
			int docId = 0;
			String article = null;
			String fromOrigin = null;
			String polar = null;

			while (rs.next()) {
				docId = rs.getInt("docid");
				article = rs.getString("article");
				fromOrigin = rs.getString("fromOrigin");
				polar = rs.getString("polar");

				corpusList.add(new CorpusItemPojo(docId, article, fromOrigin,
						CorpusItemPojo.getEmotionPolarStatus(polar)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return corpusList;
	}
	
}
