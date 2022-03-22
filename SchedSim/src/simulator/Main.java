package simulator;

public class Main{
	
	public static void main(String[] args) {
		
		int taskType = getTask(); // 0 to quit, 1 is test generation, 2 is running tests through sim.
		
		if(taskType == 0) {
			return;
		} else if(taskType == 1) {
			TestGen test = new TestGen();
			test.run();
		} else {
			Simulator simRun = new Simulator();
			simRun.runSim();
		}
	}
	
	public static int getTask() {
		InputScanner userIn = new InputScanner();
		int retVal = 5;
		
		while(retVal < 0 || retVal > 2) {
			System.out.println("Select Task [0 - Quit] [1 - Create Test] [2 - Run Simulation]");
			retVal = userIn.getInt();
		}
		
		return retVal;
	}
}