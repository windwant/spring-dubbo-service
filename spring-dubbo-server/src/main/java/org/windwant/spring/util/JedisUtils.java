package org.windwant.spring.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisUtils {
	private static final Logger logger = LoggerFactory.getLogger(JedisUtils.class);

	//设置锁的lua脚本
	private static final String SETNX_EXPIRE_SCRIPT = "if redis.call('setnx', KEYS[1], KEYS[2]) == 1 then\n"
			+ "return redis.call('expire', KEYS[1], KEYS[3]);\n" + "end\n" + "return nil;";

	private static JedisPool jedisPool;

	public static JedisPool getJedisPool() {
		return jedisPool;
	}

	public static void setJedisPool(JedisPool jedisPool) {
		JedisUtils.jedisPool = jedisPool;
	}

	private static String host;

	private static Integer port;

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		JedisUtils.host = host;
	}

	public static Integer getPort() {
		return port;
	}

	public static void setPort(Integer port) {
		JedisUtils.port = port;
	}

	public static boolean init(String host, Integer port){
		try {
			JedisUtils.host = host;
			JedisUtils.port = port;
			jedisPool = new JedisPool(host, port);
			System.out.println(jedisPool);
			return true;
		}catch (Exception e){}
		return false;
	}

	public static boolean checkExist(String key) {
		if(jedisPool == null) return false;
		try (Jedis jedis = jedisPool.getResource()) {
			logger.info("get redis key record: {}", jedis.get(key));
			return jedis.exists(key);
		}catch (Exception e) {
			logger.warn("get redis key record failed , the message is " + e.getMessage());
		}
		return false;
	}

	public static void set(String key, String value) {
		if(jedisPool == null) return;
		try (Jedis jedis = jedisPool.getResource()) {
			logger.info("set key: {}, value: {}", key, value);
			jedis.set(key, value);
			jedis.expire(key, 20);
		}catch (Exception e) {
			logger.warn("set key failed , the message is " + e.getMessage());
		}
	}

	public static String get(String key) {
		if(jedisPool == null) return null;
		try (Jedis jedis = jedisPool.getResource()) {
			String value = jedis.get(key);
			logger.info("get key: {}, value: {}", key, value);
			return value;
		}catch (Exception e) {
			logger.warn("get key failed , the message is " + e.getMessage());
		}
		return null;
	}

	/**
	 * 设置锁的lua脚本
	 * private static final String SETNX_EXPIRE_SCRIPT = "if redis.call('setnx', KEYS[1], KEYS[2]) == 1 then\n"
	 * "return redis.call('expire', KEYS[1], KEYS[3]);\n" + "end\n" + "return nil;";
	 *
	 * @param key
	 * @return
	 */
	public static boolean setLockKey(String key, String value, Integer seconds) {
		if (jedisPool == null) return false;
		try (Jedis jedis = jedisPool.getResource()) {
			if(jedis.eval(SETNX_EXPIRE_SCRIPT, 3, key, value, String.valueOf(seconds)) != null){
				logger.info("set lock key: {}, value: {}", key, value);
				return true;
			}
		}catch (Exception e) {
			logger.warn("set lock key failed , the message is " + e.getMessage());
		}
		return false;
	}
}