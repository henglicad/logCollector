package collector.processor;

import org.apache.spark.streaming.api.java.JavaPairInputDStream;

public interface IDataProcessor {
	/**
	 * Take in Stream of messages from Kafka
	 * @param messages
	 */
	void process(JavaPairInputDStream<String, String> messages);
}
