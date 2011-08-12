package com.y1ban.logcentric.subscriber.impl;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.y1ban.logcentric.data.LoggingEventData;
import com.y1ban.logcentric.subscriber.LogCollector;

public class ZMQLogCollector implements LogCollector {

	private final Socket socket;
	private final Logger logger;
	private final Gson gson;
	private final boolean blocking;

	@Inject
	public ZMQLogCollector(final Socket socket, final Logger logger,
			final Gson gson, @Named("logcentric.blocking") final String blocking) {
		this.socket = socket;
		this.logger = logger;
		this.gson = gson;
		this.blocking = Boolean.parseBoolean(blocking);
	}

	@Override
	public void run() {
		while (true) {
			collectOne();
		}
	}

	protected void collectOne() {
		final byte[] input = socket.recv(blocking ? 0 : ZMQ.NOBLOCK);
		final String json = new String(input);
		final LoggingEventData data = gson.fromJson(json,
				LoggingEventData.class);
		final LoggingEvent event = data.toLoggingEvent();
		logger.callAppenders(event);
	}

}
