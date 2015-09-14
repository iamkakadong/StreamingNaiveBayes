import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Countwords{
	public static void main(String[] args){
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
			String line = null;
			while ((line = reader.readLine()) != null){
				String[] words;
				words = line.split(":\\s");
				Matcher m = Pattern.compile("^Y = [\\w]{2}+ and W = (\\w+)$").matcher(words[0]);
				if (m.find()){
					if (!m.group(1).equals("ANY")){
						System.out.println(m.group(1));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}