package simulator;

public class PCB {
	private String name;
	private float burstTime;
	private float arrivalTime;
	private float remainingTime;
	private float executed;
	private float beginTime;
	private float endTime;
	private float waitTime;
	private float turnaroundTime;
	
	public float getArrivalTime() { return arrivalTime; }
	public float getBeginTime() { return beginTime; }
	public float getBurstTime() { return burstTime; }
	public float getEndTime() { return endTime; }
	public float getExecuted() { return executed; }
	public float getTurnaroundTime() { return turnaroundTime; }
	public String getName() { return name; }
	public float getRemainingTime() { return remainingTime; }
	public float getWaitTime() { return waitTime; }
	public void setRemainingTime(float remainingTime) { this.remainingTime = remainingTime; }
	public void setWaitTime(float waitTime) { this.waitTime = waitTime; }
	public void setEndTime(float endTime) { this.endTime = endTime; }
	public void setBurstTime(float burstTime) { this.burstTime = burstTime; }
	public void setArrivalTime(float arrivalTime) { this.arrivalTime = arrivalTime; }
	public void setBeginTime(float beginTime) { this.beginTime = beginTime; }
	public void setExecuted(float executed) { this.executed = executed; }
	public void setTurnaroundTime(float turnaroundTime) { this.turnaroundTime = turnaroundTime;}
	
	public PCB(String name, float burstTime, float arrivalTime) {
		this.name = name;
		this.burstTime = burstTime;
		this.remainingTime = burstTime;
		this.arrivalTime = arrivalTime;
	}
}
