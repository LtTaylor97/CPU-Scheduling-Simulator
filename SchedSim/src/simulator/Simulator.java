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
	protected String algorithm;
	protected int quantum;
	protected int testCount;
	protected OutputWriter writer;
	
	public void runSim() {
		String[] inputResults = readSimInput();
		String filePath = inputResults[0];
		this.algorithm = inputResults[1];
		this.quantum = Integer.valueOf(inputResults[2]);
		File f = new File(filePath);
		
		while((!f.exists() && !f.isDirectory()) || !Arrays.asList(allowedAlgs).contains(this.algorithm)) { // handles bad input
			
			if(!f.exists()) {
				System.out.println("Bad Filepath");
			} else if(!Arrays.asList(allowedAlgs).contains(this.algorithm)) {
				System.out.println("Invalid Algorithm");
			}
			
			System.out.println("Please Retry Inputs");
			inputResults = readSimInput();
			filePath = inputResults[0];
			this.algorithm = inputResults[1];
			this.quantum = Integer.parseInt(inputResults[2]);
			f = new File(filePath);
		}
		
		if (f.isDirectory()) {
			File[] listFiles = f.listFiles();
			this.testCount = listFiles.length;
			this.writer = new OutputWriter(this.testCount, this.algorithm);
			this.writer.setDirectory(filePath);
			for(int i = 0; i < this.testCount; i++) {
				File passFile = listFiles[i];
				
				if (!passFile.isDirectory()) {
					if(i != listFiles.length - 1) {
						runAlg(passFile.getPath(), true, passFile.getName(), false);
					} else {
						runAlg(passFile.getPath(), true, passFile.getName(), true);
					}
				}
			}
		} else {
			runAlg(filePath, false, f.getName(), false);
		}
	}
	
	public void runAlg(String filePath, boolean isBatch, String fileName, boolean last) {
		PCB[] pcb;
		
		try {
			pcb = readSimValues(filePath);
		} catch (IOException exc) {
			System.out.println("Fatal File Error");
			return;
		}
		
		if (this.algorithm.matches("rr")) {
			RR alg = new RR(pcb, this.quantum, fileName, isBatch, writer);
			alg.run();
			if (last == true) {
				writer.writeToFile();
			}
		} else if(this.algorithm.matches("fcfs")) {
			FCFS alg = new FCFS(pcb, fileName, isBatch, writer);
			alg.run();
			if (last) {
				writer.writeToFile();
			}
		} else if(this.algorithm.matches("sjf")) {
			SJF alg = new SJF(pcb, fileName, isBatch, writer);
			alg.run();
			if (last) {
				writer.writeToFile();
			}
		} else if(this.algorithm.matches("srtn")) {
			SRTN alg = new SRTN(pcb, fileName, isBatch, writer);
			alg.run();
			if (last) {
				writer.writeToFile();
			}
		}
	}
	
	public String[] readSimInput() {
		String[] res = new String[3];
		InputScanner userIn = new InputScanner();
		
		System.out.println("Specify Test Set Directory: ");
		String filePath = System.getProperty("user.dir") + "/" + userIn.getLine();
		
		System.out.println("Specify File Name (blank for batch): ");
		String fileName = userIn.getLine();
		
		if(fileName.isBlank()) {
			res[0] = filePath;
		} else {
			res[0] = filePath + "/" + fileName;
		}
		
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
				pcbArray[i] = new PCB((String) pTitles.get(i), ((Long) pBTimes.get(i)).intValue(), ((Long) pATimes.get(i)).intValue());
			}
		}
		
		return pcbArray;
	}
}