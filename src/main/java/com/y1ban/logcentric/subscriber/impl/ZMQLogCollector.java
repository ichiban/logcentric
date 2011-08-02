package com.y1ban.logcentric.subscriber.impl;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import com.google.gson.Gson;
import com.y1ban.logcentric.data.LoggingEventData;
import com.y1ban.logcentric.subscriber.LogCollector;

public class ZMQLogCollector implements LogCollector {

	final Socket socket;
	final Logger logger;
	final Gson gson;
	final boolean blocking;

	public ZMQLogCollector(final Socket socket, final Logger logger,
			final Gson gson, final boolean blocking) {
		this.socket = socket;
		this.logger = logger;
		this.gson = gson;
		this.blocking = blocking;
	}

	@Override
	public void handle() {
		String json;
		LoggingEventData data;
		LoggingEvent event;

		while (true) {
			json = new String(socket.recv(blocking ? 0 : ZMQ.NOBLOCK)).trim();
			data = gson.fromJson(json, LoggingEventData.class);
			event = data.toLoggingEvent();
			logger.callAppenders(event);
		}
	}

}
