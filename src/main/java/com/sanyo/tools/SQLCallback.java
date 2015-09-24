package com.sanyo.tools;

import java.sql.Statement;
/**
 * 
 * @author yeshengan
 * @since 2015年9月24日 下午6:06:21
 * @version 1.0.0
 * @TODO
 */
public interface SQLCallback {
    Object callback(Statement ps) throws Exception; 
}
