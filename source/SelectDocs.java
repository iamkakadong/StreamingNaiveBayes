import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

public class SelectDocs{
	public static void main(String[] args){
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
			String line = null;
			final double p = 0.5;
			final int size = 1;
			int counts = 0;
			Random rand = new Random();
			while (((line = reader.readLine()) != null) && (counts < size)){
				if (rand.nextDouble() < p){
//					writer.newLine();
					writer.write(line);
					writer.newLine();
					counts++;
				}
//				else{;}
			}
//			System.out.println(line);
//			System.out.println(counts);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}