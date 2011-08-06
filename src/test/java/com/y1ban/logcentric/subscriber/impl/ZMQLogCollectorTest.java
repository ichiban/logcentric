package com.y1ban.logcentric.subscriber.impl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.lang.reflect.Type;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;
import org.zeromq.ZMQ.Socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.y1ban.logcentric.data.LoggingEventData;

public class ZMQLogCollectorTest {

	@Test
	public void collectWithBlocking() {
		Socket socket = createMock(Socket.class);
		Logger logger = createMock(Logger.class);
		final LoggingEventData data = createMock(LoggingEventData.class);
		LoggingEvent event = createMock(LoggingEvent.class);

		Gson gson = new GsonBuilder().registerTypeAdapter(
				LoggingEventData.class,
				new JsonDeserializer<LoggingEventData>() {

					@Override
					public LoggingEventData deserialize(JsonElement json,
							Type typeOfT, JsonDeserializationContext context)
							throws JsonParseException {
						return data;
					}
				}).create();

		String json = "{\"json\":\"dummy\"}";

		expect(socket.recv(eq(0))).andReturn(json.getBytes());
		expect(data.toLoggingEvent()).andReturn(event);
		logger.callAppenders(eq(event));
		expectLastCall();

		replay(socket, logger, data);

		ZMQLogCollector collector = new ZMQLogCollector(socket, logger, gson,
				Boolean.TRUE.toString());
		collector.collectOne();

		verify(socket, logger, data);
	}
}
