package com.fiftyonred.mock_jedis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.*;

public class MockJedisExpireTest {
	private Jedis mockJedis = null;

	@Before
	public void setUp() {
		mockJedis = new MockJedis("test");
	}

	@Test
	public void testPsetexAndPttl() throws InterruptedException {
		int delay = 200;

		mockJedis.psetex("test", delay, "value");
		assertEquals("value", mockJedis.get("test"));

		assertTrue(mockJedis.pttl("test") > 0);

		Thread.sleep(delay + 1);

		assertTrue(mockJedis.pttl("test") == -1);
	}

	@Test
	public void testSetexAndTtl() throws InterruptedException {
		int delay = 1;

		mockJedis.setex("test", delay, "value");
		assertEquals("value", mockJedis.get("test"));

		assertTrue(mockJedis.ttl("test") > 0);

		Thread.sleep(delay * 1000 + 1);

		assertEquals(-1L, mockJedis.ttl("test").longValue());
	}

	@Test
	public void testExpire() throws InterruptedException {
		int delay = 1;

		mockJedis.set("test", "123");
		mockJedis.expire("test", delay);

		assertTrue(mockJedis.ttl("test") > 0);

		Thread.sleep(delay * 1000 + 1);

		assertNull(mockJedis.get("test"));
		assertEquals(-1L, mockJedis.ttl("test").longValue());
	}

	@Test
	public void testPExpire() throws InterruptedException {
		int delay = 200;

		mockJedis.set("test", "123");
		mockJedis.pexpire("test", delay);

		assertTrue(mockJedis.ttl("test") > 0);

		Thread.sleep(delay + 1);

		assertNull(mockJedis.get("test"));
		assertEquals(-1L, mockJedis.ttl("test").longValue());
	}

	@Test
	public void testExpireAt() throws InterruptedException {
		int delay = 1;

		mockJedis.set("test", "123");
		long startTimeInSec = System.currentTimeMillis() / 1000;
		mockJedis.expireAt("test", startTimeInSec + delay);

		assertTrue(mockJedis.ttl("test") > 0);

		Thread.sleep(delay * 1000 + 1);

		assertNull(mockJedis.get("test"));
		assertTrue(mockJedis.ttl("test") == -1);
	}

	@Test
	public void testPexpireAt() throws InterruptedException {
		int delay = 200;

		mockJedis.set("test", "123");
		long startTimeInMillisec = System.currentTimeMillis();
		mockJedis.pexpireAt("test", startTimeInMillisec + delay);

		assertTrue(mockJedis.ttl("test") > 0);

		Thread.sleep(delay + 1);

		assertNull(mockJedis.get("test"));
		assertTrue(mockJedis.ttl("test") == -1);
	}

	@Test
	public void testRenew() throws InterruptedException {
		int delay = 200;

		mockJedis.psetex("test", delay, "value");

		assertEquals("value", mockJedis.get("test"));
		assertTrue(mockJedis.pttl("test") > 0);

		Thread.sleep(delay / 2 + 1);

		mockJedis.psetex("test", delay, "value");

		assertEquals("value", mockJedis.get("test"));
		assertTrue(mockJedis.pttl("test") > 0);

		Thread.sleep(delay + 1);

		assertNull(mockJedis.get("test"));
		assertTrue(mockJedis.pttl("test") == -1);

	}
}
