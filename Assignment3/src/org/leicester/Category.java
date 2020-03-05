package org.leicester;

import java.io.IOException;
import java.math.BigDecimal;
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


//the approach for this one was a simple 1 in that:
//the mapper just sets the key to the category, fetches the price after using splitbycomma, if its not a 0 then it removes the $ sign and just outputs it in the format 1,price. so 1 of this price
//the combiner just aggegates the prices that are the same and emits a combined total of how many of that price are present i.e value 1,3.99 and value 1,3.99 would be combined into 2,3.99.
//the reducer, just totals the prices up (by multiplying how many of a certain price there are), totals up freeapps, totals up paid apps based on price. then calculates average price and rounds up using the BigDecimal class
//the reducer then just throws the results out.


public class Category {
	
	//Mapper
	//added to job in the main method
	public static class CategoryMapper extends Mapper<Object, Text, Text, Text>{
		
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
			
			//String S = value.toString();
			String[] A = splitbycomma(value.toString());
			String workingValue="";
			
			// skip empty lines and header lines
			if (A.length==0 || A[0].equals("App")) return;
						
			//process the line
			emitKey.set(A[1]);
			
			//get the price, if 0 use whole string and don't try to remove the $, if not 0 then its fine
			workingValue = A[7].trim();
			
			if (!workingValue.equals("0")) {
				workingValue ="1,"+workingValue.substring(1);
			}else {
				workingValue = "1,"+workingValue;
			}
			
			//set the emit value to the output val
			emitValue.set(workingValue);
		
			//return the pair
			context.write(emitKey, emitValue);
		}
	}
	
	//Combiner
	public static class CategoryCombiner extends Reducer<Text, Text, Text, Text>{
		
		static Text result = new Text();
		
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
			
			ArrayList<String[]> vals = new ArrayList<String[]>();
			int count =0;
			
			//moving the values into a easier to work with data type and splitting the data
			for (Text Data:values) {
				vals.add(Data.toString().split(","));
			}
			
			//while there is still data in the array go through once for every value in position 0 and check for duplicates and aggregate them, then emit them
			while(vals.size()!=0) {
				
				//start at 1 so it skips and doesn't remove entry at 0
				count =1;
				
				while(count<vals.size()) {
					
					//if a matching data value was found aggrgate and remove dumplicate
					if(vals.get(0)[1].equals(vals.get(count)[1])) {
						vals.get(0)[0] = String.valueOf((Integer.parseInt(vals.get(0)[0])+1));
						vals.remove(count);
					}
			
					//increment to move along array
					count = count+1;
				}
				//prepare to emit aggregated value and remove it from position 0 
				result.set(vals.get(0)[0]+","+vals.get(0)[1]);
				context.write(key, result);
				result.clear();
				vals.remove(0);
			}
		}
	}

	
	public static class CategoryReducer extends Reducer<Text, Text, Text, Text>{
		static Text result = new Text();
		
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
			
			int freeApps = 0;
			int paidApps =0;
			double currentTotal=0.0;
			
			//calculate required statistics using all values for the key
			for(Text val:values) {				
				
				//split the string via ',' and get the price 
				String v=val.toString();
				String[] splitV = v.split(",");
				double price = Double.parseDouble(splitV[1]);
				
				if(price>0) {
					//code for paid apps, to get the total multiply total found with the price, add found to the paidapp total (data from combiner)
					paidApps = paidApps+Integer.parseInt(splitV[0]);
					currentTotal=currentTotal+(price*Double.parseDouble(splitV[0]));
				}else {
					//code for free apps, add total found to the total of freeapps
					freeApps = freeApps+Integer.parseInt(splitV[0]);
				}
			}

			//computing the averages
			double dPaidApps = paidApps;
			double averagePrice = currentTotal/dPaidApps;

			//used for rounding via BigDecimal class
			if(paidApps!=0) {
			BigDecimal temp = BigDecimal.valueOf(averagePrice);
			temp = temp.setScale(2, RoundingMode.HALF_UP);
			averagePrice = temp.doubleValue();
			}else {
				averagePrice =0;
			}
			
			//prepare for emitting output data, then write to context
			String emitVal = freeApps+", "+paidApps+", "+averagePrice;
			result.set(emitVal);			
			context.write(key, result);
		}
	}
	
	
	  public static void main(String[] args) throws Exception {
		
		    Configuration conf = new Configuration();
			Job job = Job.getInstance(conf, "Task1Hadoop");
			
			//this class jar
			job.setJarByClass(Category.class);
			
			//sets the jobs mapper class which is Task1HadoopMapper class
			job.setMapperClass(CategoryMapper.class);
			
			//sets the jobs combiner class 
			job.setCombinerClass(CategoryCombiner.class);

			//sets the jobs Reducer class
			job.setReducerClass(CategoryReducer.class);


			//sets the output type for the key and value
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
			
			//sets file input and file output
			FileInputFormat.addInputPath(job, new Path(args[0]));
			FileOutputFormat.setOutputPath(job, new Path(args[1]));
			System.exit(job.waitForCompletion(true) ? 0 : 1);
	  }

}
