package simulator;

import java.util.Arrays;
import java.util.LinkedList;

public class SRTN extends Scheduler {
	
	private int count;
	private LinkedList<PCB> readyQueue = new LinkedList<>();
	
	public SRTN(PCB[] pcb, String fileName, boolean isBatch, OutputWriter writer) {
		super(pcb, writer);
		this.setFileName(fileName);
		this.setBatch(isBatch);
		this.count = 0;
	}
	
	public void run() {
		
		this.sortPCB();
		boolean running = true;
		int exitIndex = 0;
		
		while(running){
			
			checkATimes(true);
			
			if(this.readyQueue.isEmpty()) {
				contextChangeTable(exitIndex, exitIndex, this.timeLine, true);
				running = false;
				break;
			}
			
			PCB curPCB = null;
			float shortestTimeLeft = 0;
			
			for(PCB checkPCB : readyQueue) {
				float rTime = checkPCB.getRemainingTime();
				
				if(shortestTimeLeft == 0 || rTime < shortestTimeLeft) {
					shortestTimeLeft = rTime;
					curPCB = checkPCB;
				}
			}
			
			int enterIndex = Arrays.asList(this.pcb).indexOf(curPCB);	// Determine whether context change is occuring w/ this index
			
			if(exitIndex != enterIndex){
				contextChangeTable(exitIndex, enterIndex, this.timeLine, false);
			}
			
			exitIndex = enterIndex;
			
			int rTime = curPCB.getRemainingTime();
			int nextArrival = nextAT();
			int totTime = rTime + this.timeLine;
			
			if(curPCB.getBurstTime() == rTime){
				curPCB.setBeginTime(this.timeLine);
			}
			
			if(nextArrival > 0) {
				if(totTime > nextArrival ) {
					curPCB.setRemainingTime(rTime - (nextArrival - this.timeLine));
					this.timeLine = nextArrival;
				} else {
					this.timeLine = this.timeLine + rTime;
					curPCB.setRemainingTime(0);
					curPCB.setCompletionTime(this.timeLine);
					readyQueue.remove(curPCB);
				}
			} else {
				this.timeLine = this.timeLine + rTime;
				curPCB.setRemainingTime(0);
				curPCB.setCompletionTime(this.timeLine);
				readyQueue.remove(curPCB);
			}
		}
		
		this.averageTime();
		this.resultTable();
	}
	
	
	private int nextAT() {
		int nextAT = 0;
		
		for(int i = this.count; i < this.pcb.length; i++) {
			PCB checkPCB = this.pcb[i];
			int aTime = checkPCB.getArrivalTime();
			
			if(nextAT == 0 && aTime > this.timeLine) {
				nextAT = aTime;
			} else if (aTime > this.timeLine && aTime < nextAT ) {
				nextAT = aTime;
			}
		}
		
		return nextAT;
	}
	
	private void checkATimes(boolean atStart) {
		if (count < this.pcb.length) {
			
			for(int i = this.count; i < this.pcb.length; i++) {
				PCB checkPCB = this.pcb[i]; 							// the PCB we're checking on
				
				if(checkPCB.getRemainingTime() > 0 && checkPCB.getArrivalTime() <= this.timeLine) { 		// only add to queue if arrival time is reached or passed.
					this.readyQueue.add(checkPCB);
					this.count++;
				} else if(this.readyQueue.isEmpty() && atStart) { 		// Advance time if queue is empty
					this.timeLine = checkPCB.getArrivalTime();
					this.readyQueue.add(checkPCB);
					this.count++;
				}
			}
		}
	}
}
