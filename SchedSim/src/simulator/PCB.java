package simulator;

public class PCB {
	private String name;
	private int burstTime;
	private int arrivalTime;
	private int remainingTime;
	private int executed;
	private int beginTime;
	private int waitTime;
	private int turnaroundTime;
	private int responseTime;
	private int completionTime;
	
	public int getArrivalTime() { return this.arrivalTime; }
	public int getBeginTime() { return this.beginTime; }
	public int getBurstTime() { return this.burstTime; }
	public int getExecuted() { return this.executed; }
	public int getTurnaroundTime() { return this.turnaroundTime; }
	public String getName() { return this.name; }
	public int getRemainingTime() { return this.remainingTime; }
	public int getWaitTime() { return this.waitTime; }
	public int getResponseTime() { return this.responseTime; }
	public int getCompletionTime() { return this.completionTime; }
	public void setRemainingTime(int remainingTime) { this.remainingTime = remainingTime; }
	public void setWaitTime(int waitTime) { this.waitTime = waitTime; }
	public void setBurstTime(int burstTime) { this.burstTime = burstTime; }
	public void setArrivalTime(int arrivalTime) { this.arrivalTime = arrivalTime; }
	public void setBeginTime(int beginTime) { this.beginTime = beginTime; }
	public void setExecuted(int executed) { this.executed = executed; }
	public void setTurnaroundTime(int turnaroundTime) { this.turnaroundTime = turnaroundTime; }
	public void setResponseTime(int responseTime) { this.responseTime = responseTime; }
	public void setCompletionTime(int completionTime) { this.completionTime = completionTime; }
	
	
	public PCB(String name, int burstTime, int arrivalTime) {
		this.name = name;
		this.burstTime = burstTime;
		this.remainingTime = burstTime;
		this.arrivalTime = arrivalTime;
	}
}
