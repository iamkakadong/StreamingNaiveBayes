import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

public class MergeCounts{
	public static void main(String[] args){
		String event = "";
		int count = 0;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
			String line = null;
			if ((line = reader.readLine()) != null){
				Vector<String> eventCount = Functions.tokenizeDoc(line, "msg");
				event = eventCount.get(0);
				count = Integer.parseInt(eventCount.get(1));
			}
			while ((line = reader.readLine()) != null){
				Vector<String> eventCount = Functions.tokenizeDoc(line, "msg");
				if (!eventCount.get(0).equals(event)){
					writer.write(event + ": " + count);
					writer.newLine();
					event = eventCount.get(0);
					count = Integer.parseInt(eventCount.get(1));
				}
				else{
					count += Integer.parseInt(eventCount.get(1));
				}
			}
			writer.write(event + ": " + count);
			writer.newLine();
			writer.flush();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
}