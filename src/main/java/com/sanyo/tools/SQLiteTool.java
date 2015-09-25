package com.sanyo.tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * @author yeshengan
 * @since 2015年3月26日 上午11:36:26
 * @version 1.0.0
 * @TODO
 */
public class SQLiteTool {

	private static Logger logger = Logger.getLogger("error");

	private Connection connection;

	public SQLiteTool(Connection connection) {
		this.connection = connection;
	}

	/**
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public HashMap<String, String> selectVector(String sql) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		HashMap<String, String> backMap = new HashMap<String, String>();
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String country = rs.getString("country");
				String province = rs.getString("province");
				String prov = "";
				if (!country.equals("中国")) {
					prov = province.equalsIgnoreCase("未知") ? ""
							: ("-" + province);
				} else {
					prov = "-" + province;
				}
				String city = rs.getString("city");
				String operator = rs.getString("operator");
				backMap.put("country", country + prov);
				backMap.put("city", city);
				backMap.put("operator", operator);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			rs.close();
			stmt.close();
			// connection.close();
		}
		return backMap;
	}

}
