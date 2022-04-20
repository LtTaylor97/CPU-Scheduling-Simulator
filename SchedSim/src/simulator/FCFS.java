package simulator;

public class FCFS extends Scheduler {

	public FCFS(PCB[] pcb, String fileName, boolean isBatch, OutputWriter writer) {
		super(pcb, writer);
		this.setFileName(fileName);
		this.setBatch(isBatch);
	}
	
	public void run() {
		
		int highBT = 0;
		
		for(int i = 0; i < pcb.length; i++) {
			int curBTime = pcb[i].getBurstTime();
			if (curBTime > highBT) {
				highBT = curBTime;
			}
		}
		
		RR theEasyWay = new RR(pcb, highBT, this.fileName, this.isBatch, this.writer);
		theEasyWay.run();
	}
}
