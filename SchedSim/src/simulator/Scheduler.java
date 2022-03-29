package simulator;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;

public class Scheduler {
	protected int nProcess;
	protected float timeLine = 0;
	protected float avgTurnTime;
	protected float avgWaitTime;
	protected float avgResTime;
	protected float totBurst;
	protected float avgCompTime;
	protected PCB[] pcb;
	private static final DecimalFormat df = new DecimalFormat("00.000");
	
	public Scheduler(PCB[] pcb) {
		this.pcb = pcb;
		this.nProcess = pcb.length; // ??? is this right?
		this.totBurst = 0;
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
			float turnTime = curPCB.getCompletionTime() - curPCB.getArrivalTime();
			totTurnTime += turnTime;
			
			float waitTime = turnTime - curPCB.getBurstTime();
			totWaitTime += waitTime;
			
			float resTime = curPCB.getArrivalTime() - curPCB.getBeginTime();
			totResTime += resTime;
			
			this.totBurst += curPCB.getBurstTime();
			totCompTime += curPCB.getCompletionTime();
			
			curPCB.setTurnaroundTime(turnTime);
			curPCB.setWaitTime(waitTime);
			curPCB.setResponseTime(resTime);
		}
		
		this.avgCompTime = totCompTime / length;
		this.avgResTime = totResTime / length;
		this.avgTurnTime = totTurnTime / length;
		this.avgWaitTime = totWaitTime / length;
	}
	
	public void resultTable() { // Prints out the final details of the simulation
		
		
		
		System.out.println("-------------------------------------------------------------------------------------------------------");
		System.out.println("| Process | Arrival Time | Burst Time | Wait Time | Turnaround Time | Completion Time | Response Time |");
		System.out.println("-------------------------------------------------------------------------------------------------------");
		
		for(int i = 0; i < this.pcb.length; i++) {
			PCB curPCB = this.pcb[i];
			String nm = curPCB.getName();
			String at = df.format(Math.abs(curPCB.getArrivalTime()));
			String bt = df.format(Math.abs(curPCB.getBurstTime()));
			
			String wt = df.format(Math.abs(curPCB.getWaitTime()));			// using abs to avoid the odd negative sign on 0's.
			String tt = df.format(Math.abs(curPCB.getTurnaroundTime()));
			String ct = df.format(Math.abs(curPCB.getCompletionTime()));
			String rt = df.format(Math.abs(curPCB.getResponseTime()));
			
			
			
			System.out.println("| " + nm + "      |   " + at + "     |  " + bt + "    |  " + wt + "   |     " + tt + "      |     " + ct + "      |    " + rt + "     |" );
		}
		//avgCompTime
		String avgWT = df.format(Math.abs(this.avgWaitTime));			// Adding a 0 to a float (sometimes) apparently inverts the float's sign bit because...? What?????
		String avgTT = df.format(Math.abs(this.avgTurnTime));
		String avgCT = df.format(Math.abs(this.avgCompTime));
		String avgRT = df.format(Math.abs(this.avgResTime));
		
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("| Averages                            |  " + avgWT + "   |     " + avgTT + "      |     " + avgCT + "      |    " + avgRT + "     |"); 		// print averages
		System.out.println("-------------------------------------------------------------------------------------------------------");
		System.out.println("| Total Burst Time       |  " + this.totBurst + "                                                                      |");			// total burst time - semi-useful?
		System.out.println("-------------------------------------------------------------------------------------------------------");
	}
	
	public void contextChangeTable(int i, int j, float time, boolean complete) {     // Prints out details of a preemption occurring.
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
			System.out.println("| " + exitPCB.getName() + "               | " + enterPCB.getName() + "               |");
			System.out.println("---------------------------------------");
		} else {
			System.out.println("| Complete                            |");
			System.out.println("---------------------------------------");
			System.out.println("| " + exitPCB.getName() + "                                  |");
			System.out.println("---------------------------------------");
		}
		
	}
}
