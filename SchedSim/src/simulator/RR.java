package simulator;

import java.util.*;

public class RR extends Scheduler {
	
	private float quantum;
	private int count;
	private Queue<PCB> readyQueue = new LinkedList<>();

	public RR(PCB[] pcb, float quantum) {
		super(pcb);
		this.quantum = quantum;
		this.count = 0;
	}
	
	public void run() {
		this.sortPCB();
		int exitIndex = 0;
		boolean running = true;
		
		
		while(running){												// could do it other (more complicated) ways but.. why not?
			
			checkATimes(true);										// true for being located here in the loop - at start
			
			if(this.readyQueue.isEmpty()) {
				contextChangeTable(exitIndex, exitIndex, this.timeLine, true);
				running = false;
				break;
			}
			
			PCB curPCB = readyQueue.remove(); 							// current PCB from queue
			float pcbBTL = curPCB.getRemainingTime();					// Remaining Burst Time
			int enterIndex = Arrays.asList(this.pcb).indexOf(curPCB);	// Determine whether context change is occuring w/ this index
			
			if(exitIndex != enterIndex){
				contextChangeTable(exitIndex, enterIndex, this.timeLine, false);
			}
			
			if(curPCB.getBurstTime() == curPCB.getRemainingTime()){
				curPCB.setBeginTime(this.timeLine);
			}
			
			if (pcbBTL > quantum) {
				pcbBTL = pcbBTL - quantum;
				this.timeLine = this.timeLine + quantum;
			} else {
				this.timeLine = this.timeLine + pcbBTL;
				pcbBTL = 0;
				curPCB.setCompletionTime(this.timeLine);
			}
			
			exitIndex = Arrays.asList(this.pcb).indexOf(curPCB);
			curPCB.setRemainingTime(pcbBTL);
			checkATimes(false);										// false for being located here - not at start
			
			if (pcbBTL > 0) {
				readyQueue.add(curPCB);
			}
		}
		
		this.averageTime();
		this.resultTable();
	}
	
	private void checkATimes(boolean atStart) {							// moved into its own function to account for timeline shift mid-loop.
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
