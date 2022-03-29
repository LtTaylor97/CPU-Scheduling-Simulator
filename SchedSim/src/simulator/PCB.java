package simulator;

public class PCB {
	private String name;
	private float burstTime;
	private float arrivalTime;
	private float remainingTime;
	private float executed;
	private float beginTime;
	private float waitTime;
	private float turnaroundTime;
	private float responseTime;
	private float completionTime;
	
	public float getArrivalTime() { return this.arrivalTime; }
	public float getBeginTime() { return this.beginTime; }
	public float getBurstTime() { return this.burstTime; }
	public float getExecuted() { return this.executed; }
	public float getTurnaroundTime() { return this.turnaroundTime; }
	public String getName() { return this.name; }
	public float getRemainingTime() { return this.remainingTime; }
	public float getWaitTime() { return this.waitTime; }
	public float getResponseTime() { return this.responseTime; }
	public float getCompletionTime() { return this.completionTime; }
	public void setRemainingTime(float remainingTime) { this.remainingTime = remainingTime; }
	public void setWaitTime(float waitTime) { this.waitTime = waitTime; }
	public void setBurstTime(float burstTime) { this.burstTime = burstTime; }
	public void setArrivalTime(float arrivalTime) { this.arrivalTime = arrivalTime; }
	public void setBeginTime(float beginTime) { this.beginTime = beginTime; }
	public void setExecuted(float executed) { this.executed = executed; }
	public void setTurnaroundTime(float turnaroundTime) { this.turnaroundTime = turnaroundTime; }
	public void setResponseTime(float responseTime) { this.responseTime = responseTime; }
	public void setCompletionTime(float completionTime) { this.completionTime = completionTime; }
	
	
	public PCB(String name, float burstTime, float arrivalTime) {
		this.name = name;
		this.burstTime = burstTime;
		this.remainingTime = burstTime;
		this.arrivalTime = arrivalTime;
	}
}
