import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TestAccuracy {
	public static void main(String args[]) throws IOException{
		Path filename;
		String line, res = null;
		Vector<String> tokens;
		String labels;
		String[] reslabel;
		double count = 0, totalcount = 0;
		filename = FileSystems.getDefault().getPath("./", args[0]);
		Path outpath = FileSystems.getDefault().getPath("./", "out");
		BufferedReader test = Files.newBufferedReader(filename, StandardCharsets.UTF_8);
		BufferedReader result = Files.newBufferedReader(outpath, StandardCharsets.UTF_8);
		while((line = test.readLine())!=null){
			res = result.readLine();
			tokens = Functions.tokenizeDoc(line, "raw");
			labels = tokens.get(0);
			reslabel = res.split("\\t");
			totalcount++;
			System.out.println(labels);
			if(labels.contains(reslabel[0])){
				count++;
			}
		}
		System.out.println((float)count/totalcount);
	}
}
