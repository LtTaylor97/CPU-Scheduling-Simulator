package simulator;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;

import org.json.simple.*;

public class TestGen {
	private int btLow = 1;				// burst time low/high
	private int btHigh = 10;
	private int atLow = 0;				// arrival time low/high
	private int atHigh = 30;
	private int numTests = 1;
	private int numTestsMade = 0;
	private int procCountLow;
	private int procCountHigh;
	private String fldName;
	private String fileName;
	private static FileWriter file;
	
	public TestGen() {
		this.procCountLow = 0;
		this.procCountHigh = 0;
		this.fldName = "Tests";
		this.fileName = "Test";
	}
	
	public void run() {
		readTestInput();
		generateTest();
	}
	
	public void readTestInput() {
		InputScanner userIn = new InputScanner();
		
		System.out.println("Specify Folder Name: ");
		this.fldName = userIn.getLine();
		this.fldName = fldName.replaceAll("\\s", "");
		
		System.out.println("Specify Filename: ");
		this.fileName = userIn.getLine();
		this.fileName = this.fileName.replaceAll("\\s", "");
		
		System.out.println("Specify Number of Tests: ");
		this.numTests = userIn.getInt();
		
		if(this.numTests == 1) {
			System.out.println("Specify Process Count: ");
			this.procCountLow = userIn.getInt();
			this.procCountHigh = this.procCountLow;
			
		} else if(this.numTests > 1) {
			System.out.println("Specify Process Count Min: ");
			this.procCountLow = userIn.getInt();
			
			System.out.println("Specify Process Count Max: ");
			this.procCountHigh = userIn.getInt();
		}
	}
	
	@SuppressWarnings("unchecked") // JSON Simple is throwing warnings I have no way to fix personally without modifying it. Works fine anyway.
	public void generateTest() {
		while(numTestsMade < numTests) {
			JSONObject out = new JSONObject();
			JSONArray titles = new JSONArray();
			JSONArray burstTimes = new JSONArray();
			JSONArray arrivalTimes = new JSONArray();
			int procCount = this.procCountLow;
			
			if (this.procCountLow != this.procCountHigh) {
				procCount = ThreadLocalRandom.current().nextInt(this.procCountLow, this.procCountHigh);
			}
			
			out.put("Test Name", this.fileName);
			
			for(int i = 0; i < procCount; i++) {
				String pTitle = "P" + (i + 1);
				int btVal = this.btLow;
				int atVal = this.atLow;
				
				if (this.btLow != this.btHigh) {
					btVal = ThreadLocalRandom.current().nextInt(this.btLow, this.btHigh);
				}
				
				if (this.atLow != this.atHigh) {
					atVal = ThreadLocalRandom.current().nextInt(this.atLow, this.atHigh);
				}
				
				titles.add(pTitle);
				burstTimes.add(btVal);
				arrivalTimes.add(atVal);
			}
			
			out.put("Process Titles", titles);
			out.put("Burst Times", burstTimes);
			out.put("Arrival Times", arrivalTimes);
			
			try {
				Path path = Paths.get(System.getProperty("user.dir") + "/" + this.fldName + "/");
				Files.createDirectories(path);
				file = new FileWriter(System.getProperty("user.dir") + "/" + this.fldName + "/" + this.fileName + "_" + numTestsMade + ".json");
				file.write(out.toJSONString());
				
			} catch(IOException exc) {
				exc.printStackTrace();
				System.out.println("Test Gen Failed: IOEXC, bad path?");
				
			} finally {
				try {
					file.flush();
					file.close();
					
				} catch (IOException exc) {
					exc.printStackTrace();
					
				}
			}
			numTestsMade++;
		}
	}
}
