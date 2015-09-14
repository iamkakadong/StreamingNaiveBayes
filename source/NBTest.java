import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class NBTest{
	public static void main(String[] args) throws IOException{
		if (args.length != 1) throw new java.lang.IllegalArgumentException("Input must have exactly 1 parameter, now it has " + args.length);
		Path filepath = FileSystems.getDefault().getPath("./", args[0]);

		// scan through test file to retrieve words.
		Vector<String> quests = new Vector<String>();
		BufferedReader fileReader = Files.newBufferedReader(filepath, StandardCharsets.UTF_8);
		String line = null;
		HashSet<String> needed = new HashSet<String>();
		while ((line = fileReader.readLine()) != null){
			needed = Functions.tokenizeTest(line, needed);
			quests.add(line);
		}
		
		// scan through messages to build hashmap of relavent words and their counts
		BufferedReader streamReader = new BufferedReader(new InputStreamReader(System.in));
		HashMap<String, Integer> relevantCounts = new HashMap<String, Integer>();
		Vector<String> labels = new Vector<String>();
		int wordCounts = 0;
		while ((line = streamReader.readLine()) != null){
			Functions.processMsg(line, needed, labels, relevantCounts);
			wordCounts++;
		}
		
		// predict most probable label for each line of document
		while (!quests.isEmpty()){
			line = quests.firstElement();
			Functions.predict(line, labels, relevantCounts, wordCounts);
			quests.remove(0);
		}
	}
}