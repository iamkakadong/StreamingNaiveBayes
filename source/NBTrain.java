import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Vector;

public class NBTrain {
	public static void main(String[] args){
		int bufferSize = 500;
		HashMap<String, Integer> localBuffer = new HashMap<String, Integer>(bufferSize);
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
			String line = null;
			while ((line = reader.readLine()) != null){
				// Tokenize Doc
				Vector<String> words = Functions.tokenizeDoc(line, "raw");
				// Emit counter messages
				Functions.counts(words, localBuffer, bufferSize);
			}
			Functions.printCurrentHashTable(localBuffer);	// When the iteration is over, deal with entries left in hashtable.
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
