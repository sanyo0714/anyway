package com.sanyo.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author yeshengan
 * @since 2015年9月24日 下午6:06:36
 * @version 1.0.0
 * @TODO
 */
public class IOUtils extends org.apache.commons.io.IOUtils {
    private static Logger logger = LoggerFactory.getLogger(IOUtils.class);
    public static void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    public static void closeQuietly(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    public static void closeQuietly(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
