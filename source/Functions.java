import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Functions{

	static Vector<String> tokenizeDoc(String curDoc, String config){
		// Separate a document by space into a vector of strings. ignore special characters.
		Vector<String> tokens = new Vector<String>();
		int index;
		String buffer;
		
		if (config == "raw") {
			index = curDoc.indexOf('\t');
			tokens.addElement(curDoc.substring(0, index).replaceAll("\\W+", ""));
			curDoc = curDoc.substring(index + 1);
			while ((index = curDoc.indexOf(' ')) != -1){
				buffer = curDoc.substring(0, index);
				buffer = buffer.replaceAll("\\W+", "");
				if (buffer.length() > 0) tokens.addElement(buffer);
				curDoc = curDoc.substring(index + 1);
			}
			curDoc = curDoc.replaceAll("\\W+",  "");
			if (curDoc.length() > 0) tokens.addElement(curDoc);
		}
		else if (config == "msg"){
			index = curDoc.indexOf(":");
			tokens.addElement(curDoc.substring(0, index));
			tokens.addElement(curDoc.substring(index + 2));
		}
		else throw new java.lang.IllegalArgumentException("configuration must be one of \"msg\" or \"raw\"");
		
		return tokens;
	}

	static void counts(Vector<String> words, HashMap<String, Integer> curDict, int bufferSize){
		// Performs label-data counting procedure as a training process of Naive Bayes.
		
		// extract labels 'y' from the tokens and add event-counts to hashtable.
		Vector<String> labels = new Vector<String>();
		String labelComb = words.get(0);
		for (int i = 0; i < labelComb.length(); i+=2){
			String tmp = labelComb.substring(i, i+2);
			labels.add(tmp);
			safeAddToHashTable("Y = " + tmp, 1, curDict, bufferSize);
			safeAddToHashTable("Y = ANY", 1, curDict, bufferSize);
		}
		
		// If possible, add event information first to memory-allocated hashtable to pre-process;
		// Otherwise emit information from hashtable and reset the table so that pre-process is again available.
		for (int j = 1; j < words.size(); j++){
			for (int k = 0; k < labels.size(); k++){
				safeAddToHashTable("Y = " + labels.get(k) + " and W = " + words.get(j), 1,
						curDict, bufferSize);
				safeAddToHashTable("Y = " + labels.get(k) + " and W = ANY", 1,
						curDict, bufferSize);
			}
		}
	}
	
	static HashSet<String> tokenizeTest(String curDoc, HashSet<String> hs){
		/* Tokenize input by space and ignore special characters. Starting with the 2nd token, add to a hashset as specified by input.
		The 2nd token and on are supposed to be test string, while the first token is just meaningless to keep format consistent. */
		curDoc = curDoc.substring(curDoc.indexOf('\t') + 1);
		String buffer;
		int index;
		while ((index = curDoc.indexOf(' ')) != -1){
			buffer = curDoc.substring(0, index);
			buffer = buffer.replaceAll("\\W+", "");
			if (buffer.length() > 0) hs.add(buffer);
			curDoc = curDoc.substring(index + 1);
		}
		return hs;
	}

	static void processMsg(String msg, HashSet<String> needed, Vector<String> labels, HashMap<String, Integer> dict){
		/* Process stream message from MergeCounts. For each message, may discard, may add to set of labels, may add to a
		hashmap that contains event-count pair of relevant words. Helper<Vector<String>, HashMap<String, Integer>>*/

		String[] words;
		words = msg.split(":\\s");

		// if event is "Y = xx", add to list of labels and record event-count to hashmap.
		if (Pattern.matches("^Y = \\w\\w$", words[0])){
			String tmp = words[0].substring(words[0].length() - 2, words[0].length());
			labels.add(tmp);
			dict.put(words[0], Integer.parseInt(words[1]));
		}
		// if event is "Y = ANY", record event-count to hashmap.
		else if (Pattern.matches("^Y = ANY$", words[0])){
			dict.put(words[0], Integer.parseInt(words[1]));
		}
		// if event is "Y = xx and W = some words", check whether the word is needed. if so, record event-count to hashmap.
		else{
			Matcher m = Pattern.compile("^Y = [\\w]{2}+ and W = (\\w+)$").matcher(words[0]);
			if (m.find()){
				if (m.group(1).equals("ANY") || needed.contains(m.group(1))){
					dict.put(words[0], Integer.parseInt(words[1]));
				}
			}
		}
	}

	static void predict(String curDoc, Vector<String> labels, HashMap<String, Integer> dict, int wordCounts){
		double maxLog = -1000000000;
		String opLabel = "";
		double logLikelihood = 0;
		String label = "";
		int distWords = (int) (wordCounts * 5 / labels.size());
		final double alpha = 5;	// smooth factor
		Vector<String> words = tokenizeDoc(curDoc, "raw");
		for (int i = 0; i < labels.size(); i++){
			logLikelihood = 0;
			label = labels.get(i);
			double tmp = dict.get("Y = " + label + " and W = ANY") + alpha * distWords; 
			// double tmp = dict.get("Y = " + label + " and W = ANY");
			// System.out.println(dict.get("Y = " + label + " and W = ANY"));
			for (int j = 0; j < words.size(); j++){
				if (dict.containsKey("Y = " + label + " and W = " + words.get(j))){	
					logLikelihood += Math.log((dict.get("Y = " + label + " and W = " + words.get(j)) + alpha) / tmp);
					// logLikelihood += Math.log(dict.get("Y = " + label + " and W = " + words.get(j)) / tmp);
				}
				else{
					logLikelihood += Math.log(alpha / tmp);
				}
			}
			logLikelihood += Math.log((float)dict.get("Y = " + label) / dict.get("Y = ANY"));
			if (logLikelihood > maxLog){
				maxLog = logLikelihood;
				opLabel = label;
			}
		}
		System.out.println(opLabel + '\t' + maxLog);
	}
	
	static void printCurrentHashTable(HashMap<String, Integer> ht){
		// Prints key-value pairs in current hashtable line by line to System.out.
		
		try { 
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
			if (ht.size() != 0) {
				for (Entry<String, Integer> entry: ht.entrySet()){
					writer.write(entry.getKey() + ": " + entry.getValue().intValue());
					writer.newLine();
				}
			}
			writer.flush();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private static void safeAddToHashTable(String key, int value, HashMap<String, Integer> ht, int bufferSize){
		/* Adds key-value pair into hashtable such that if adding the new pair exceeds buffer size
		(for sake of memory allocation), process all entries in ht and then clear the table. */ 
		
		if (ht.containsKey(key)){	
			// if key already exist, increment value as specified by input
			ht.put(key, ht.get(key) + value);
		}
		else if (ht.size() < bufferSize){	
			// if key is new, create new entry in ht.
			ht.put(key, value);
		}
		else{	
			// if spills, output all that's in the current table and reset table.
			printCurrentHashTable(ht);
			ht.clear();
			ht.put(key, value);
		}
	}
	

}