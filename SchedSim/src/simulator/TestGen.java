package simulator;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

import org.json.simple.*;

public class TestGen {
	
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	private float btLow = 1;				// burst time low/high
	private float btHigh = 20;
	private float atLow = 0;				// arrival time low/high
	private float atHigh = 30;
	
	private static FileWriter file;
	
	// TODO: (1) Priority system; (2) Generate multiple tests
	
	private int procCount;
	private String fldName;
	private String fileName;
	
	public TestGen() {
		this.procCount = 0;
		this.fldName = "Tests";
		this.fileName = "Test";
	}
	
	public int getProcCount() { return this.procCount; }
	public String getFldName() { return this.fldName; }
	public String getFileName() { return this.fileName; }
	
	public void run() {
		readTestInput();
		generateTest();
	}
	
	public void readTestInput() {
		InputScanner userIn = new InputScanner();
		
		System.out.println("Specify Process Count: ");
		this.procCount = userIn.getInt();
		
		System.out.println("Specify Folder Name: ");
		this.fldName = userIn.getLine();
		this.fldName = fldName.replaceAll("\\s", "");
		
		System.out.println("Specify Filename: ");
		this.fileName = userIn.getLine();
		this.fileName = this.fileName.replaceAll("\\s", "");
	}
	
	@SuppressWarnings("unchecked") // JSON Simple is throwing errors I have no way to fix personally without modifying it.
	public void generateTest() {
		JSONObject out = new JSONObject();
		JSONArray titles = new JSONArray();
		JSONArray burstTimes = new JSONArray();
		JSONArray arrivalTimes = new JSONArray();
		
		out.put("Test Name", this.fileName);
		
		for(int i = 0; i < this.procCount; i++) {
			String pTitle = "P" + (i + 1);
			float btVal = ThreadLocalRandom.current().nextFloat(this.btLow, this.btHigh);
			float atVal = ThreadLocalRandom.current().nextFloat(this.atLow, this.atHigh);
			
			titles.add(pTitle);
			burstTimes.add(df.format(btVal));
			arrivalTimes.add(df.format(atVal));
		}
		
		out.put("Process Titles", titles);
		out.put("Burst Time", burstTimes);
		out.put("Arrival Times", arrivalTimes);
		
		try {
			Path path = Paths.get(System.getProperty("user.dir") + "/" + this.fldName + "/");
			Files.createDirectories(path);
			file = new FileWriter(System.getProperty("user.dir") + "/" + this.fldName + "/" + this.fileName);
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
	}
}
