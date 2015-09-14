import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Countwords2{
	public static void main(String[] args){
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
			String line = null;
			String prevWord = "";
			int distinctWords = 0;
			while ((line = reader.readLine()) != null){
				if (!prevWord.equals(line)){
					prevWord = line;
					distinctWords++;
				}
			}
			System.out.println("Num of distinct words = " + distinctWords);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}