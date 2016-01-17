package com.zel.classfy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
	public Connection dbConn = null;
	public String serverIP;
	public String userName;
	public String userPwd;
    public String port;
    public String dbName;
    
	public DBUtils(String serverIp,String port,String dbName, String userName, String userPwd) {
		this.serverIP = serverIp;
		this.userName = userName;
		this.userPwd = userPwd;
		
		this.port=port;
		this.dbName=dbName;
        //初始化打开db connection
		openConnection(serverIp, port,dbName,userName, userPwd);
	}

	public boolean openConnection(String serverIP,String port,String dbName, String userName,
			String userPwd) {
		String driverName = "com.mysql.jdbc.Driver"; // sqlserver2005
		// jdbc驱动
		//entityevalplatform
		String dbURL ="jdbc:mysql://"+serverIP+":"+port+"/"+dbName+"?useUnicode=true&zeroDateTimeBehavior=convertToNull&characterEncoding=utf-8";
//		String dbURL = "jdbc:jtds:sqlserver://" + serverIP + ":1433/LOS"; // 服务器地址
		try {
			Class.forName(driverName);
			dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Statement getStat() {
		try {
			return dbConn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PreparedStatement getPreparedStat(String sql) {
		try {
		   return dbConn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean closeConnection() {
		try {
			if (dbConn.isClosed()) {
				return true;
			} else {
				dbConn.close();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String args[]) throws Exception {
		// openConnection("192.168.1.120", "sa", "123456789");

		// Statement stat=dbConn.createStatement();
		// String sql="select a108 from tb_A108";
		// ResultSet rs=stat.executeQuery(sql);
		// String sql = "call del_repeat_records_zel 'zel'";
		// CallableStatement cmd = null;
		// cmd = dbConn.prepareCall(sql);
	}

}
