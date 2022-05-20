package com.infinityworks.flutter.jonathancowling;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.ConfigOptions;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;
import org.apache.hadoop.fs.s3a.AWSCredentialProviderList;
import org.apache.hadoop.fs.s3a.S3AFileSystem;

public class Example4 {

	public static void main(String[] args) throws Exception {

		Properties prop = new Properties();
		InputStream propertySource = Example3.class.getResourceAsStream("/kinesis.properties");
		if (propertySource != null) {
			prop.load(propertySource);          
		}

		// final StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration().set(ConfigOptions.key("s3.endpoint").stringType().noDefaultValue(), "http://localhost:4566"));
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		env.addSource(new FlinkKinesisConsumer<>("input", new SimpleStringSchema(), prop))
     		.map(String::toUpperCase)
			.addSink(StreamingFileSink.forRowFormat(new Path("s3://output/output"), (String element, OutputStream stream) -> {
				PrintStream out = new PrintStream(stream);
				out.println(element);
			})
			.build());

		env.execute("Kinesis Uppercase S3");
	}
}
