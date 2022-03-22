package simulator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Simulator {
	
	private static String[] allowedAlgs = {"rr", "fcfs"};
	
	public void runSim() {
		String[] inputResults = readSimInput();
		String fileName = inputResults[0];
		String algorithm = inputResults[1];
		float quantum = Float.parseFloat(inputResults[2]);
		File f = new File(fileName);
		
		while(!f.exists() || f.isDirectory() || !Arrays.asList(allowedAlgs).contains(algorithm)) { // handles bad input
			if(!f.exists() || f.isDirectory()) {
				System.out.println("Bad Filepath");
			} else if(!Arrays.asList(allowedAlgs).contains(algorithm)) {
				System.out.println("Invalid Algorithm");
			}
			System.out.println("Please Retry Inputs");
			inputResults = readSimInput();
			fileName = inputResults[0];
			algorithm = inputResults[1];
			quantum = Float.parseFloat(inputResults[2]);
			f = new File(fileName);
		}
		
		PCB[] pcb;
		
		try {
			pcb = readSimValues(fileName);
		} catch (IOException exc) {
			System.out.println("Fatal File Error");
			return;
		}
		
		if (algorithm == "rr") {
			RR alg = new RR(pcb, quantum);
			alg.run();
		} else if(algorithm == "fcfs") {
			// FCFS alg = new FCFS(pcb);
			// alg.run();
		}
		// TODO: stuff with PCB data after run
	}
	
	public static String[] readSimInput() {
		String[] res = new String[3];
		
		InputScanner userIn = new InputScanner();
		
		System.out.println("Specify Dataset Filename: ");
		res[0] = userIn.getLine();
		
		System.out.println("Specify Algorithm (FCFS, RR): ");
		res[1] = userIn.getLine();
		res[1] = res[1].replaceAll("\\s", "").toLowerCase();
		
		if (res[1] == "rr") {
			System.out.println("Specify Quantum: ");
			res[2] = userIn.getLine();
		} else {
			res[2] = "0";
		}
		
		return res;
	}
	
	public static PCB[] readSimValues(String path) throws IOException // TODO: Update to JSON Simple. This would be simpler to format plain in .txt by spacing and lines but eh.
    {
		long lines = 0;
		lines = Files.lines(Paths.get(path)).count();
		
        PCB[] pcbArray = new PCB[(int) lines];//new ArrayList<Integer>();
        File srcFile = new File(path);
        Scanner srcReader = new Scanner(srcFile);
        
        int count = 0;
        while (srcReader.hasNextLine()){
        	pcbArray[count] = new PCB( srcReader.next(), srcReader.nextFloat(), srcReader.nextFloat()); // name, burst, arrival
        	count++;
        }
        
        srcReader.close();
        return pcbArray;
    }
}