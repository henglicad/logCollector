package collector.processor.impl;

import java.util.Date;

import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import collector.database.model.Metric;
import collector.database.util.DatabaseUtil;
import collector.processor.IDataProcessor;
import collector.metric.obj.AccessLog;
import collector.metric.util.LogParser;

public class MetricCounter implements IDataProcessor {
	private final static Logger logger = LoggerFactory
			.getLogger(MetricCounter.class);

	@Override
	public void process(JavaPairInputDStream<String, String> messages) {
		messages.groupByKey()
				.map(tuple -> {
					Iterable<String> logs = tuple._2;

					int num503 = 0;
					int numRequests = 0;
					int numErrors = 0;

					String memoryUsage = null;
					double cpuUsage = -1;

					for (String log : logs) {
						String logType = log.split("\\|\\|")[0];
						String actualLog = log.split("\\|\\|")[1];

						switch (logType) {
						case "ACCESS_LOG":
							AccessLog accessLog = LogParser
									.parseAccessLog(actualLog);
							if (accessLog.getResponseCode() == 503) {
								num503++;
							}

							if (accessLog.getResponseCode() >= 400) {
								numErrors++;
							}
							numRequests++;
							break;
						case "CPU":
							try {
								cpuUsage = Double.parseDouble(actualLog);
							} catch (NumberFormatException e) {
								logger.error(
										"Error when parsing cpu usage: {}",
										cpuUsage, e);
							}
							break;
						case "MEMORY":
							memoryUsage = actualLog;
							break;
						default:
							break;
						}
					}
					return new Metric(numErrors, num503, cpuUsage,
							memoryUsage, numRequests, new Date(), tuple._1);
				}).foreachRDD(rdd -> {
					rdd.collect().stream().forEach(DatabaseUtil::save);
					return null;
				});

		// .foreachRDD(rdd ->
		// rdd.collect().stream().forEach(DatabaseUtil::save));
		// messages.map

		// JavaPairDStream<String, ArrayList<String>> messagesPerTopic =
		// messages
		// .groupByKey();

		// messagesPerTopic.for

		// messages.map(tuple -> tuple._2).filter(log -> log.contains("503"))
		// .count().print();

		// errorCount
		// messagesPerTopic.
		// messagesPerTopic.foreach(new
		// Function<JavaPairRDD<String,Iterable<String>>,Void>(){
		//
		// @Override
		// public Void call(JavaPairRDD<String, Iterable<String>> arg0)
		// throws Exception {
		// // TODO Auto-generated method stub
		// return null;
		// }
		//
		// });

		// messagesPerTopic.filter(f)

		// messagesPerTopic
		// .mapToPair(new PairFunction<String, Iterable<String>, Integer>(){
		//
		// @Override
		// public Tuple2<Iterable<String>, Integer> call(String arg0)
		// throws Exception {
		// // TODO Auto-generated method stub
		// return null;
		// }
		//
		// });
	}
}
