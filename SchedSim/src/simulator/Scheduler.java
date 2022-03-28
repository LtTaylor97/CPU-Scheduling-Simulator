package simulator;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;

public class Scheduler {
	protected int nProcess;
	protected float timeLine = 0;
	protected float avgExecutionTime;
	protected float avgWaitTime;
	protected PCB[] pcb;
	private static final DecimalFormat df = new DecimalFormat("00.000");
	
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
	
	public void averageTime() {
		
		float totTurnTime = 0;
		float totWaitTime = 0;
		
		for(int i = 0; i < this.pcb.length; i++) {
			PCB curPCB = this.pcb[i];
			float turnTime = curPCB.getEndTime() - curPCB.getArrivalTime();
			totTurnTime += turnTime;
			curPCB.setTurnaroundTime(turnTime);
			
			float waitTime = curPCB.getTurnaroundTime() - curPCB.getBurstTime();
			totWaitTime += waitTime;
			curPCB.setWaitTime(waitTime);
		}
		
		this.avgExecutionTime = totTurnTime / this.pcb.length;
		this.avgWaitTime = totWaitTime / this.pcb.length;
	}
	
	public void resultTable() { // Prints out the final details of the simulation
		
		float totBurst = 0;
		
		System.out.println("----------------------------------------------------------------------");
		System.out.println("| Process | Wait Time | Turnaround Time | Arrival Time | Burst Time |"); // Wait, Turnaround, Arrival, Burst, Completion Time, Response Time
		System.out.println("----------------------------------------------------------------------");
		
		for(int i = 0; i < this.pcb.length; i++) {
			PCB curPCB = this.pcb[i];
			String wt = df.format(Math.abs(curPCB.getWaitTime()));			// using abs to avoid the odd negative sign on 0's.
			String tt = df.format(Math.abs(curPCB.getTurnaroundTime()));
			String at = df.format(Math.abs(curPCB.getArrivalTime()));
			String bt = df.format(Math.abs(curPCB.getBurstTime()));
			String nm = curPCB.getName();
			
			totBurst += curPCB.getBurstTime();
			
			System.out.println("| " + nm + "      |  " + wt + "   |    " + tt + "      |    " + at + "    |   " + bt + "   |");
		}
		
		String prnt2 = "| Averages | " + df.format(this.avgWaitTime) + " | " + df.format(this.avgExecutionTime);
		prnt2 = prnt2 + " | Total Burst | " + totBurst;
		
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println(prnt2);
	}
	
	public void preemptionTable(int i, int j, float time) {     // Prints out details of a preemption occurring.
		PCB exitPCB = pcb[i];
		PCB enterPCB = pcb[j];
		System.out.println("-> Context Change at Time: " + time);
		System.out.println("---------------------------------------");
		System.out.println("| Exits            | Enters           |");
		System.out.println("---------------------------------------");
		System.out.println("| " + exitPCB.getName() + "              | " + enterPCB.getName() + "              |");
		System.out.println("---------------------------------------");
		
	}
}
