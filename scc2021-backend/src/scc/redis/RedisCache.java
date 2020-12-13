package scc.redis;

import java.time.Duration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisCache {

	private static final String RedisHostname = "scc-redis.redis.cache.windows.net";
	private static final String RedisKey = "MsuRd94p9xWZFx6Exz0UmsMQuP5lRk7+xxm9tay2a4Q=";
	
	private static JedisPool instance;
	
	public synchronized static JedisPool getCachePool() {
		if( instance != null)
			return instance;
		final JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(128);
		poolConfig.setMaxIdle(128);
		poolConfig.setMinIdle(16);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		poolConfig.setTestWhileIdle(true);    poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis()); poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
		poolConfig.setNumTestsPerEvictionRun(3);
		poolConfig.setBlockWhenExhausted(true);
		// connect to redis on localhost:6379
		instance = new JedisPool(poolConfig, "cache"); //new JedisPool(poolConfig, RedisHostname, 6380, 1000, RedisKey, true);
		return instance;
	}
}
