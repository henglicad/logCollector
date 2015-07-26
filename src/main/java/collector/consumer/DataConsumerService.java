package collector.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import collector.processor.impl.Aggregate;
import collector.processor.impl.MetricCounter;

public class DataConsumerService {
	private final static Logger logger = LoggerFactory
			.getLogger(DataConsumerService.class);
	
	public static void main(String[] args){
		DataStreamReader streamReader = new DataStreamReader();
		logger.info("Start reading data stream");
		streamReader.startReadingDataStream(new MetricCounter());
	}
}
