package collector.producer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataProducerService {
	private final static Logger logger = LoggerFactory
			.getLogger(DataProducerService.class);

	public static void main(String[] args) {
		BlockingQueue<String> queue = new ArrayBlockingQueue<String>(10000);
		// read properties
		Properties properties = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String resourceName = "producer/producer-service.properties";
		try (InputStream resourceStream = loader
				.getResourceAsStream(resourceName)) {
			properties.load(resourceStream);
		} catch (IOException e) {
			logger.error("Cannot load property file {}", resourceName, e);
		}

		// do a little post processing
		List<String> fileList = Arrays.asList(properties.getProperty(
				"log.files").split(","));
		fileList = fileList.stream().map(String::trim)
				.collect(Collectors.toList());

		// TODO: may want to send in batches
		
		// starting the jobs
		DataStreamSender sender = new DataStreamSender();
		sender.setQueue(queue);
		new Thread(sender).start();
		
		// sending performance data periodically
		TimerTask task = new MetricCollectorTimerTask(queue); 
    	Timer timer = new Timer();
		timer.schedule(task, 0, 10000); // collect every 10 sec
		
		// read log files
		List<DataReader> logReaders = new ArrayList<DataReader>();
		fileList.forEach(file -> logReaders.add(new DataReader(file)));
		logReaders.stream().forEach(reader -> {
			reader.setQueue(queue);
			new Thread(reader).start();
		});
		
		

	}
}
