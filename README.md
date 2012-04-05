# LogCentric

LogCentric is a Java library for centralized logging built on top of log4j and ZeroMQ.

**zmq-appender, a successor of LogCentric by @lusis, is now available at https://github.com/lusis/zmq-appender**

Usually web apps are running on several machines and logs are stored in each of them.
Digging into logs here and there are not just a pain but an obstacle to understanding the whole story of the system.
So keeping the whole logs at one place is a nice idea.

LogCentric consists of 2 parts: ZMQAppender and LogCollector.

ZMQAppender is a log4j appender that broadcasts logging events over ZeroMQ.
LogCollector catches the logging events and generates a centralized log in a manner of usual apps that use log4j.

# ZMQAppender

ZMQAppender can be configured in log4j.xml.

	<appender name="logcentric" class="com.y1ban.logcentric.publisher.ZMQAppender">
		<param name="threads" value="1" />
		<param name="endpoint" value="tcp://*:5555" />
		<param name="blocking" value="true" />
	</appender>

# LogCollector

LogCentric jar is an executable jar to run LogCollector.

	java -Dlogcentric.threads=1 -Dlogcentric.endpoint=tcp://localhost:5555 -Dlogcentric.blocking=true -jar logcentric-<VERSION>.jar

# License

Copyright 2012 ICHIBANGASE, Yutaka

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.