package testhadoopproject;
//package org.myorg;
import java.io.IOException;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
import java.io.*;
import java.nio.*;

class lengthLCS
{
public static int compute(String x, String y)
{
      int M = x.length();
      int N = y.length();

      // opt[i][j] = length of LCS of x[i..M] and y[j..N]
      int[][] opt = new int[M+1][N+1];

      // compute length of LCS and all subproblems via dynamic programming
      for (int i = M-1; i >= 0; i--) {
          for (int j = N-1; j >= 0; j--) {
              if (x.charAt(i) == y.charAt(j))
                  opt[i][j] = opt[i+1][j+1] + 1;
              else
                  opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1]);
          }
      }
      
      return opt[0][0];
  }
public static String computeString(String x, String y)
{
      int M = x.length();
      int N = y.length();

      // opt[i][j] = length of LCS of x[i..M] and y[j..N]
      int[][] opt = new int[M+1][N+1];

      // compute length of LCS and all subproblems via dynamic programming
      for (int i = M-1; i >= 0; i--) {
          for (int j = N-1; j >= 0; j--) {
              if (x.charAt(i) == y.charAt(j))
                  opt[i][j] = opt[i+1][j+1] + 1;
              else
                  opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1]);
          }
      }
      String s = "";
      // recover LCS itself and print it to standard output
      int i = 0, j = 0;
      while(i < M && j < N) {
          if (x.charAt(i) == y.charAt(j))
          {
              //System.out.print(x.charAt(i));
              s = s+x.charAt(i);
              i++;
              j++;
          }
          else if (opt[i+1][j] >= opt[i][j+1]) i++;
          else                                 j++;
      }
      //System.out.println();
      return s;
  }
  public static class Map1 extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
         private final static IntWritable one = new IntWritable(1);
     private Text word = new Text();
     String s1, s2;
     
     public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
         String[] line = value.toString().split(" ");
         BufferedReader br = null; String s1="", s2="";
	      
			try {	 
				br = new BufferedReader(new FileReader(line[0]));
				String s;
				while((s=br.readLine())!=null){s1+="\n"+s;}
				br = new BufferedReader(new FileReader(line[1]));
				while((s=br.readLine())!=null){s2+="\n"+s;}
	 
			} catch (IOException e) {
				e.printStackTrace();
			}
         
 int r= compute(s1, s2);
 r=Integer.MAX_VALUE-r;
 word.set(r+","+line[0]+","+line[1]+",");
 output.collect(word, new IntWritable(0));
    }
  }public static class Reduce1 extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
    public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
    output.collect(key, values.next());
    }
  }
  public static class Reduce2 extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
	    public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
	    output.collect(key, values.next());
	    }
	  }
  public static class Map2 extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
      private final static IntWritable one = new IntWritable(1);
  private Text word = new Text();
  String s1, s2;
  public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
      String[] line = value.toString().split(" ");
     BufferedReader br = null; String r,s1="", s2="";
     if(line.length<2){
    	 try {	 
    			br = new BufferedReader(new FileReader(line[0]));
    			String s;
    			while((s=br.readLine())!=null){s1+=s;}
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	 
    	r=s1; 
     } 
     else{
	try {	 
		br = new BufferedReader(new FileReader(line[0]));
		String s;
		while((s=br.readLine())!=null){s1+=s;}
		br = new BufferedReader(new FileReader(line[1]));
		while((s=br.readLine())!=null){s2+=s;}

	} catch (IOException e) {
		e.printStackTrace();
	}
	r= computeString(s1, s2);}
	word.set(r);
	output.collect(word, new IntWritable(0));
     }
}
  public static void main(String[] args) throws Exception {
    JobConf conf = new JobConf(lengthLCS.class);
    conf.setJobName("lengthLCS");

    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(IntWritable.class);

    conf.setMapperClass(Map1.class);
    conf.setCombinerClass(Reduce1.class);
    conf.setReducerClass(Reduce1.class);

    conf.setInputFormat(TextInputFormat.class);
    conf.setOutputFormat(TextOutputFormat.class);
    String initPath="/home/hduser/Pooja/hadoop-1.2.1/wordcount_classes/input";
    File dir=new File(initPath);
    BufferedWriter out = null;
    try  
    {
        FileWriter fstream = new FileWriter("/home/hduser/Pooja/hadoop-1.2.1/wordcount_classes/pair/pairfile.txt", false); //true tells to append data.
        out = new BufferedWriter(fstream);
            }
    catch (IOException e)
    {
        System.err.println("Error: " + e.getMessage());
    }
    
    int len=dir.list().length; String[] stream=dir.list();
    for (int i=0; i<len; i++) {
    	for(int j=i+1	; j<len; j++){
    			out.write(initPath+"/"+stream[i]+" "+initPath+"/"+stream[j]+"\n"); 
    			//System.out.println(stream[i]+" "+stream[j]+"\n"); 
}
  }
    out.close();
      FileInputFormat.setInputPaths(conf, "/home/hduser/Pooja/hadoop-1.2.1/wordcount_classes/pair");
      Path reduce1=new Path("/home/hduser/Pooja/hadoop-1.2.1/wordcount_classes/Reduce1Folder");
      FileOutputFormat.setOutputPath(conf, reduce1);
      JobClient.runJob(conf);
      driver2("/home/hduser/Pooja/hadoop-1.2.1/wordcount_classes/Reduce1Folder/part-00000", "/home/hduser/Pooja/hadoop-1.2.1/wordcount_classes/FinalFolder", initPath);
  }
  public static void driver2(String inputPath, String outputPath, String initPath) throws Exception {
	  	  int k=4;
	      JobConf conf = new JobConf(lengthLCS.class);
	      conf.setJobName("lengthLCS");

	      conf.setOutputKeyClass(Text.class);
	      conf.setOutputValueClass(IntWritable.class);

	      conf.setMapperClass(Map2.class);
	      conf.setCombinerClass(Reduce2.class);
	      conf.setReducerClass(Reduce2.class);

	      conf.setInputFormat(TextInputFormat.class);
	      conf.setOutputFormat(TextOutputFormat.class); 
	      // read top k files 
	      BufferedWriter out = null;
	      BufferedReader br = null;
	      
			try {
	 
				String sCurrentLine;
	 
				br = new BufferedReader(new FileReader(inputPath));
	 
			} catch (IOException e) {
				e.printStackTrace();
			}
			HashSet<String> hs=new HashSet();
	      try  
	      {
	          FileWriter fstream = new FileWriter("/home/hduser/Pooja/hadoop-1.2.1/wordcount_classes/kprime/kprimedocs", false); //true tells to append data.
	          out = new BufferedWriter(fstream);
	          for (int i=0; i<k; i++) {	
		    	    String[] stream=br.readLine().split(",");
		    	    hs.add(stream[1].trim());
		    	    hs.add(stream[2].trim());
	      			//System.out.println(stream[i]+" "+stream[j]+"\n"); 
	  	    }
	          int odd=0;
	         for(String s: hs){
	        	 //out.write(s);
	        	 if(odd==0){
	        	 out.write(s); odd=1;}
	        	 else {
	        		 out.write(" "+s+"\n"); odd=0;
	        	 }
	         }
	      }//end of try
	      catch (IOException e)
	      {
	          System.err.println("Error: " + e.getMessage());
	      }
	      
	      out.close(); 
	      Path reduce2=new Path("/home/hduser/Pooja/hadoop-1.2.1/wordcount_classes/Reduce2Folder");
	      
	      FileInputFormat.setInputPaths(conf, "/home/hduser/Pooja/hadoop-1.2.1/wordcount_classes/kprime");
	      FileOutputFormat.setOutputPath(conf, reduce2);
	      JobClient.runJob(conf);
	      perform("/home/hduser/Pooja/hadoop-1.2.1/wordcount_classes/Reduce2Folder/part-00000");
	    }
  public static void perform(String inputPath){
	  BufferedReader br = null; BufferedWriter out = null;
      
		try {
			/*try{
			
		    
	          FileWriter fstream = new FileWriter("/home/hduser/Pooja/hadoop-1.2.1/wordcount_classes/part2.txt", true); //true tells to append data.
	          out = new BufferedWriter(fstream);
	          out.write("Pooja");}
			
		    catch (IOException e)
		    {
		        System.err.println("Error: " + e.getMessage());
		    }
		    */

			String sCurrentLine1, sCurrentLine2, result="";

			br = new BufferedReader(new FileReader(inputPath));
			br.readLine();
			if((sCurrentLine1=br.readLine())!=null)
				{
				String[] s1=sCurrentLine1.split(" ");
				sCurrentLine1="";  //removing last value '0' of each key
				for(int i=0; i<s1.length-1; i++){
					sCurrentLine1+=s1[i].trim();
				}
				if((sCurrentLine2=br.readLine())!=null){
					
					s1=sCurrentLine2.split(" ");
					sCurrentLine2="";  //removing last value '0' of each key
					for(int i=0; i<s1.length-1; i++){
						sCurrentLine2+=s1[i].trim();
					}
				result=computeString(sCurrentLine1,sCurrentLine2);
				//System.out.println(sCurrentLine1+","+sCurrentLine2+":"+result+" : printed");
				}
			
				else {result=sCurrentLine1; }// write in a file and save and exit
			 }
			while((sCurrentLine1=br.readLine())!=null){
				String[] s1=sCurrentLine1.split(" ");
				sCurrentLine1="";  //removing last value '0' of each key
				for(int i=0; i<s1.length-1; i++){
					sCurrentLine1+=s1[i].trim();
				}
				result=computeString(sCurrentLine1,result);
				//out.write(sCurrentLine1);
				//System.out.println(sCurrentLine1);
				}			
			  System.out.println("Longest Common Subsequence for k' documents:"+result+".");
		         // out.write(result);		     

		} catch (IOException e) {
			e.printStackTrace();
		}  
	  
  }
}