package simulator;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OutputWriter {
	private String[] outputArray;
	private int outCount = 1;
	private int testCount = 1;
	private String directory = "";
	private String algorithm;
	
	public void setDirectory(String directory) {this.directory = directory;}
	
	public OutputWriter(int testCount, String alg) {
		this.testCount = testCount + 1;
		this.outputInit();
		this.algorithm = alg;
	}
	
	public void outputInit() {
		if (this.outputArray == null) {
			this.outputArray = new String[this.testCount];
			this.outputArray[0] = "Test Name,Program Count,Avg. Wait Time,Avg. Turnaround Time,Avg. Completion Time,Avg. Response Time,Algorithm\n";
		}
	}
	
	public void outputAdd(String fn, String wt, String tt, String ct, String rt, int count) {
		this.outputArray[outCount] = (fn + "," + count + "," + wt + "," + tt + "," + ct + "," + rt + "," + this.algorithm + "\n");
		this.outCount++;
	}
	
	public void writeToFile() {
		try {
			Path path = Paths.get(this.directory + "/" + "Results" + "/");
			Files.createDirectories(path);
			FileWriter file = new FileWriter(this.directory + "/" + "Results" + "/" + "Results_" + algorithm + ".csv");
			
			for (String out : this.outputArray) {
				if(out != null) {
					file.write(out);
				}
			}
			
			file.close();
			
		} catch(IOException exc) {
			exc.printStackTrace();
			System.out.println("Test Gen Failed: IOEXC, bad path?");
		}
	}
}
