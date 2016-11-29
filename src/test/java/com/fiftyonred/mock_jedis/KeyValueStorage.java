package com.fiftyonred.mock_jedis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mmaquiel on 28/11/16.
 */
public class KeyValueStorage {

	private Map<RedisKey, RedisValue> keyValues;

	public KeyValueStorage() {
		keyValues = new ConcurrentHashMap<>();
	}

	public void set(RedisKey key, RedisValue value) {
		keyValues.put(key, value);
	}

	public RedisValue get(RedisKey key) {
		return keyValues.get(key);
	}
}
