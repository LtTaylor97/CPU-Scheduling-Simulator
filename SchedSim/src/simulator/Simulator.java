package simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Simulator {
	
	private static String[] allowedAlgs = {"rr", "fcfs", "sjf", "srtn"};
	
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
		
		if (algorithm.matches("rr")) {
			RR alg = new RR(pcb, quantum);
			alg.run();
			
		} else if(algorithm.matches("fcfs")) {
			FCFS alg = new FCFS(pcb);
			alg.run();
			
		} else if(algorithm.matches("sjf")) {
			SJF alg = new SJF(pcb);
			alg.run();
			
		} else if(algorithm.matches("srtn")) {
			SRTN alg = new SRTN(pcb);
			alg.run();
		}
		// TODO: save results to a file after completing test(s)? Maybe...
	}
	
	public String[] readSimInput() {
		String[] res = new String[3];
		InputScanner userIn = new InputScanner();
		
		System.out.println("Specify Test Set Filepath + Name: ");
		res[0] = System.getProperty("user.dir") + "/" + userIn.getLine();
		
		System.out.println("Specify Algorithm (FCFS, RR, SJF, SRTN): ");
		res[1] = userIn.getLine();
		res[1] = res[1].toLowerCase();
		
		if (res[1].matches("rr")) {
			System.out.println("Specify Quantum: ");
			res[2] = userIn.getLine();
		} else {
			res[2] = "0";
		}
		
		return res;
	}
	
	private PCB[] readSimValues(String path) throws IOException
    {	
		JSONObject testSet;
		JSONParser jsonParser = new JSONParser();
			
		try (FileReader reader = new FileReader(path)) {
			Object obj = jsonParser.parse(reader);
			testSet = (JSONObject) obj;
			return parseTestSet(testSet);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
            e.printStackTrace();
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
		
		return null;	// Shouldn't ever return this.
    }
	
	private PCB[] parseTestSet(JSONObject set) {
		JSONArray pTitles = (JSONArray) set.get("Process Titles");
		JSONArray pBTimes = (JSONArray) set.get("Burst Times");
		JSONArray pATimes = (JSONArray) set.get("Arrival Times");
		int length = pTitles.size();
		PCB[] pcbArray = new PCB[length];
		
		if(length < 1) {
			System.out.println("File is Empty?");
			return pcbArray;
		} else {
			for (int i = 0; i < length; i++) {
				pcbArray[i] = new PCB((String) pTitles.get(i), Float.parseFloat((String) pBTimes.get(i)), Float.parseFloat((String) pATimes.get(i)));
			}
		}
		
		return pcbArray;
	}
}