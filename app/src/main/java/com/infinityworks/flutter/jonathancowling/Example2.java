package com.infinityworks.flutter.jonathancowling;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Example2 {

	public static void main(String[] args) throws Exception {

		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		env.fromSequence(1, 1000)
			.map(new FizzBuzz()) // FizzBuzz could be a lambda, but for testing it was brought out
			.print();

		env.execute("Fizz Buzz");
	}
}
