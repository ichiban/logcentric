package com.y1ban.logcentric.adapters;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

@SuppressWarnings("rawtypes")
public class MapDeserializer implements JsonDeserializer<Map> {

	@SuppressWarnings("unchecked")
	@Override
	public Map deserialize(JsonElement elem, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		final Map map = new HashMap();
		final JsonObject object = elem.getAsJsonObject();
		for (Entry<String, JsonElement> entry : object.entrySet()) {
			map.put(entry.getKey(), entry.getValue().getAsString());
		}
		return map;
	}

}
