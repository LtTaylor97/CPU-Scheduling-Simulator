package simulator;

import java.util.Arrays;
import java.util.LinkedList;

public class SJF extends Scheduler {
	
	private int count;
	private LinkedList<PCB> readyQueue = new LinkedList<>();
	
	public SJF(PCB[] pcb) {
		super(pcb);
		this.count = 0;
	}
	
	public void run() {
		this.sortPCB();
		boolean running = true;
		int exitIndex = 0;
		
		while(running){												// could do it other (more complicated) ways but.. why not?
			
			checkATimes(true);										// true for being located here in the loop - at start
			
			if(this.readyQueue.isEmpty()) {
				contextChangeTable(exitIndex, exitIndex, this.timeLine, true);
				running = false;
				break;
			}
			
			float shortestTime = 0f;
			PCB curPCB = null;
			
			for(PCB checkPCB : readyQueue) {
				
				float bTime = checkPCB.getBurstTime();
				
				if (shortestTime == 0f || bTime < shortestTime) {
					shortestTime = bTime;
					curPCB = checkPCB;
				}
			}
			
			int enterIndex = Arrays.asList(this.pcb).indexOf(curPCB);
			
			if(exitIndex != enterIndex){
				contextChangeTable(exitIndex, enterIndex, this.timeLine, false);
			}
			
			exitIndex = Arrays.asList(this.pcb).indexOf(curPCB);
			
			
			if (curPCB != null) {
				this.timeLine = this.timeLine + curPCB.getBurstTime();
				curPCB.setRemainingTime(0);
				curPCB.setCompletionTime(this.timeLine);
				readyQueue.remove(curPCB);
			}
		}
		
		this.averageTime();
		this.resultTable();
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
