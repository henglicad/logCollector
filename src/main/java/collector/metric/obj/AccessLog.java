package collector.metric.obj;

public class AccessLog {
	private String time;
	private String threadID;
	private String logLevel;
	private String logType;
	private String url;
	private int responseCode;
	
	
	public AccessLog(){
		
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getThreadID() {
		return threadID;
	}

	public void setThreadID(String threadID) {
		this.threadID = threadID;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
}
