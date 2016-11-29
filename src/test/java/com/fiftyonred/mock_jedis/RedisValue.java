package com.fiftyonred.mock_jedis;

/**
 * Created by mmaquiel on 28/11/16.
 */
public abstract class RedisValue {

	private boolean asBytes;

	public abstract String getAsString();

	public abstract byte[] getAsBytes();
}
