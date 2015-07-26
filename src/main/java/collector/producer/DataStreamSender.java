package collector.producer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example: http://kafka.apache.org/07/quickstart.html
 * https://github.com/gwenshap/kafka-examples
 * 
 * @author tschen
 *
 */
public class DataStreamSender implements Runnable {

	private final static Logger logger = LoggerFactory
			.getLogger(DataStreamSender.class);

	private ProducerConfig config;
	private Producer<String, String> producer;
	private BlockingQueue<String> queue;

	public DataStreamSender() {
		setProperties();
		producer = new Producer<String, String>(config);

	}

	private void setProperties() {
		String resourceName = "producer/producer.properties";
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties producerProperties = new Properties();
		try (InputStream resourceStream = loader
				.getResourceAsStream(resourceName)) {
			producerProperties.load(resourceStream);
			config = new ProducerConfig(producerProperties);
		} catch (IOException e) {
			logger.error("Cannot file property file", e);
		}
	}

	public void close() {
		producer.close();
	}

	public void send(String topic, String value) {
		KeyedMessage<String, String> data = new KeyedMessage<String, String>(
				topic, value);
		producer.send(data);
	}

	public void setQueue(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			while (true) {
				// TODO: may need to add logic to create different topics
				String logLine = queue.take();
				logger.debug("Sending message : {} : {}", "log", logLine);
				// topic, content
				send("log", logLine);
			}
		} catch (InterruptedException e) {
			logger.error("Send error", e);
		}
	}

}
