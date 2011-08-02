# LogCentric

LogCentric is a Java library for centralized logging built on top of log4j and ZeroMQ.

Usually web apps are running on several machines and logs are stored in each of them.
Digging into logs here and there are not just a pain but an obstacle to understanding the whole story of the system.
So keeping the whole logs at one place is a nice idea.

LogCentric consists of 2 parts: ZMQAppender and LogCollector.

ZMQAppender is a log4j appender that broadcasts logging events over ZeroMQ.
LogCollector catches the logging events and generates a centralized log in a manner of usual apps that use log4j.

# ZMQAppender

ZMQAppender can be configured in log4j.xml.

example)

	<appender name="logcentric" class="com.y1ban.logcentric.publisher.ZMQAppender">
		<param name="threads" value="1" />
		<param name="endpoint" value="tcp://*:5556" />
		<param name="blocking" value="true" />
	</appender>

# LogCollector

LogCentric jar is an executable jar to run LogCollector.

	java -Dlogcentric.threads=1 -Dlogcentric.endpoint=tcp://localhost:5556 -Dlogcentric.blocking=true -jar logcentric-<VERSION>.jar