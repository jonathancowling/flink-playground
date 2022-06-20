package com.infinityworks.flutter.jonathancowling;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;

import java.time.Duration;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.flink.core.fs.Path;
import org.apache.flink.formats.avro.utils.AvroKryoSerializerUtils;
import org.apache.flink.formats.parquet.avro.ParquetAvroWriters;

public class Example6 {

	public static void main(String[] args) throws Exception {
		
		final Schema schema = SchemaBuilder.record("record").fields().requiredString("message").endRecord();
		
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		env.fromSequence(1, 1000)
			.map(new FizzBuzz()) // FizzBuzz could be a lambda, but for testing it was brought out
			.map(s -> (GenericRecord) new GenericRecordBuilder(schema).set("message", s).build())
			.addSink(
				StreamingFileSink
				    .forBulkFormat(new Path(Path.CUR_DIR, "out"), ParquetAvroWriters.forGenericRecord(schema))
					.build()
			);

		// Serialisers must be added to avoid "KryoException: java.lang.UnsupportedOperationException"
		AvroKryoSerializerUtils.getAvroUtils().addAvroSerializersIfRequired(env.getConfig(), GenericData.Record.class);

		// checkpointing needed so files get written (as opposed to just inprogress files)
		env.enableCheckpointing(Duration.ofSeconds(1).toMillis());
		env.execute("Parquet with Schema");
	}
}
