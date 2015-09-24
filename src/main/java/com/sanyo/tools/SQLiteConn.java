package com.sanyo.tools;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteConn implements Serializable {
	private static final long serialVersionUID = 102400L;

	/**
	 * 与SQLite嵌入式数据库建立连接
	 * 
	 * @return Connection
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception {
		Connection connection = null;
		try {
			Class.forName("org.sqlite.JDBC", true, this.getClass()
					.getClassLoader());
			connection = DriverManager
					.getConnection("jdbc:sqlite:D:/tools/demo_lib/sqlite/geo1.db");
			connection.setAutoCommit(false);
		} catch (Exception e) {
			throw new Exception("" + e.getLocalizedMessage(), new Throwable(
					"可能由于数据库文件受到非法修改或删除。"));
		}
		return connection;
	}
}
