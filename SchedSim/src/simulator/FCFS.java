package simulator;

public class FCFS extends Scheduler {

	public FCFS(PCB[] pcb) {
		super(pcb);
	}
	
	public void run() {
		
		float highBT = 0.0f;
		
		for(int i = 0; i < pcb.length; i++) {
			float curBTime = pcb[i].getBurstTime();
			if (curBTime > highBT) {
				highBT = curBTime;
			}
		}
		
		RR theEasyWay = new RR(pcb, highBT);
		theEasyWay.run();
	}
}
