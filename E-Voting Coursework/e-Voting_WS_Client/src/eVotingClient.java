import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class eVotingClient {
		
		//static string used to connect to the voting service
		private static final String BASE_URI ="https://campus.cs.le.ac.uk/tyche/CO7214RestAPI4/rest/votingservice/";

		
		public static void main(String[] args) {

			//used to clear previous data
			clearDatabase();
			
			//creating the 3 motions for voting, using m(x) to simplify
            System.out.println(createMotion("m1","let_there_be_cake"));
            System.out.println(createMotion("m2","let_there_be_crisp"));
            System.out.println(createMotion("m3","let_there_be_fizzydrink"));

            //looping through and creating 10 voters using vtr+1 to easily generate multiples 
            System.out.println("Creating voters");
            //creating 10 voters
            for(int i=1; i<11; i++) {
                System.out.println(createVoter("vtr"+i));
            }

            //j used as a counter for selecting motion
            int j =1;
            int tempJ;
            int delegateVtr;
            boolean vote = true;
            
            //looping though to make 10 direct votes from 10 voters
            System.out.println("Creating Direct votes");
             for(int i=1; i <11; i++) {
            	 
            	 //keeping numbering according to motions
            	 if(j==4)
            		 j=1;
            
            	 //used to flip vote boolean between voters
            	 vote = !vote;
            	 
            	 //output if vote was successful
                 System.out.println(createVotingRecord(("m"+j),("vtr"+i),("vr"+i)));
                 
                 //casting direct vote
                 System.out.println(vote(("vtr"+i),("vr"+i), vote));
                 
                 //moving to next motion
            	 j=j+1;
             }
             
             //used to move motion selector forward, so delegates do not get the same initial motions as first votes
             j = 3;
             
             System.out.println("Setting up delegate votes");
             //10 delegated votes
             for(int i=1; i <11; i++) {
            	 
            	 //not proper way of looping around, but it works for voter10 to delegate to voter1
            	 if((i+1)>10)
            		 delegateVtr = 1;
            	 else
            		 delegateVtr = i+1;
            	             	 
            	 //keeping numbering according to motions
            	 if(j==4)
            		 j=1;
            
            	 //same as before, used to flip vote bools
            	 vote = !vote;
            	 
            	 //output if vote was successful
            	 tempJ=10+i;
            
            	 //creating votingRecord for voter
                 System.out.println(createVotingRecord(("m"+j),("vtr"+i),("vr"+tempJ)));
                 
                 //delegating freshly created vote to delegate
                 System.out.println(delegate(("vtr"+i),("vr"+tempJ),("vtr"+delegateVtr)));
                 
                 //delegate is voting
                 System.out.println(vote(("vtr"+delegateVtr),("vr"+tempJ), vote));
                 System.out.println("delegated vote "+("vr"+tempJ)+" from "+("vtr"+i)+" to "+("vtr"+delegateVtr));

                 //moving to next motion
            	 j=j+1;
             }

             //totaling up all votes
             int totalVoters;
             int totalFor;
             
             //for checking purposes
             int totalAgainst = 0;
             
             //looping through all motions to count up total votes
             for (int i=1 ; i<4; i++) {
            	 List<String> votersForMotion = getVotersForMotion("m"+i);
            	 totalVoters = 0;
            	 totalFor = 0;
            	 totalAgainst = 0;
            	 
            	 //fetching all voters from a motion
            	 for(int check=0; check<votersForMotion.size(); check++ ) {
            		 totalVoters = totalVoters +1;
            	 
            		 //using this custom method to check if the voter has voted on the motion and to fetch their response by parsing the JSON
            		 if (parseVoteForFromVoteRecordsJSON(getVotingRecordForVoter(votersForMotion.get(check)),"m"+i))
            			 totalFor = totalFor+1;
            		 //for test purposes
            		 else
            			 totalAgainst = totalAgainst +1;
            	 }
             
            	 //submitting the motion results
            	 System.out.println("Submitting results for motion "+("m"+i)+", total Votes = "+totalVoters+", Total Voters In Favour = "+totalFor+", "+totalAgainst);
            	 
            	 //outputting true or false as to whether or not the submission was successful 
            	 if (submitResultForMotion(("m"+i), totalVoters, totalFor).equals("true"))
            		 System.out.println("Submission complete");
            	 else System.out.println("Submission failed");
             }
    
             
             //experimental code 
            
            //testing if voter can create multiple voting records for the same motion
          /*System.out.println("Experiement code:");
            System.out.println(createVotingRecord("m1","vtr1","test1"));
            System.out.println(createVotingRecord("m1","vtr1","test2"));
            System.out.println(delegate("vtr1", "test1", "vtr2"));

            clearDatabase();
            
            System.out.println(createVotingRecord("m1","vtr1","test1"));
            System.out.println(delegate("vtr1", "test1", "vtr2"));
            System.out.println(createVotingRecord("m1","vtr1","test2"));
            
            clearDatabase();*/
            	
		}
		
		public static String getHello() {     

			Client client = ClientBuilder.newClient();

			WebTarget target =client.target(BASE_URI+MessageFormat.format("hello",new Object[] {}));

			return target.request(MediaType.TEXT_PLAIN).get(String.class); //resquest TEXT_PLAIN String as defined in the Server side 

		} 
		
		//method used to create a motion
		//createMotion/{userID}/{motion_id}/{text}
		public static String createMotion(String motionID, String motionName) {
			
			Client client = ClientBuilder.newClient();
			
			//putting together the request using the provided parameters
			WebTarget target =client.target(BASE_URI+MessageFormat.format(("createMotion/sk2727/"+motionID+"/"+motionName),new Object[] {}));
			
			//returning the result
			return target.request(MediaType.APPLICATION_JSON).get(String.class);
		}
		
		//used to create a voting record
		//createVotingRecord/{userID}/{motion_id}/{voter_id}/{record_id}
		public static String createVotingRecord(String motionID, String voterID, String newRecordID) {
			
			Client client = ClientBuilder.newClient();
			
			//passing in the parameters and creating the request
			WebTarget target =client.target(BASE_URI+MessageFormat.format("createVotingRecord/sk2727/"+motionID+"/"+voterID+"/"+newRecordID,new Object[] {}));
			return target.request(MediaType.APPLICATION_JSON).get(String.class);
		}
		
		//used to vote
		//vote/{userID}/{voter_id}/{record_id}/{vote}
		public static String vote(String voterID, String votingRecordID, boolean vote) {
			
			Client client = ClientBuilder.newClient();
			
			//getting the voter id, record id and the vote to cast
			WebTarget target =client.target(BASE_URI+MessageFormat.format("vote/sk2727/"+voterID+"/"+votingRecordID+"/"+vote,new Object[] {}));
			return target.request(MediaType.APPLICATION_JSON).get(String.class);
		}
		
		//used for fetching voters on a motion
		//getVotersForMotion/{userID}/{motion_id}
		public static List<String> getVotersForMotion(String motionID) {
			
			Client client = ClientBuilder.newClient();
			WebTarget target =client.target(BASE_URI+MessageFormat.format("getVotersForMotion/sk2727/"+motionID,new Object[] {}));
			
			String tempJSON = (target.request(MediaType.APPLICATION_JSON).get(String.class));
			String buffer="";
			List<String> out = new ArrayList<String>();
			//parsing and splitting and putting json array into a list
			for(int i =0; i<tempJSON.length(); i++) {
				if((tempJSON.charAt(i)!='"')&&(tempJSON.charAt(i)!='[')&&(tempJSON.charAt(i)!=']')&&(tempJSON.charAt(i)!=','))
				buffer = buffer +tempJSON.charAt(i);
				
				if(tempJSON.charAt(i)==',') {
					out.add(buffer);
					buffer= "";
				}
			}
			out.add(buffer);
			
			//returning output
			return out;
		}
		
		//used for fetching voting records for a voter
		//getVotingRecordsForVoter/{userID}/{voter_id}
		public static String getVotingRecordForVoter(String voterID) {
			
			Client client = ClientBuilder.newClient();
			WebTarget target =client.target(BASE_URI+MessageFormat.format("getVotingRecordsForVoter/sk2727/"+voterID,new Object[] {}));
			
			return target.request(MediaType.APPLICATION_JSON).get(String.class);
		}
		
		//used to wipe the database
		//clearDatabase/{userID}
		public static String clearDatabase() {
			
			Client client = ClientBuilder.newClient();
			WebTarget target =client.target(BASE_URI+MessageFormat.format("clearDatabase/sk2727/",new Object[] {}));
			
			return target.request(MediaType.APPLICATION_JSON).get(String.class);
		}
		
		//used for creating a new voter
		//createVoter/{userID}/{voter_id}
		public static String createVoter(String newVoterID) {
			
			Client client = ClientBuilder.newClient();
			WebTarget target =client.target(BASE_URI+MessageFormat.format("createVoter/sk2727/"+newVoterID,new Object[] {}));
			return target.request(MediaType.APPLICATION_JSON).get(String.class);
			
		}
		
		//used to delegate a vote
		//delegate/{userID}/{voter_id}/{record_id}/{delegate_id}
		public static String delegate(String voterID, String recordID, String delegateID) {
			
			Client client = ClientBuilder.newClient();
			
			//setting up the delegate request
			WebTarget target =client.target(BASE_URI+MessageFormat.format("delegate/sk2727/"+voterID+"/"+recordID+"/"+delegateID,new Object[] {}));
			
			//returning whether successful
			return target.request(MediaType.APPLICATION_JSON).get(String.class);
		}
		
		//used to submit results for a motion
		//submitResultForMotion/{userID}/{motion_id}/{nr_of_votes}/{nr_in_favour}
		public static String submitResultForMotion(String motionID, int nr_of_votes, int nr_in_favour) {
			
		Client client = ClientBuilder.newClient();
			
			//setting up the submit request
			WebTarget target =client.target(BASE_URI+MessageFormat.format("submitResultForMotion/sk2727/"+motionID+"/"+nr_of_votes+"/"+nr_in_favour,new Object[] {}));
			
			//returning whether successful
			return target.request(MediaType.APPLICATION_JSON).get(String.class);
		}
		
		//custom parse used to go through the votingrecords JSON and check then fetch a motion vote boolean
		public static boolean parseVoteForFromVoteRecordsJSON(String totalVRJSON, String motionToSearchFor) {            
            
            String textBuffer="";
            String voteBoolBuffer="";
            String motionID="";
            for(int i = 0; i <totalVRJSON.length(); i++) {
            
            //parsing though JSON
            if(totalVRJSON.charAt(i)=='"') {
            	
            	//looking for the key "voted_yes" as is first in the JSON data order
            	if(textBuffer.equals("voted_yes")) {
            		int tempi= i+1;
            		voteBoolBuffer ="";
            		while(totalVRJSON.charAt(tempi)!=','){
            			if(totalVRJSON.charAt(tempi)!=':') {
            				//if found, will hold onto it
            			voteBoolBuffer = voteBoolBuffer + totalVRJSON.charAt(tempi);
            			}
            			tempi = tempi+1;
            		}
            		            		
            		//now parsing though looking for required motion_id
            	}else if (textBuffer.equals("motion_id")) {
            		int tempj = i+3;
            		motionID ="";
            		while(totalVRJSON.charAt(tempj)!='"') {
            			motionID= motionID+totalVRJSON.charAt(tempj);
            			
            			tempj = tempj+1;
            		}
            		
            		//when found a motion_id, compare to check if this is the required one
            		if (motionID.equals(motionToSearchFor))
            			//if correct one then return the vote decision
            			if(voteBoolBuffer.equals("true"))
            				return true;
            			else return false;
            		
            	}else {
            	
            		textBuffer = "";
            	}
            }else {
            	//if ordinary letter then just append to end of buffer string
                textBuffer = textBuffer+totalVRJSON.charAt(i);

            }
            }
            return false;
		}	
	}
