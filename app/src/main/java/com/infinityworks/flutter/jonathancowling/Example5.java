package com.infinityworks.flutter.jonathancowling;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;

import java.net.URI;

import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.core.fs.Path;

public class Example5 {

	public static void main(String[] args) throws Exception {
				
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		env.fromSequence(1, 1000)
			.map(new FizzBuzz()) // FizzBuzz could be a lambda, but for testing it was brought out
			.addSink(
				StreamingFileSink
				    .forRowFormat(new Path(Path.CUR_DIR, "out"), new SimpleStringEncoder<String>())
					.build()
			);
		env.execute("File Sink");
	}
}
