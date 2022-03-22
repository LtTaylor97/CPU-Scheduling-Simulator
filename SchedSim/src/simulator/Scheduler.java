package simulator;

import java.util.Arrays;
import java.util.Comparator;

public class Scheduler {
	protected int nProcess;
	protected float timeLine = 0;
	protected float avgExecutionTime;
	protected float avgWaitTime;
	protected float normalization;
	protected PCB[] pcb;
	
	public Scheduler(PCB[] pcb) {
		this.pcb = pcb;
		this.nProcess = pcb.length; // ??? is this right?
	}
	
	public void sortPCB() {
		Arrays.sort(this.pcb, new Comparator<PCB>() {
			@Override
			public int compare(PCB o1, PCB o2) {
				float pcb1AT = o1.getArrivalTime();
				float pcb2AT = o2.getArrivalTime();
				
				if(pcb1AT < pcb2AT) { return -1; }
				else if(pcb1AT == pcb2AT) { return 0; }
				else if(pcb1AT > pcb2AT) { return 1; }
				else {return 0;}
			}
		});
	}
	
	/* not using these yet/not sure what to do with them exactly
	public void normalization() {
		// ????
	}
	
	public void averageTime() {
		// calculates and sets the avg Time values
	}
	
	public void resultTable() {
		// Prints out the full details of the simulation
	}
	
	public void preemptionTable(int i, int j, float time) {
		// Prints out details of a preemption occurring.
	}
	*/
}
