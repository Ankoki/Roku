package com.ankoki.roku.test;

import com.ankoki.roku.web.json.JSON;
import com.ankoki.roku.web.json.JSONSerializable;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadWriteTest {

	static {
		JSONSerializable.register(SerializableTest.class);
	}

	// READ

	@Test
	public void empty() {
		JSON json = new JSON("{}");
		assert json.isEmpty() : json.size();
	}

	@Test
	public void basic() {
		JSON json = new JSON("{\"key\": \"value\"}");
		assert json.size() == 1 : json.size();
		assert json.get("key").equals("value") : json.get("key");
	}

	@Test
	public void multiple() {
		JSON json = new JSON("{\"first\":\"first value is this!\", \"second\": \"second value\"}");
		assert json.size() == 2 : json.size();
		assert json.get("first").equals("first value is this!") : json.get("first");
		assert json.get("second").equals("second value") : json.get("second value");
	}

	@Test
	public void array() {
		JSON json = new JSON("{\"string\": \"string value\", \"my-array\": [\"array test value\", \"second array value\"]}");
		assert json.size() == 2 : json.size();
		assert json.get("string").equals("string value") : json.get("string");
		assert ((List<?>) json.get("my-array")).size() == 2 : ((List<?>) json.get("my-array")).size();
		assert ((List<?>) json.get("my-array")).get(0).equals("array test value") : ((List<?>) json.get("my-array")).get(0);
		assert ((List<?>) json.get("my-array")).get(1).equals("second array value") : ((List<?>) json.get("my-array")).get(1);
	}

	@Test
	public void map() {
		JSON json = new JSON("{\"map\":{\"inner-key\":\"inner value\"}}");
		assert json.size() == 1 : json.size();
		assert ((Map<String, Object>) json.get("map")).size() == 1 : ((Map<String, Object>) json.get("map")).size();
		assert ((Map<String, Object>) json.get("map")).get("inner-key").equals("inner value") : ((Map<String, Object>) json.get("map")).get("inner-key");
	}

	@Test
	public void numbers() {
		JSON json = new JSON("{\"one\":1, \"negative\":-53}");
		assert json.size() == 2 : json.size();
		assert json.get("one").equals(1) : json.get("one");
		assert json.get("negative").equals(-53) : json.get("negative");
	}

	@Test
	public void nullability() {
		JSON json = new JSON("{\"null\":null}");
		assert json.size() == 1 : json.size();
		assert json.get("null") == null : json.get("null");
	}

	@Test
	public void booleans() {
		JSON json = new JSON("{\"true\":true,\"false\":false}");
		assert json.size() == 2 : json.size();
		assert json.get("true").equals(true) : json.get("true");
		assert json.get("false").equals(false) : json.get("false");
	}

	@Test
	public void serializable() {
		JSON json = new JSON("{\"serializable\":{\"-x\":\"com.ankoki.roku.test.ReadWriteTest$SerializableTest\",\"one\":1,\"two\":2,\"five\":5}}");
		assert json.size() == 1 : json.size();
		assert json.get("serializable") instanceof SerializableTest : json.get("serializable").getClass().getName();
		assert ((SerializableTest) json.get("serializable")).one == 1 : ((SerializableTest) json.get("serializable")).one;
		assert ((SerializableTest) json.get("serializable")).two == 2 : ((SerializableTest) json.get("serializable")).two;
		assert ((SerializableTest) json.get("serializable")).five == 5 : ((SerializableTest) json.get("serializable")).five;
	}

	// TODO WRITE

	// SERIALIZABLE TEST CLASS

	public static class SerializableTest extends JSONSerializable {

		public static SerializableTest deserialize(Map<String, Object> map) {
			return new SerializableTest((int) map.get("one"), (int) map.get("two"), (int) map.get("five"));
		}

		private final int one;
		private final int two;
		private final int five;

		public SerializableTest(int one, int two, int five) {
			this.one = one;
			this.two = two;
			this.five = five;
		}

		@Override
		public Map<String, Object> serialize() {
			Map<String, Object> map = new HashMap<>();
			map.put("one", one);
			map.put("two", two);
			map.put("five", five);
			return map;
		}

	}

}
