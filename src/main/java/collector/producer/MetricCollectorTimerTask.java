package collector.producer;

import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

import collector.metric.util.SystemMonitor;

public class MetricCollectorTimerTask extends TimerTask {

	private BlockingQueue<String> queue;

	private String cpuData = "CPU||";

	private String memData = "MEMORY||";

	public MetricCollectorTimerTask(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			queue.put(cpuData + SystemMonitor.getCPUUsage());
			queue.put(memData + SystemMonitor.getFreeMemory() + ","
					+ SystemMonitor.getTotalMemorySize());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
