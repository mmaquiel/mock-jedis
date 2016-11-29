package com.fiftyonred.mock_jedis;

/**
 * Created by mmaquiel on 28/11/16.
 */
public class BinaryValue extends RedisValue {

	private byte[] byteArray;

	public BinaryValue(byte[] byteArray) {
		this.byteArray = byteArray;
	}

	@Override
	public String getAsString() {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] getAsBytes() {
		return byteArray;
	}

	public static RedisValue of(byte[] value) {
		return new BinaryValue(value);
	}
}
