package com.sanyo.launch;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

/**
 * @author yeshengan
 * @since 2016年3月28日 下午5:25:33
 * @version 1.0.0
 * @TODO
 */
public class LaunchMongoWorker {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String host = "220.181.156.245";
		int port = 27017;
		Mongo mongo = new MongoClient(new ServerAddress(host, port),
				getConfOptions());
		
		
		

	}

	private static MongoClientOptions getConfOptions() {
		return new MongoClientOptions.Builder().socketKeepAlive(true) // 是否保持长链接
				.connectTimeout(5000) // 链接超时时间
				.socketTimeout(5000) // read数据超时时间
				.readPreference(ReadPreference.primary()) // 最近优先策略
				.connectionsPerHost(30) // 每个地址最大请求数
				.maxWaitTime(1000 * 60 * 2) // 长链接的最大等待时间
				.threadsAllowedToBlockForConnectionMultiplier(50) // 一个socket最大的等待请求数
				.writeConcern(WriteConcern.MAJORITY).build();
	}
}
