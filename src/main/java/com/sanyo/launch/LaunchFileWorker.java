package com.sanyo.launch;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.sanyo.tools.SQLiteCRUD;
import com.sanyo.tools.SQLiteConn;

public class LaunchFileWorker {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		
		String path = LaunchFileWorker.class.getClassLoader().getResource("")
				.getPath()
				+ "ip_standard.txt";

		ArrayList<String> lineList = (ArrayList<String>) FileUtils
				.readLines(new File(path));
		Set<String> allLocation = new HashSet<String>();
		Map<String, String> allblocks = new HashMap<String, String>();
		for (String line : lineList) {
			String[] strs = line.split("\t");
			long s = Long.valueOf(strs[0]);
			long e = Long.valueOf(strs[1]);
			String key = String.format("%d\t%d\t%d\t%d", s, e, s - s % 65536, e
					- e % 65536);
			String value = String.format("%s\t%s\t%s\t%s\t%s\t%s", strs[2],
					strs[3], strs[4], strs[5], strs[6], strs[7]);
			allLocation.add(value);
			allblocks.put(key, value);
		}
		Iterator<String> iterator = allLocation.iterator();
		Map<String, Integer> geolocation = new HashMap<String, Integer>();
		int i = 1;
		while (iterator.hasNext()) {
			String value = iterator.next();
			geolocation.put(value, i);
			i++;
		}

		Map<String, Integer> geoblocks = new HashMap<String, Integer>();
		for (String key : allblocks.keySet()) {
			geoblocks.put(key, geolocation.get(allblocks.get(key)));
		}

		/**
		 * 打印输出
		 * 
		 */
		SQLiteConn sc = new SQLiteConn();
		Connection connection = sc.getConnection();
		Statement stat = connection.createStatement();
		stat.executeUpdate("CREATE TABLE geolocation (locId INTEGER PRIMARY KEY,country TEXT,province TEXT,city TEXT,town TEXT,address TEXT,operator TEXT);");
		stat.executeUpdate("CREATE TABLE geoblocks (startIpNum NUMERIC UNIQUE,endIpNum NUMERIC UNIQUE,locId INTEGER REFERENCES geolocation(locID), sidx NUMERIC, eidx NUMERIC);");
		stat.executeUpdate("CREATE INDEX geosidx ON geoblocks(sidx);");
		stat.executeUpdate("CREATE INDEX geoeidx ON geoblocks(eidx);");
		stat.close();
		connection.commit();

		SQLiteCRUD scrud = new SQLiteCRUD(sc.getConnection());
		int count = 0;
		List<String> sqls = new ArrayList<String>();
		for (Entry<String, Integer> entry : geolocation.entrySet()) {
			String line = String.format("%d\t%s", entry.getValue(),
					entry.getKey());
			String[] strs = line.split("\t");
			if (strs.length < 7)
				continue;

			String sql = "insert into " + "geolocation" + " values(" + strs[0]
					+ ",'" + strs[1] + "','" + strs[2] + "','" + strs[3] + "','" + strs[4] + "','" + strs[5] + "','" + strs[6] + "')";
			sqls.add(sql);
			count++;
			if (count % 500 == 0) {
				scrud.insertBatch(sqls);
				sqls = new ArrayList<String>();
			}

		}

		if (count % 500 != 0) {
			scrud.insertBatch(sqls);
		}

		count = 0;
		sqls = new ArrayList<String>();
		for (Entry<String, Integer> entry : geoblocks.entrySet()) {
			String line = String.format("%d\t%s", entry.getValue(),
					entry.getKey());
			String[] strs = line.split("\t");
			if (strs.length < 5)
				continue;

			String sql = "insert into " + "geoblocks" + " values("+strs[1]+","+strs[2]+","+strs[0]+","+strs[3]+","+strs[4]+")";
			sqls.add(sql);
			count++;
			if (count % 500 == 0) {
				scrud.insertBatch(sqls);
				sqls = new ArrayList<String>();
			}
		}

		if (count % 500 != 0) {
			scrud.insertBatch(sqls);
		}
		
		
		
		stat = connection.createStatement();
		stat.executeUpdate("UPDATE geoblocks  SET `sidx` ='16777215' WHERE `sidx` != `eidx`;");
		stat.close();
		connection.commit();
		connection.close();
		
		System.exit(0);
		

	}

}
