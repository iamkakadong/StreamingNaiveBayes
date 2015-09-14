# StreamingNaiveBayes

####Please read LICENSE file carefully.

To test the code, you should first compile individual java files using:
`javac *.java`

Then `cat testfile.txt | java -Xmx128m NBTrain | sort -k1,1 | java -Xmx128m MergeCounts | java -Xmx128m NBTest testfile.txt` will provide the predictions to each line of testfile.

