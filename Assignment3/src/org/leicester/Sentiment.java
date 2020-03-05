package org.leicester;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

//the way that i've solved this is that i use the total length of the arrays after splitbycomma to differenciate between the 2 formats of both files
//i then process accordingly based on what data i need from each file. so 1 mapper is able to handle both files. it also assigns a or b to the output pair to help identify what values each key value pair has, which helps in the combiner
//in the mapper i also filter out all data that doesnt have the everyone age rating and only emit data that contains this value

//in the combiner, based on if the values are a or b i pass 'a' values through and if the values are 'b' then i total up the valid reviews and the sentiments and check to see if the total reviews are equal to or more than 50, and pass
//the combined total on as b value, which reduces a large dataset to just a totals datavalue and passes that on.

//the reducer just checks if both a and b parts are present and they indicate those parts have made it through the screening. then if both are present it uses a 
//combined total sentiments and a combined total review amount and calculated the average and then generates the output. combined meaning values from ALL key values pairs that contain b for one key. i.e multiple b totals.


//please note for rounding i made the assumption that rounding means RoundingMode.HALF_UP 

public class Sentiment {

	public static class SentimentMapper extends Mapper<Object, Text, Text, Text>{
	
		private Text emitKey = new Text();
		private Text emitValue = new Text();
		
		public static String[] splitbycomma(String S) {
		    ArrayList<String> L = new ArrayList<String>();
		    String[] a = new String[0];
		        StringBuffer B = new StringBuffer();
		    int i = 0;
		    while (i<S.length()) {
		            int start = i;
		            int end=-1;
		            if (S.charAt(i)=='"') { // parse field enclosed in quotes
		                    B.setLength(0); // clear the StringBuffer
		                    B.append('"');
		                    i++;
		                    while (i<S.length()) {
		                        if (S.charAt(i)!='"') { // not a quote, just add to B
		                            B.append(S.charAt(i));
		                            i++;
		                        }
		                        else { // quote, need to check if next character is also quote
		                            if (i+1 < S.length() && S.charAt(i+1)=='"') {
		                                B.append('"');
		                                i+=2;
		                            }
		                            else { // no, so the quote marked the end of the String
		                                B.append('"');
		                                L.add(B.toString());
		                                i+=2;
		                                break;
		                            }
		                        }
		                    }
		            }
		            else { // standard field, extends until next comma
		                    end = S.indexOf(',',i)-1;
		                    if (end<0) end = S.length()-1;
		                    L.add(S.substring(start,end+1));
		                    i = end+2;
		            }
		    }
		    return L.toArray(a);
		}
		
		
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException{
			
			String[] data = splitbycomma(value.toString());
			
			// skip empty lines and header lines
			if (data.length==0 || data[0].equals("App")) return;
		
			//checks if its the data is in the format of file a or b and decides on what to do
			if(data.length==13) {
				//filters through everything except data with Everyone and returns the data in the format a,category. a identifies what part it is
				if(data[8].equals("Everyone")) {					
				emitKey.set(data[0]);
				String outputVal ="a,";
				outputVal = outputVal+data[1]+" ";
				emitValue.set(outputVal);
				
				}else {
					return;
				}
				
			}else {
				//filters through all nan data
				if(!data[3].equals("nan")) {
					//filters through all data with less than 0.3 and returns in format b,1,sentiment. b identifies what part this is and 1 identifies how many there are.
					if(Double.parseDouble(data[3])>=0.3) {
						emitKey.set(data[0]);
						String outputVal = "b,1,"+data[3];
						emitValue.set(outputVal);
						
					}else {
						return;
					}
				}else {
					return;
				}
			}

			//emit the data
			context.write(emitKey,emitValue);
		}
	}
	
	public static class SentimentCombiner extends Reducer<Text, Text, Text, Text>{
		
		static Text result = new Text();
		
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
			
			ArrayList<String[]> data = new ArrayList<String[]>();
			boolean containsCat = false;
			int aPos =-1;
			Double sentimentTotal = 0.0;
			int totalReviews = 0;
						
			//goes through the values and adds them into a easier to work with structure and also based on what parts are in the values, it will choose certain things to emit. it will pass the 
			//category on as a and if there are sentiment values it will total them up and the total amount of value reviews and pass them on as b
			for(Text val : values) {
				data.add(val.toString().split(","));
				
				if(data.get(data.size()-1)[0].equals("a")) {
					containsCat = true;
					aPos = data.size()-1;
				}
				
				if(data.get(data.size()-1)[0].equals("b")) {
					sentimentTotal= sentimentTotal+Double.parseDouble(data.get(data.size()-1)[2]);
					totalReviews = totalReviews + Integer.parseInt(data.get(data.size()-1)[1]);
				}
				
			}

			//filters through data that has more than 50 total reviews.
			if(totalReviews>=50){			
				result.set("b,"+totalReviews+","+sentimentTotal);
				context.write(key, result);
				
			}else if (containsCat){
				result.set(data.get(aPos)[0]+","+data.get(aPos)[1]+", ");
				context.write(key, result);
			}else
				return;
		}
	}
	
	public static class SentimentReducer extends Reducer<Text, Text, Text, Text>{
		static Text result = new Text();
		
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
		
			ArrayList<String[]> data = new ArrayList<String[]>();
			String category ="";
			Double sentimentTotal = 0.0;
			int totalReviews = 0;
			boolean foundA = false;
			boolean foundB = false;
			
			//goes through all values for every file and totals up values according to what part (a or b) they are and checks if BOTH parts are present as they are required and indicates the data piece got through the screening.
			for(Text val : values) {
				data.add(val.toString().split(","));
				
				//assigns category
				if(data.get(data.size()-1)[0].equals("a")) {
					category = data.get(data.size()-1)[1];
					foundA = true;
				}
				
				//totals up sentiment totals and review totals
				if(data.get(data.size()-1)[0].equals("b")) {
					totalReviews = totalReviews+Integer.parseInt(data.get(data.size()-1)[1]);
					sentimentTotal = sentimentTotal+Double.parseDouble(data.get(data.size()-1)[2]);
					foundB = true;
				}
			}
			
			//checks if both mandatory parts are present.
			if(!(foundA&&foundB)) 
				return;
			
			//calculates average for sentiment
			Double totalReviewsD = (double) totalReviews;
			BigDecimal temp = BigDecimal.valueOf(sentimentTotal/totalReviewsD);
			Double sentimentVal = temp.round(new MathContext(2,RoundingMode.HALF_UP)).doubleValue();
			
			//outputs result
			result.set(category+", "+totalReviews+", "+sentimentVal);
			context.write(key, result);
		}
	}
	
	public static void main(String[] args) throws Exception {

	    Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "Task2Hadoop");
		
		//this class jar
		job.setJarByClass(Sentiment.class);
		
		//sets the jobs mapper class which is Task2HadoopMapper class
		job.setMapperClass(SentimentMapper.class);
		
		//sets the jobs combiner class 
		job.setCombinerClass(SentimentCombiner.class);
		
		//sets the jobs Reducer class
		job.setReducerClass(SentimentReducer.class);
		
		//sets the output type for the key and value
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		//sets file input and file output
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
