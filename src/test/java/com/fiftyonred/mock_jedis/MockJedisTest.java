package com.fiftyonred.mock_jedis;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.SortingParams;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.*;

import static org.junit.Assert.*;

public class MockJedisTest {
	private MockJedis mockJedis = null;

	@Before
	public void setUp() {
		mockJedis = new MockJedis("test");
	}

	@Test
	public void testSet() {
		assertEquals("OK", mockJedis.set("test", "123"));
	}

	@Test
	public void testGet() {
		mockJedis.set("test", "123");
		assertEquals("123", mockJedis.get("test"));
		assertEquals(null, mockJedis.get("unknown"));
	}

	@Test
	public void testHashes() {
		assertEquals(0L, mockJedis.hlen("test").longValue());
		assertEquals(0L, mockJedis.hdel("test", "name").longValue());
		assertEquals(null, mockJedis.hget("test", "name"));
		mockJedis.hset("test", "name", "value");
		final Set<String> keys = mockJedis.hkeys("test");
		final Map<String, String> entries = mockJedis.hgetAll("test");
		final List<String> vals = mockJedis.hvals("test");
		assertTrue(keys.contains("name"));
		assertEquals(1, keys.size());
		assertEquals(1, entries.size());
		assertEquals("value", entries.get("name"));
		assertTrue(vals.contains("value"));
		assertEquals(1, vals.size());
		assertTrue(mockJedis.hexists("test", "name"));
		assertFalse(mockJedis.hexists("test", "name2"));
		assertEquals(1L, mockJedis.hlen("test").longValue());
		assertEquals("value", mockJedis.hget("test", "name"));
		assertEquals(1L, mockJedis.hdel("test", "name").longValue());
	}

	@Test
	public void testSets() {
		assertFalse(mockJedis.sismember("test", "member 1"));

		assertEquals(2L, (long) mockJedis.sadd("test", "member 1", "member 2"));
		assertEquals(1L, (long) mockJedis.sadd("test", "member 3"));

		// duplicate member 1. should drop
		assertEquals(0L, (long) mockJedis.sadd("test", "member 1"));

		assertEquals(3, mockJedis.smembers("test").size());

		// should remove member 3
		assertEquals(1L, (long) mockJedis.srem("test", "member 3"));

		List<String> sortedMembers = new ArrayList<String>(2);
		sortedMembers.addAll(mockJedis.smembers("test"));
		Collections.sort(sortedMembers);

		assertEquals("member 1", sortedMembers.get(0));
		assertEquals("member 2", sortedMembers.get(1));
	}

	@Test
	public void testHincrBy() {
		mockJedis.hincrBy("test1", "name", 10);
		assertEquals("10", mockJedis.hget("test1", "name"));

		mockJedis.hincrBy("test1", "name", -2);
		assertEquals("8", mockJedis.hget("test1", "name"));

		mockJedis.hset("test1", "name", "5");
		mockJedis.hincrBy("test1", "name", 2);
		assertEquals("7", mockJedis.hget("test1", "name"));

		mockJedis.hincrByFloat("test1", "name", -0.5D);
		assertEquals("6.5", mockJedis.hget("test1", "name"));
	}

	@Test
	public void lpushShoudAddAnElementToTheHeadOfAList() {
		mockJedis.lpush("test", "a");
		mockJedis.lpush("test", "b");
		mockJedis.lpush("test", "c");

		assertEquals(Arrays.asList("c", "b", "a"), mockJedis.lrange("test", 0, -1));
	}

	@Test
	public void rpushShouldAddAnElementAtTheTailOfAList() {
		mockJedis.rpush("test", "a");
		mockJedis.rpush("test", "b");
		mockJedis.rpush("test", "c");

		assertEquals(Arrays.asList("a", "b", "c"), mockJedis.lrange("test", 0, -1));
	}

	@Test
	public void testLRange() {
		mockJedis.lpush("test", "a");
		mockJedis.lpush("test", "b");
		mockJedis.lpush("test", "c");
		mockJedis.lpush("test", "d");

		assertEquals(Arrays.asList("d", "c"), mockJedis.lrange("test", 0, 1));
		assertEquals(Arrays.asList("b", "a"), mockJedis.lrange("test", 2, 5));
		assertEquals(Arrays.asList("b", "a"), mockJedis.lrange("test", -2, -1));
		assertEquals(Arrays.asList("b"), mockJedis.lrange("test", -2, -2));
		assertEquals(0, mockJedis.lrange("test", -7, -6).size());
		assertEquals(0, mockJedis.lrange("test", 6, 7).size());
	}

	@Test
	public void testSort() {
		mockJedis.lpush("test", "a");
		mockJedis.lpush("test", "c");
		mockJedis.lpush("test", "b");
		mockJedis.lpush("test", "d");

		try {
			mockJedis.sort("test");
			fail("Sorting numbers is default");
		} catch (JedisDataException e) {
		}

		assertEquals(Arrays.asList("a", "b", "c", "d"), mockJedis.sort("test", new SortingParams().alpha()));
		assertEquals(Arrays.asList("d", "c", "b", "a"), mockJedis.sort("test", new SortingParams().desc().alpha()));

		mockJedis.sort("test", new SortingParams().alpha(), "newkey");

		assertEquals(Arrays.asList("a", "b", "c", "d"), mockJedis.lrange("newkey", 0, 10));

		mockJedis.sadd("settest", "1", "2", "3", "4", "5", "6");

		assertEquals(Arrays.asList("1", "2", "3", "4", "5", "6"), mockJedis.sort("settest"));
		assertEquals(Arrays.asList("3", "4", "5"), mockJedis.sort("settest", new SortingParams().limit(2, 3)));
		assertEquals(Arrays.asList("4", "3", "2"), mockJedis.sort("settest", new SortingParams().limit(2, 3).desc()));
	}

	@Test(expected = JedisDataException.class)
	public void testInvalidKeyTypeHashToString() {
		mockJedis.hset("test", "test", "1");
		mockJedis.get("test");
	}

	@Test(expected = JedisDataException.class)
	public void testInvalidKeyTypeHashToList() {
		mockJedis.hset("test", "test", "1");
		mockJedis.llen("test");
	}

	@Test(expected = JedisDataException.class)
	public void testInvalidKeyTypeStringToHash() {
		mockJedis.set("test", "test");
		mockJedis.hget("test", "test");
	}

	@Test(expected = JedisDataException.class)
	public void testInvalidKeyTypeStringToList() {
		mockJedis.set("test", "test");
		mockJedis.lpop("test");
	}

	@Test(expected = JedisDataException.class)
	public void testInvalidKeyTypeListToHash() {
		mockJedis.lpush("test", "test");
		mockJedis.hgetAll("test");
	}

	@Test(expected = JedisDataException.class)
	public void testInvalidKeyTypeListToString() {
		mockJedis.lpush("test", "test");
		mockJedis.incr("test");
	}

	@Test
	public void testKeys() {
		mockJedis.set("A1", "value");
		mockJedis.set("A2", "value");
		mockJedis.set("A3", "value");
		mockJedis.hset("B1", "name", "value");
		mockJedis.hset("B2", "name", "value");
		mockJedis.hset("C2C", "name", "value");

		assertEquals(6, mockJedis.keys("*").size());
		assertEquals(1, mockJedis.keys("A1").size());
		assertEquals(3, mockJedis.keys("A*").size());
		assertEquals(2, mockJedis.keys("*1").size());
		assertEquals(3, mockJedis.keys("*2*").size());
		assertEquals(1, mockJedis.keys("C*C").size());

		mockJedis.set("testC2C", "value");
		assertEquals(1, mockJedis.keys("C*C").size());
	}

	@Test
	public void testMultipleDB() {
		assertEquals(0L, mockJedis.dbSize().longValue());
		mockJedis.set("test", "test");
		assertEquals(1L, mockJedis.dbSize().longValue());
		mockJedis.move("test", 5);
		assertEquals(0L, mockJedis.dbSize().longValue());
		mockJedis.select(5);
		assertEquals(1L, mockJedis.dbSize().longValue());
	}
}
