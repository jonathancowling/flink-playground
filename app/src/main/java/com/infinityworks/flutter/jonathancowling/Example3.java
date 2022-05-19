package com.infinityworks.flutter.jonathancowling;

import java.io.InputStream;
import java.util.Properties;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;

public class Example3 {

	public static void main(String[] args) throws Exception {

		Properties prop = new Properties();
		InputStream propertySource = Example3.class.getResourceAsStream("/kinesis.properties");
		if (propertySource != null) {
			prop.load(propertySource);          
		}

		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		env.addSource(new FlinkKinesisConsumer<>("input", new SimpleStringSchema(), prop))
     		.map(String::toUpperCase)
			.print();

		env.execute("Kinesis Uppercase");
	}
}
