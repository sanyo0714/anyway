package com.sanyo.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author yeshengan
 * @since 2015年9月24日 下午6:06:02
 * @version 1.0.0
 * @TODO
 */
public class MySQLDB {
	private static Logger logger = LoggerFactory.getLogger(MySQLDB.class);

	/**
	 * 定义连接mysql的驱动器.
	 */
	private static String driver = "com.mysql.jdbc.Driver";
	/**
	 * 定义连接mysql的驱动器.
	 */
	private static String url = "jdbc:mysql://127.0.0.1:3306/world?useUnicode=yes&characterEncoding=UTF-8";
	/**
	 * 定义连接mysql的驱动器.
	 */
	private static String user = "root";
	/**
	 * 定义连接mysql的驱动器.
	 */
	private static String password = "1234";
	/**
	 * 对mysql进行初始化
	 */
	static {
		try {
			Class.forName(driver);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			System.exit(-1);
		}
	}

	/**
	 * <p>
	 * 定义连接mysql的驱动器.
	 * <p>
	 * 
	 * @return connection:数据库连接
	 */
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static Connection getConnection(String url, String user,
			String password) {
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static void doMySQL(SQLCallback callback, String sql) {
		Connection conn = MySQLDB.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			if (callback != null) {
				callback.callback(ps);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			IOUtils.closeQuietly(ps);
			IOUtils.closeQuietly(conn);
		}
	}

	public static void updateMySQL(String sql, SQLCallback callback) {
		logger.debug("update mysql sql:" + sql);
		Connection conn = getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			if (callback != null) {
				callback.callback(ps);
			}
			ps.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} finally {
			IOUtils.closeQuietly(ps);
			IOUtils.closeQuietly(conn);
		}
	}

	public static void updateMySQL(Connection conn, String sql,
			SQLCallback callback) {
		logger.debug("update mysql sql:" + sql);
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			if (callback != null) {
				callback.callback(ps);
			}
			ps.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} finally {
			IOUtils.closeQuietly(ps);
		}
	}

	public static void updateSQL(Connection conn, String sql,
			SQLCallback callback) {
		logger.debug("update mysql sql:" + sql);
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			if (callback != null) {
				callback.callback(ps);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} finally {
			IOUtils.closeQuietly(ps);
		}
	}

}
