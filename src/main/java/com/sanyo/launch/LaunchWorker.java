package com.sanyo.launch;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.sanyo.tools.MySQLDB;
import com.sanyo.tools.SQLCallback;

public class LaunchWorker {

	
	
	//第一步 建表
	// DROP TABLE IF EXISTS `ip_standard`;
	//
	// CREATE TABLE `ip_standard` (
	// `ip_start` decimal(10,0) NOT NULL,
	// `ip_end` decimal(10,0) NOT NULL,
	// `country` varchar(50) DEFAULT NULL,
	// `province` varchar(50) DEFAULT NULL,
	// `city` varchar(50) DEFAULT NULL,
	// `town` varchar(50) DEFAULT NULL,
	// `address` varchar(100) DEFAULT NULL,
	// `operator` varchar(20) DEFAULT NULL,
	// `city_cn` varchar(30) DEFAULT NULL,
	// `province_cn` varchar(30) DEFAULT NULL,
	// PRIMARY KEY (`ip_start`,`ip_end`)
	// ) ENGINE=MyISAM DEFAULT CHARSET=utf8;

	// /*Table structure for table `geoblocks` */
	//
	// DROP TABLE IF EXISTS `geoblocks`;
	//
	// CREATE TABLE `geoblocks` (
	// `startIpNum` decimal(10,0) DEFAULT NULL,
	// `endIpNum` decimal(10,0) DEFAULT NULL,
	// `locId` int(11) DEFAULT NULL,
	// `sidx` decimal(20,0) DEFAULT NULL,
	// `eidx` decimal(20,0) DEFAULT NULL,
	// UNIQUE KEY `startIpNum` (`startIpNum`),
	// UNIQUE KEY `endIpNum` (`endIpNum`),
	// KEY `geoidx` (`sidx`)
	// ) ENGINE=MyISAM DEFAULT CHARSET=utf8;
	//
	// /*Table structure for table `geolocation` */
	//
	// DROP TABLE IF EXISTS `geolocation`;
	//
	// CREATE TABLE `geolocation` (
	// `locId` int(11) NOT NULL,
	// `country` text,
	// `province` text,
	// `city` text,
	// `town` text,
	// `address` text,
	// `operator` text,
	// PRIMARY KEY (`locId`)
	// ) ENGINE=MyISAM DEFAULT CHARSET=utf8;
	
	
	
	// 第二步执行 launch worker
	
	// 第三步 执行INSERT INTO geolocation(country,province,city,town,address,operator,locId) SELECT country,province,city,town,address,operator,@rownum:=@rownum+1 AS rownum FROM (SELECT @rownum:=0) r,(SELECT country,province,city,town,address,operator FROM ip_standard GROUP BY country,province,city,town,address,operator) a;
	
	// 第四步 修改 geoblocks  locId text 
	
	// 第五步 执行INSERT INTO geoblocks( startIpNum, endIpNum, sidx ,eidx ,locId) SELECT ip_start,ip_end,(ip_start - (ip_start % 65536)) AS s,(ip_end - (ip_end % 65536)) AS e,CONCAT(country,province,city,town,address,operator) AS ro FROM ip_standard ORDER BY ip_start;
	
	// 第六步 执行UPDATE geoblocks t SET t.locId = (SELECT locId FROM geolocation WHERE CONCAT(country,province,city,town,address,operator) = t.locId);
	
	// 第七步 修改 geoblocks  locId int 11位
	
	
	/*
	 * 检测sql
	 * SELECT g.*,i.ip_start FROM geoblocks g ,ip_standard i WHERE g.locId = CONCAT(i.country,i.province,i.city,i.town,i.address,i.operator);
	 * SELECT a.locId,b.locId FROM geoblocks a LEFT JOIN geolocation b ON (a.locId = CONCAT(b.country,b.province,b.city,b.town,b.address,b.operator));
	 */
	

	private static final String INSERT_SQL = "insert into ip_standard(ip_start,ip_end,country,province,city,town,address,operator) values(?, ?, ?, ?, ?,?,?, ?)";

	public static void main(String[] args) throws Exception {
		String path = LaunchWorker.class.getClassLoader().getResource("")
				.getPath()
				+ "ip_standard.txt";

		@SuppressWarnings("unchecked")
		ArrayList<String> lineList = (ArrayList<String>) FileUtils
				.readLines(new File(path));

		LaunchWorker.persistData(lineList);

	}

	/**
	 * 将数据入库
	 * 
	 * @param output
	 * @param conf
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public static Boolean persistData(final ArrayList<String> lineList)
			throws IOException, SQLException {
		boolean persistDataFlag = false;

		Connection connection = MySQLDB.getConnection();

		try {
			connection.setAutoCommit(false);

			MySQLDB.updateSQL(connection, INSERT_SQL, new SQLCallback() {
				@Override
				public Object callback(Statement ps) throws Exception {

					PreparedStatement pst = (PreparedStatement) ps;
					int i = 0;
					for (String sLine : lineList) {
						if (sLine != null) {
							String[] strs = sLine.split("\t");
							if (strs.length < 8)
								continue;

							long ipStart = 0;
							long ipEnd = 0;
							String start = strs[0];
							String end = strs[1];
							if (start == null) {
							} else {
								try {
									ipStart = Long.parseLong(start);
								} catch (NumberFormatException nfe) {
								}
							}
							if (end == null) {
							} else {
								try {
									ipEnd = Long.parseLong(end);
								} catch (NumberFormatException nfe) {
								}
							}

							// insert
							pst.setLong(1, ipStart);
							pst.setLong(2, ipEnd);
							pst.setString(3, strs[2]);
							pst.setString(4, strs[3]);
							pst.setString(5, strs[4]);
							pst.setString(6, strs[5]);
							pst.setString(7, strs[6]);
							pst.setString(8, strs[7]);
							pst.addBatch();

							i++;
							if (i % 5000 == 0) {
								pst.executeBatch();
							}
						}
					}
					pst.executeBatch();
					return null;
				}
			});

			connection.commit();
			persistDataFlag = true;
		} finally {
			connection.close();
		}
		return persistDataFlag;
	}
}
