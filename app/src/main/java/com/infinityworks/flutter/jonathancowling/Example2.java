package com.infinityworks.flutter.jonathancowling;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Example2 {

	public static void main(String[] args) throws Exception {

		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		env.fromSequence(1, 1000)
			.map(number -> {
				boolean fizz = number % 3 == 0;
				boolean buzz = number % 5 == 0;

				StringBuilder b = new StringBuilder();

				// normally fizzbuzz doesn't want the number printed in all cases,
				// but, as this is not necessarily ordered, it's useful here
				b.append("(" + number.toString() + "): " );
				if (fizz) {
					b.append("fizz");
				}
				if (buzz) {
					b.append("buzz");
				}
				if (!fizz && !buzz) {
					b.append(number.toString());
				}

				return b.toString();
			})
			.print();

		env.execute("Fizz Buzz");
	}
}
