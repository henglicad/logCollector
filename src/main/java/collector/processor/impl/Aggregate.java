package collector.processor.impl;

import java.io.Serializable;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;

import scala.Tuple2;
import collector.processor.IDataProcessor;

public class Aggregate implements IDataProcessor, Serializable {

	private static final long serialVersionUID = 265221371690687266L;

	@Override
	public void process(JavaPairInputDStream<String, String> messages) {

		// DStream: sequence of RDDs that presents streams of data
		// JavaDStream<String> lines = messages.map(
		// new Function<Tuple2<String, String>, String>() {
		// private static final long serialVersionUID = 9174430087884353818L;
		//
		// @Override
		// public String call(Tuple2<String, String> tuple2) {
		// return tuple2._2();
		// }
		// }).cache();
		// JavaPairDStream<String, Integer> wordCounts = lines.mapToPair(
		// new PairFunction<String, String, Integer>() {
		//
		// private static final long serialVersionUID = -5361351005611686720L;
		//
		// @Override
		// public Tuple2<String, Integer> call(String s)
		// throws Exception {
		// return new Tuple2<String, Integer>(s, 1);
		// }
		//
		// }).reduceByKey(new Function2<Integer, Integer, Integer>() {
		// private static final long serialVersionUID = 1597536134161007070L;
		//
		// @Override
		// public Integer call(Integer count1, Integer count2)
		// throws Exception {
		// return count1 + count2;
		// }
		// });

		// Using lambda in Java 8
		// take only the values
		JavaDStream<String> lines = messages.map(tuple2 -> tuple2._2());

		// JavaDStream<Integer> ints = messages.map(tuple2 ->
		// Integer.parseInt(tuple2._2()));

		JavaPairDStream<String, Integer> wordCounts = lines
				.mapToPair(line -> new Tuple2<String, Integer>(line, 1));
		wordCounts.reduceByKey((val1, val2) -> val1 + val2);
		wordCounts.print();
		

		// we can specify the window and the sliding interval
		// lines.window(windowDuration)

		// wordCounts.print();
	}

}
