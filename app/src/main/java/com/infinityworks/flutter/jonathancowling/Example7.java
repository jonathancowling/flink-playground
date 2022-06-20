package com.infinityworks.flutter.jonathancowling;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;

import java.time.Duration;

import org.apache.avro.generic.GenericData;
import org.apache.flink.core.fs.Path;
import org.apache.flink.formats.avro.utils.AvroKryoSerializerUtils;
import org.apache.flink.formats.parquet.avro.ParquetAvroWriters;

public class Example7 {

	public static void main(String[] args) throws Exception {
		
		FizzBuzz fb = new FizzBuzz(false);

		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		env.fromSequence(1, 1000)
			.map(i -> new Record(i, fb.map(i))) // FizzBuzz could be a lambda, but for testing it was brought out
			.addSink(
				StreamingFileSink
				    .forBulkFormat(new Path(Path.CUR_DIR, "out"), ParquetAvroWriters.forReflectRecord(Record.class))
					.build()
			);

		// Serialisers must be added to avoid "KryoException: java.lang.UnsupportedOperationException"
		AvroKryoSerializerUtils.getAvroUtils().addAvroSerializersIfRequired(env.getConfig(), GenericData.Record.class);
		
		// checkpointing needed so files get written (as opposed to just inprogress files)
		env.enableCheckpointing(Duration.ofSeconds(1).toMillis());
		env.execute("Parquet with Reflection");
	}
}
