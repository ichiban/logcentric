package com.y1ban.logcentric;

import java.util.Map;

import org.apache.log4j.Logger;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.y1ban.logcentric.adapters.MapDeserializer;
import com.y1ban.logcentric.subscriber.LogCollector;
import com.y1ban.logcentric.subscriber.impl.ZMQLogCollector;

public class LogCentric {
	public static void main(String[] args) {
		final String threads = System.getProperty("logcentric.threads");
		final String endpoint = System.getProperty("logcentric.endpoint");
		final String blocking = System.getProperty("logcentric.blocking");

		final Context context = ZMQ.context(Integer.parseInt(threads));
		final Socket socket = context.socket(ZMQ.SUB);
		socket.connect(endpoint);
		socket.subscribe(new byte[0]);
		final Logger logger = Logger.getLogger(LogCentric.class);
		final Gson gson = new GsonBuilder().registerTypeAdapter(Map.class,
				new MapDeserializer()).create();

		final LogCollector handler = new ZMQLogCollector(socket, logger, gson,
				Boolean.parseBoolean(blocking));
		handler.handle();
	}
}
