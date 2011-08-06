package com.y1ban.logcentric.publisher;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.y1ban.logcentric.data.LoggingEventData;

public class ZMQAppender extends AppenderSkeleton implements Appender {

	private Socket socket;
	private final Gson gson = new Gson();

	private int threads;
	private String endpoint;
	private boolean blocking;

	public ZMQAppender() {
		super();
	}

	@Inject
	public ZMQAppender(final Socket socket) {
		this();
		this.socket = socket;
	}

	@Override
	public void close() {

	}

	/**
	 * ZMQAppender doesn't require layout, just publishes log events over
	 * ZeroMQ. Real logging tasks are up to subscribers.
	 */
	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(final LoggingEvent event) {
		if (isNotReady()) {
			init();
		}

		final LoggingEventData data = new LoggingEventData(event);
		final String json = gson.toJson(data);
		System.out.println(json);
		socket.send(json.getBytes(), blocking ? 0 : ZMQ.NOBLOCK);
	}

	private boolean isNotReady() {
		return null == socket;
	}

	private void init() {
		final Context context = ZMQ.context(threads);
		final Socket socket = context.socket(ZMQ.PUB);
		socket.bind(endpoint);
		this.socket = socket;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(final int threads) {
		this.threads = threads;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(final String endpoint) {
		this.endpoint = endpoint;
	}

	public boolean isBlocking() {
		return blocking;
	}

	public void setBlocking(boolean blocking) {
		this.blocking = blocking;
	}
}
