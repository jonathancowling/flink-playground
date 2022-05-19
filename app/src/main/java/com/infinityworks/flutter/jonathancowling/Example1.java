package com.infinityworks.flutter.jonathancowling;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;

public class Example1 {

	public static void main(String[] args) throws Exception {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
	
		DataStreamSource<Long> source = env.fromSequence(1, 1000);

		source.addSink(new PrintSinkFunction<>());

		env.execute("Count");
	}
}
