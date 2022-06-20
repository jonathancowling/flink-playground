package com.infinityworks.flutter.jonathancowling;

import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.OnCheckpointRollingPolicy;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;

public class Example4 {

	public static void main(String[] args) throws Exception {

		Properties prop = new Properties();
		InputStream propertySource = Example3.class.getResourceAsStream("/kinesis.properties");
		if (propertySource != null) {
			prop.load(propertySource);
		}

		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		env.enableCheckpointing(Duration.ofSeconds(1).toMillis());

		env.addSource(new FlinkKinesisConsumer<>("input", new SimpleStringSchema(), prop))
     		.map(String::toUpperCase)
			.addSink(StreamingFileSink.forRowFormat(new Path("s3://output/output/"), new SimpleStringEncoder<String>())
			.withBucketAssigner(new DateTimeBucketAssigner<>("yyyy-MM-dd--HH"))
			.withRollingPolicy(OnCheckpointRollingPolicy.build()) // recommended rolling policy from flink docs
			.build());

		env.execute("Kinesis Uppercase S3");
	}
}
