package simulator;

import java.util.Arrays;
import java.util.Comparator;

public class Scheduler {
	protected int nProcess;
	protected int timeLine = 0;
	protected int avgTurnTime;
	protected int avgWaitTime;
	protected int avgResTime;
	protected float totBurst;
	protected int avgCompTime;
	protected boolean isBatch;
	protected String fileName;
	protected PCB[] pcb;
	protected OutputWriter writer;
	public void setBatch(boolean isBatch) {this.isBatch = isBatch;}
	public void setFileName(String fileName) {this.fileName = fileName;}
	
	public Scheduler(PCB[] pcb, OutputWriter writer) {
		this.pcb = pcb;
		this.nProcess = pcb.length; // ??? is this right?
		this.totBurst = 0;
		this.isBatch = false;
		this.writer = writer;
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
		float totResTime = 0;
		float totCompTime = 0;
		int length = this.pcb.length;
		
		for(int i = 0; i < length; i++) {
			PCB curPCB = this.pcb[i];
			int turnTime = curPCB.getCompletionTime() - curPCB.getArrivalTime();
			totTurnTime += turnTime;
			
			int waitTime = turnTime - curPCB.getBurstTime();
			totWaitTime += waitTime;
			
			int resTime = curPCB.getArrivalTime() - curPCB.getBeginTime();
			totResTime += resTime;
			
			this.totBurst += curPCB.getBurstTime();
			totCompTime += curPCB.getCompletionTime();
			
			curPCB.setTurnaroundTime(turnTime);
			curPCB.setWaitTime(waitTime);
			curPCB.setResponseTime(resTime);
		}
		
		this.avgCompTime = Math.abs(Math.round(totCompTime/length));
		this.avgResTime = Math.abs(Math.round(totResTime/length));
		this.avgTurnTime = Math.abs(Math.round(totTurnTime/length));
		this.avgWaitTime = Math.abs(Math.round(totWaitTime/length));
	}
	
	public String strHelper(int val) {
		return String.valueOf(Math.round(Math.abs(val)));
	}
	
	public void resultTable() { // Prints out the final details of the simulation
		
		if(!this.isBatch) {
			System.out.println("-------------------------------------------------------------------------------------------------------");
			System.out.println("| Process | Arrival Time | Burst Time | Wait Time | Turnaround Time | Completion Time | Response Time |");
			System.out.println("-------------------------------------------------------------------------------------------------------");
			
			for(int i = 0; i < this.pcb.length; i++) {
				PCB curPCB = this.pcb[i];
				String nm = curPCB.getName();
				String at = strHelper(curPCB.getArrivalTime());
				String bt = strHelper(curPCB.getBurstTime());
				
				String wt = strHelper(curPCB.getWaitTime());			// using abs to avoid the odd negative sign on 0's.
				String tt = strHelper(curPCB.getTurnaroundTime());
				String ct = strHelper(curPCB.getCompletionTime());
				String rt = strHelper(curPCB.getResponseTime());
				
				//System.out.println("| " + nm + "      |   " + at + "     |  " + bt + "    |  " + wt + "   |     " + tt + "      |     " + ct + "      |    " + rt + "     |" );
				System.out.printf("| %-7s | %-12s | %-10s | %-9s | %-15s | %-15s | %-13s |%n", nm, at, bt, wt, tt, ct, rt);
			}
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} else {
			System.out.println("-------------------------------------------------------------------------------------------------------");
			System.out.printf( "| %-35s | Wait Time | Turnaround Time | Completion Time | Response Time |\n", this.fileName.replaceAll(".json", ""));
			System.out.println("-------------------------------------------------------------------------------------------------------");
		}
		
		//avgCompTime
		String avgWT = String.valueOf(this.avgWaitTime);
		String avgTT = String.valueOf(this.avgTurnTime);
		String avgCT = String.valueOf(this.avgCompTime);
		String avgRT = String.valueOf(this.avgResTime);
		
		if (this.isBatch) {
			writer.outputAdd(this.fileName, avgWT, avgTT, avgCT, avgRT, this.pcb.length);
		}
		
		System.out.printf("| Averages                            | %-9s | %-15s | %-15s | %-13s |%n", avgWT, avgTT, avgCT, avgRT); 		// print averages
		System.out.println("-------------------------------------------------------------------------------------------------------");
		System.out.printf("| Total Burst Time:      | %-22s | %-15s | %-31s |%n", Math.round(this.totBurst), "Process Count:", this.pcb.length);
		System.out.println("-------------------------------------------------------------------------------------------------------");
	}
	
	public void contextChangeTable(int i, int j, int time, boolean complete) {     // Prints out details of a preemption occurring.
		
		if(!this.isBatch) {
			PCB exitPCB = pcb[i];
			PCB enterPCB = pcb[j];
			
			if(!complete) {
				System.out.println("-> Context Change at Time: " + time);
			} else {
				System.out.println("-> Completion at Time:     " + time);
			}
			System.out.println("---------------------------------------");
			
			if(!complete) {
				System.out.println("| Exits            | Enters           |");
				System.out.println("---------------------------------------");
				System.out.printf("| %-16s | %-16s |%n", exitPCB.getName(), enterPCB.getName());
				System.out.println("---------------------------------------");
			} else {
				System.out.println("| Complete                            |");
				System.out.println("---------------------------------------");
				System.out.printf("| %-35s |%n", exitPCB.getName());
				System.out.println("---------------------------------------");
			}
		}
	}
}
