package collector.producer;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataReader implements Runnable {
	private final static Logger logger = LoggerFactory
			.getLogger(DataReader.class);

	private String filePath;
	private BlockingQueue<String> queue;
	private boolean stop = false;

	public DataReader(String filePath) {
		this.filePath = filePath;
	}

	public void setQueue(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	/**
	 * stop reading the file
	 */
	public synchronized void stop() {
		this.stop = true;
	}

	@Override
	public void run() {
		try {
			
			String path = filePath.split("\\|\\|")[1];
			String fileType = filePath.split("\\|\\|")[0];
			
			logger.info("Reading file {}, file type is {}", path, fileType);

			
			int numLineRead = 0; //test code TODO remove
			try (LineNumberReader reader = new LineNumberReader(new FileReader(
					path))) {

				while (!stop) {
					
					String line;

					line = reader.readLine();

					if (line == null || numLineRead > 1000) {
						logger.debug(
								"File {} has no more updates; waiting for a few more seconds.",
								filePath);
						Thread.sleep(2000);
						numLineRead = 0;
						continue;
					}
					logger.debug("Read line: {}", line);
					queue.put(fileType+"||"+line);
					numLineRead++;
				}
			}
		} catch (IOException e) {
			logger.error("Cannot find file: {}", filePath, e);
		} catch (InterruptedException e) {
			logger.error("InterruptedException file: {}", filePath, e);
		}

	}
}
