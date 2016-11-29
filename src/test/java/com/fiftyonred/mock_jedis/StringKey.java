package com.fiftyonred.mock_jedis;

import java.util.Objects;

/**
 * Created by mmaquiel on 28/11/16.
 */
public class StringKey extends RedisKey {

	private String key;

	public StringKey(String key) {
		this.key = key;
	}

	public static StringKey of(String key) {
		return new StringKey(key);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		StringKey stringKey = (StringKey) o;
		return Objects.equals(key, stringKey.key);
	}

	@Override
	public int hashCode() {
		return Objects.hash(key);
	}
}
