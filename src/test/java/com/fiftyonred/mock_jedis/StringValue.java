package com.fiftyonred.mock_jedis;

/**
 * Created by mmaquiel on 28/11/16.
 */
public class StringValue extends RedisValue {
	private String value;

	public StringValue(String value) {
		this.value = value;
	}

	public static RedisValue of(String value) {
		return new StringValue(value);
	}

	@Override
	public String getAsString() {
		return value;
	}

	@Override
	public byte[] getAsBytes() {
		return value.getBytes();
	}
}
