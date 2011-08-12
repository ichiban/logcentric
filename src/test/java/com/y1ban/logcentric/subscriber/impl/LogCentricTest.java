package com.y1ban.logcentric.subscriber.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.google.gson.GsonBuilder;
import com.y1ban.logcentric.adapters.MapDeserializer;
import com.y1ban.logcentric.publisher.ZMQAppender;

public class LogCentricTest {

	List<LoggingEvent> events = Arrays.asList();

	@Test
	public void serializeDeserialize() {
		final List<byte[]> sentMessages = new ArrayList<byte[]>();

		Context context = ZMQ.context(0);

		Socket publisher = new Socket(context, 0) {

			@Override
			public boolean send(byte[] msg, int flags) {
				sentMessages.add(msg);
				return true;
			}

		};

		Socket subscriber = new Socket(context, 0) {

			@Override
			public byte[] recv(int flags) {
				return sentMessages.remove(0);
			}

		};

		final List<LoggingEvent> loggedEvents = new ArrayList<LoggingEvent>();

		Logger logger = new Logger(null) {

			@Override
			public void callAppenders(LoggingEvent event) {
				loggedEvents.add(event);
			}

		};

		ZMQAppender appender = new ZMQAppender(publisher);

		ZMQLogCollector collector = new ZMQLogCollector(subscriber, logger,
				new GsonBuilder().registerTypeAdapter(Map.class,
						new MapDeserializer()).create(), "true");

		for (LoggingEvent event : events) {
			appender.doAppend(event);
			collector.collectOne();
		}

		assertEquals(events.size(), loggedEvents.size());

		for (int i = 0; i < events.size(); i++) {
			LoggingEvent in = events.get(i);
			LoggingEvent out = loggedEvents.get(i);

			assertEquals(in.getFQNOfLoggerClass(), out.getFQNOfLoggerClass());
			assertEquals(in.getLevel(), out.getLevel());
			assertEquals(in.getLocationInformation(),
					out.getLocationInformation());
			assertEquals(in.getLogger(), out.getLogger());
			assertEquals(in.getLoggerName(), out.getLoggerName());
			assertEquals(in.getMessage(), out.getMessage());
			assertEquals(in.getNDC(), out.getNDC());
			assertEquals(in.getProperties(), out.getProperties());
			assertEquals(in.getThrowableInformation(),
					out.getThrowableInformation());
			assertEquals(in.getTimeStamp(), out.getTimeStamp());
		}
	}
}
