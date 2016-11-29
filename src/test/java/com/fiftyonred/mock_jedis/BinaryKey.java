package com.fiftyonred.mock_jedis;

import java.util.Arrays;

/**
 * Created by mmaquiel on 28/11/16.
 */
public class BinaryKey extends RedisKey {

	private byte[] key;

	public BinaryKey(byte[] key) {
		this.key = key;
	}

	public static RedisKey of(byte... byteArray) {
		return new BinaryKey(byteArray);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		BinaryKey binaryKey = (BinaryKey) o;
		return Arrays.equals(key, binaryKey.key);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(key);
	}
}
