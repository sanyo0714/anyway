package com.sanyo.launch;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sanyo.tools.MySQLDB;
import com.sanyo.tools.SQLCallback;
import com.sanyo.tools.SQLiteCRUD;
import com.sanyo.tools.SQLiteConn;
import com.sanyo.tools.StringUtils;

public class LaunchInsertSqliteDB {
	
	
	
//	CREATE TABLE geolocation (
//		    locId INTEGER PRIMARY KEY,
//		    country TEXT,
//		    province TEXT,
//		    city TEXT,
//		    town TEXT,
//		    address TEXT,
//		    operator TEXT
//		);
//	
//	
//	CREATE TABLE geoblocks (
//		    startIpNum NUMERIC UNIQUE,
//		    endIpNum NUMERIC UNIQUE,
//		    locId INTEGER REFERENCES geolocation(locID)
//		, sidx NUMERIC, eidx NUMERIC);
//
//		CREATE INDEX geosidx ON geoblocks(sidx);



	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		long l = StringUtils.ipToLong("175.167.136.217");
		System.out.println(l);
		l = StringUtils.ipToLong("10.18.33.4");
		System.out.println(l);
		System.exit(0);

		if ("".equals(null)) {
			System.out.println(1);
		}

		SQLiteConn sc = new SQLiteConn();
		
		final SQLiteCRUD scrud = new SQLiteCRUD(sc.getConnection());

		MySQLDB.doMySQL(new SQLCallback() {

			@Override
			public Object callback(Statement ps) throws Exception {
				// TODO Auto-generated method stub
				PreparedStatement pps = (PreparedStatement) ps;
				ResultSet rs = pps.executeQuery();
				int count = 0;
				List<String> sqls = new ArrayList<String>();
				while (rs.next()) {
					int privCount = rs.getInt("locId");
					String country = rs.getString("country");
					String province = rs.getString("province");
					String city = rs.getString("city");
					String town = rs.getString("town");
					String address = rs.getString("address");
					String operator = rs.getString("operator");

					Object[] params = { privCount, country, province, city,
							town, address, operator };

					String sql = "insert into " + "geolocation" + " values('";
					for (int i = 0; i < params.length; i++) {
						if (i == (params.length - 1)) {
							sql += (params[i] + "');");
						} else {
							sql += (params[i] + "', '");
						}
					}
					System.out.println(sql);
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
				return null;
			}
		}, "select * from geolocation");

		MySQLDB.doMySQL(new SQLCallback() {

			@Override
			public Object callback(Statement ps) throws Exception {
				// TODO Auto-generated method stub
				PreparedStatement pps = (PreparedStatement) ps;
				ResultSet rs = pps.executeQuery();
				int count = 0;
				List<String> sqls = new ArrayList<String>();
				while (rs.next()) {
					BigDecimal startIpNum = rs.getBigDecimal("startIpNum");
					BigDecimal endIpNum = rs.getBigDecimal("endIpNum");
					int locId = rs.getInt("locId");
					BigDecimal sidx = rs.getBigDecimal("sidx");
					BigDecimal eidx = rs.getBigDecimal("eidx");

					Object[] params = { startIpNum, endIpNum, locId, sidx, eidx };

					String sql = "insert into " + "geoblocks" + " values('";
					for (int i = 0; i < params.length; i++) {
						if (i == (params.length - 1)) {
							sql += (params[i] + "');");
						} else {
							sql += (params[i] + "', '");
						}
					}
					System.out.println(sql);
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

				return null;
			}
		}, "select * from geoblocks");
		
		
	}

}
