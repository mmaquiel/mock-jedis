package com.fiftyonred.mock_jedis;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

/**
 * Created by mmaquiel on 28/11/16.
 */
public class KeyValueStorageTest {

	public static final byte[] ONE_TWO_THREE_IN_BYTES = { 0x0, 0x1, 0x2 };

	@Test
	public void storingAStringWithAStringKeyShouldSaveItCorrectly() throws Exception {
		KeyValueStorage keyValueStorage = new KeyValueStorage();

		keyValueStorage.set(StringKey.of("key.name"), StringValue.of("a_value"));

		then(keyValueStorage.get(StringKey.of("key.name")).getAsString()).isEqualTo("a_value");
	}

	@Test
	public void storingAByteArrayWithAStringKeyShouldSaveItCorrectly() throws Exception {
		KeyValueStorage keyValueStorage = new KeyValueStorage();

		keyValueStorage.set(StringKey.of("key.name"), BinaryValue.of(ONE_TWO_THREE_IN_BYTES));

		then(keyValueStorage.get(StringKey.of("key.name")).getAsBytes()).contains(0x0, 0x1, 0x2);
	}

	@Test
	public void storingAStringWithABinaryKeyShouldSaveItCorrectly() throws Exception {
		KeyValueStorage keyValueStorage = new KeyValueStorage();
		RedisKey binaryKey = BinaryKey.of(ONE_TWO_THREE_IN_BYTES);

		keyValueStorage.set(binaryKey, StringValue.of("a_value"));

		then(keyValueStorage.get(BinaryKey.of(ONE_TWO_THREE_IN_BYTES)).getAsString()).isEqualTo("a_value");
	}

	@Test
	public void storingABinaryValueWithABinaryKeyShouldSaveItCorrectly() throws Exception {
		KeyValueStorage keyValueStorage = new KeyValueStorage();
		RedisKey binaryKey = BinaryKey.of(ONE_TWO_THREE_IN_BYTES);

		keyValueStorage.set(binaryKey, BinaryValue.of(new byte[]{0x0, 0x1}));

		then(keyValueStorage.get(BinaryKey.of(ONE_TWO_THREE_IN_BYTES)).getAsBytes()).contains(0x0, 0x1);
	}
}
