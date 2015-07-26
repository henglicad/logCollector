package collector.database.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "metric")
public class Metric implements Serializable{
	
	private static final long serialVersionUID = 7981351021683228070L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;

	@Column
	private int numErrors;

	@Column
	private int num503;

	@Column
	private double cpuUsage;

	@Column
	private String memUsage;

	@Column
	private int numRequests;

	/**
	 * Time stamp of when the message stream is received
	 */
	@Column
	private Date timestamp;

	public Metric(int numErrors, int num503, double cpuUsage, String memUsage,
			int numRequests, Date timestamp) {
		setNumErrors(numErrors);
		setNum503(num503);
		setCpuUsage(cpuUsage);
		setMemUsage(memUsage);
		setNumRequests(numRequests);
		setTimestamp(timestamp);
	}

	public long getId() {
		return id;
	}

	/**
	 * All kinds of errors, not just 503
	 * 
	 * @param numErrors
	 */
	public void setNumErrors(int numErrors) {
		this.numErrors = numErrors;
	}

	/**
	 * All kinds of errors, not just 503
	 * 
	 * @param numErrors
	 */
	public int getNumErrors() {
		return this.numErrors;
	}

	public void setNum503(int num503) {
		this.num503 = num503;
	}

	public int getNum503() {
		return this.num503;
	}

	public double getCpuUsage() {
		return this.cpuUsage;
	}

	public void setCpuUsage(double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public String getMemUsage() {
		return this.memUsage;
	}

	public void setMemUsage(String memUsage) {
		this.memUsage = memUsage;
	}

	public int getNumRequests() {
		return this.numRequests;
	}

	public void setNumRequests(int numRequests) {
		this.numRequests = numRequests;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	@Override
	public String toString() {
		return getId() + "," + getNumErrors() + "," + getNum503() + ","
				+ getCpuUsage() + "," + getMemUsage() + "," + getNumRequests()
				+ "," + getTimestamp();
	}
}
