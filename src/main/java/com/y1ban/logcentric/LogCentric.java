package com.y1ban.logcentric;

import java.util.Map;

import org.apache.log4j.Logger;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.y1ban.logcentric.adapters.MapDeserializer;
import com.y1ban.logcentric.subscriber.LogCollector;
import com.y1ban.logcentric.subscriber.impl.ZMQLogCollector;

public class LogCentric extends AbstractModule {
	public static void main(final String[] args) {
		final Injector injector = Guice.createInjector(new LogCentric());
		final LogCollector collector = injector.getInstance(LogCollector.class);

		collector.run();
	}

	@Override
	protected void configure() {
		Names.bindProperties(binder(), System.getProperties());
		
		bind(LogCollector.class).to(ZMQLogCollector.class);
	}

	@Provides
	@Singleton
	protected Context context(@Named("logcentric.threads") final String threads) {
		return ZMQ.context(Integer.parseInt(threads));
	}

	@Provides
	@Singleton
	protected Socket socket(final Context context,
			@Named("logcentric.endpoint") final String endpoint) {
		final Socket socket = context.socket(ZMQ.SUB);
		socket.connect(endpoint);
		socket.subscribe(new byte[0]);
		return socket;
	}

	@Provides
	protected Logger logger() {
		return Logger.getLogger(LogCentric.class);
	}

	@Provides
	@Singleton
	protected Gson gson() {
		return new GsonBuilder().registerTypeAdapter(Map.class,
				new MapDeserializer()).create();
	}
}
