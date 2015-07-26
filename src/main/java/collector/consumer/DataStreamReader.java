package collector.consumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import kafka.serializer.StringDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.Durations;

import collector.processor.IDataProcessor;

public class DataStreamReader {
	private final static Logger logger = LoggerFactory
			.getLogger(DataStreamReader.class);
	private JavaStreamingContext jssc;
	private SparkConf conf;
	private Properties properties;
	private Set<String> topicSet;
	private Map<String, String> kafkaParams;

	public DataStreamReader() {

		readPropertiesFile();
		long interval = Long
				.parseLong(properties.getProperty("batch.interval"));
		String appName = properties.getProperty("app.name");
		String master = properties.getProperty("master");
		
		conf = new SparkConf().setAppName(appName);
		conf.setMaster(master);
		jssc = new JavaStreamingContext(conf, Durations.seconds(interval));

		// get the list of topics, and do a little post processing
		topicSet = new HashSet<String>(Arrays.asList(properties.getProperty(
				"topics").split(",")));
		topicSet = topicSet.stream().map(String::trim)
				.collect(Collectors.toSet());

		String brokers = properties.getProperty("metadata.broker.list");

		kafkaParams = new HashMap<String, String>();
		kafkaParams.put("metadata.broker.list", brokers);

		logger.info(
				"DataStreamReader properties: appName:{}, master:{}, interval:{}, topicSet:{}, brokers:{}",
				appName, master, interval, topicSet, brokers);
	}

	private void readPropertiesFile() {
		properties = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String resourceName = "consumer/consumer-service.properties";
		try (InputStream resourceStream = loader
				.getResourceAsStream(resourceName)) {
			properties.load(resourceStream);
		} catch (IOException e) {
			logger.error("Cannot load property file {}", resourceName, e);
		}
	}

	public void startReadingDataStream(IDataProcessor processor) {
		// first parameter is Kafka topic, and second is content (in this case,
		// a line)
		JavaPairInputDStream<String, String> messages = KafkaUtils
				.createDirectStream(jssc, String.class, String.class,
						StringDecoder.class, StringDecoder.class, kafkaParams,
						topicSet);
		
		processor.process(messages);
		
		// start the computation
		jssc.start();
		jssc.awaitTermination();
		
	}
}
