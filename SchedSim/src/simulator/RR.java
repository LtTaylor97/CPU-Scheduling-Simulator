package simulator;

import java.util.*;

public class RR extends Scheduler {
	
	private float quantum;

	public RR(PCB[] pcb, float quantum) {
		super(pcb);
		this.quantum = quantum;
	}
	
	public void run() {
		this.sortPCB();
		Queue<PCB> readyQueue = new LinkedList<>();
		int count = 0;
		boolean running = true;
		
		while(running){												// could do it other (more complicated) ways but.. why not?
			
			if (count < pcb.length) {
				PCB checkPCB = this.pcb[count]; 					// the PCB we're checking on
				
				if(checkPCB.getArrivalTime() <= this.timeLine) { 	// only add to queue if arrival time is reached or passed.
					readyQueue.add(checkPCB);
					count++;
				} else if(readyQueue.isEmpty()) { 					// Advance time if queue is empty
					this.timeLine = checkPCB.getArrivalTime();
					readyQueue.add(checkPCB);
					count++;
				}
			} else if(readyQueue.isEmpty()) {
				running = false;
				break;
			}
			
			PCB curPCB = readyQueue.remove(); 					// current PCB from queue
			float pcbBTL = curPCB.getRemainingTime();
			
			if(curPCB.getBurstTime() == curPCB.getRemainingTime()){
				curPCB.setBeginTime(this.timeLine);
				curPCB.setWaitTime(pcbBTL);
			}
			
			if (pcbBTL > quantum) {
				pcbBTL = pcbBTL - quantum;
				this.timeLine = this.timeLine + quantum;
			} else {
				this.timeLine = this.timeLine + pcbBTL;
				pcbBTL = 0;
				curPCB.setEndTime(this.timeLine);
			}
			
			curPCB.setRemainingTime(pcbBTL);
			
			if (pcbBTL > 0) {
				readyQueue.add(curPCB);
			}
		}
	}
	
	// TODO: Print stuff out as we go too.
	
}
